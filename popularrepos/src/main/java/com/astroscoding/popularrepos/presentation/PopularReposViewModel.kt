package com.astroscoding.popularrepos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astroscoding.common.data.preferences.RepoPreferences
import com.astroscoding.common.domain.model.Sort
import com.astroscoding.common.presentation.usecase.GetReposUseCase
import com.astroscoding.common.util.exceptions.BadQueryException
import com.astroscoding.common.util.Constants
import com.astroscoding.common.util.formQuery
import com.astroscoding.common.util.languageSymbolToLanguageChar
import com.astroscoding.popularrepos.presentation.usecase.DeleteAllReposUseCase
import com.astroscoding.popularrepos.presentation.usecase.StoreReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class PopularReposViewModel @Inject constructor(
    private val getReposUseCase: GetReposUseCase,
    private val storeReposUseCase: StoreReposUseCase,
    private val repoPreferences: RepoPreferences,
    private val deleteAllReposUseCase: DeleteAllReposUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel() {

    val language = repoPreferences.language.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), Constants.DEFAULT_LANGUAGE
    )
    val sort = repoPreferences.sortOrder.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), Sort.EmptySort
    )


    private val _state = MutableStateFlow(PopularReposState())
    val state = _state.asStateFlow()
    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable -> onFailure(throwable) }

    private var currentPage = 1

    private val _refresh = MutableStateFlow(false)
    private val _languageChanged = MutableStateFlow(false)

    private val _shouldAnimateToStartOfTheList = MutableStateFlow(false)
    val shouldAnimateToStartOfTheList = _shouldAnimateToStartOfTheList.asStateFlow()


    init {
        getRepos()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getRepos() {
        viewModelScope.launch(viewModelScope.coroutineContext + exceptionHandler) {
            combine(
                language,
                sort,
                _refresh
            ) { _, _, _ ->
            }.flatMapLatest {
                getLocalRepos(sort.value)
            }.collect { reposFromDatabase ->
                if (reposFromDatabase.isEmpty() || _refresh.value || _languageChanged.value) {
                    getReposFromRemote(formQuery(language = language.value), sort.value)
                }
                if (!_state.value.loading) {
                    _state.update {
                        PopularReposState(
                            repos = reposFromDatabase
                        )
                    }
                    if (_refresh.value)
                        _refresh.value = false
                }
            }

        }
    }

    private suspend fun getLocalRepos(
        sortOrder: Sort
    ) = withContext(dispatcher) {
        getReposUseCase(sortOrder)
    }


    private fun onFailure(throwable: Throwable) {
        if (throwable is BadQueryException) {
            viewModelScope.launch {
                repoPreferences.changeLanguage(Constants.DEFAULT_LANGUAGE)
                repoPreferences.changeSortOrder(Constants.DEFAULT_SORT_TYPE)
            }
        }
        _state.update {
            it.copy(
                loading = false,
                errorMessage = throwable.message ?: "Some Error Occurred"
            )
        }
    }


    private fun getReposFromRemote(query: String, sortOrder: Sort) {
        viewModelScope.launch(exceptionHandler) {
            // if we are already fetching do not re-fetch
            if (state.value.loading)
                return@launch
            _state.update { it.copy(loading = true) }
            val reposFromRemote =
                getReposUseCase(query = query, sort = sortOrder, page = currentPage)
            deleteAllReposUseCase()
            storeReposUseCase(reposFromRemote)
            _state.update { it.copy(loading = false) }
            if (_languageChanged.value)
                _languageChanged.value = false
        }
    }

    fun onEvent(event: PopularReposUIEvent) {
        when (event) {
            PopularReposUIEvent.RefreshRepos -> {
                //deleteAllRepos()
                currentPage = 1
                _refresh.value = true
            }
            PopularReposUIEvent.RequestMoreRepos -> {
                if (!state.value.isLoadingMoreItems)
                    loadMoreItems()
            }
            is PopularReposUIEvent.SelectNewSort -> {
                viewModelScope.launch(dispatcher + exceptionHandler) {
                    currentPage = 1
                    _shouldAnimateToStartOfTheList.value = true
                    repoPreferences.changeSortOrder(event.newSort)
                }
            }
            is PopularReposUIEvent.SelectNewLanguage -> {
                viewModelScope.launch(dispatcher + exceptionHandler) {
                    currentPage = 1
                    _shouldAnimateToStartOfTheList.value = true
                    repoPreferences.changeLanguage(languageSymbolToLanguageChar(event.newLanguage))
                    _languageChanged.value = true
                }
            }
        }
    }

    private fun loadMoreItems() {
        currentPage++
        _state.update {
            it.copy(loading = true, isLoadingMoreItems = true)
        }
        viewModelScope.launch(dispatcher + exceptionHandler) {
            val newRepos = getReposUseCase(
                formQuery(language = language.value),
                sort.value,
                currentPage
            )
            // won't save new fetched items locally as I only want to have 30 items when I relaunch the app
            _state.update {
                PopularReposState(repos = it.repos + newRepos)
            }
        }
    }

    fun onDoneAnimatingToStartOfList() {
        _shouldAnimateToStartOfTheList.value = false
    }

}