package com.astroscoding.githuber.popularrepos.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astroscoding.githuber.common.data.preferences.RepoPreferences
import com.astroscoding.githuber.common.domain.model.Sort
import com.astroscoding.githuber.common.util.*
import com.astroscoding.githuber.popularrepos.presentation.usecase.DeleteAllReposUseCase
import com.astroscoding.githuber.popularrepos.presentation.usecase.GetPopularReposUseCase
import com.astroscoding.githuber.popularrepos.presentation.usecase.StoreReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class PopularReposViewModel @Inject constructor(
    private val getPopularReposUseCase: GetPopularReposUseCase,
    private val storeReposUseCase: StoreReposUseCase,
    private val repoPreferences: RepoPreferences,
    private val deleteAllReposUseCase: DeleteAllReposUseCase,
    private val dispatcher: CoroutineContext
) : ViewModel() {

    var lang: String by mutableStateOf(Constants.DEFAULT_LANGUAGE)
        private set
    var sort: Sort by mutableStateOf(Sort.EmptySort)
        private set


    private val _state = MutableStateFlow(PopularReposState())
    val state = _state.asStateFlow()
    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable -> onFailure(throwable) }
    var currentPage = 1
        private set

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
                repoPreferences.language,
                repoPreferences.sortOrder,
                _refresh
            ) { language, sortOrder, shouldRefresh ->
                Triple(language, sortOrder, shouldRefresh)
            }.flatMapLatest { (language, sortOrder, _) ->
                lang = language
                sort = sortOrder
                getLocalRepos(sortOrder)
            }.collect { reposFromDatabase ->
                if (reposFromDatabase.isEmpty() || _refresh.value || _languageChanged.value) {
                    getReposFromRemote(formQuery(language = lang), sort)
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
        getPopularReposUseCase(sortOrder)
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
                getPopularReposUseCase(query = query, sort = sortOrder, page = currentPage)
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
            val newRepos = getPopularReposUseCase(
                formQuery(language = lang),
                sort,
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