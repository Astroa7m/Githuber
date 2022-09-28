package com.astroscoding.search.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astroscoding.common.data.preferences.RepoPreferences
import com.astroscoding.common.domain.model.Sort
import com.astroscoding.common.presentation.usecase.GetReposUseCase
import com.astroscoding.common.util.exceptions.BadQueryException
import com.astroscoding.common.util.Constants
import com.astroscoding.common.util.formQuery
import com.astroscoding.common.util.languageSymbolToLanguageChar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SearchReposViewModel @Inject constructor(
    private val getReposUseCase: GetReposUseCase,
    private val repoPreferences: RepoPreferences,
    private val dispatcher: CoroutineContext
) : ViewModel() {
    private var searchJob: Job? = null
    val language = repoPreferences.language.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), Constants.DEFAULT_LANGUAGE
    )
    val sort = repoPreferences.sortOrder.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), Sort.EmptySort
    )
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _state = MutableStateFlow(SearchReposState())
    val state = _state.asStateFlow()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable -> onFailure(throwable) }

    private var currentPage = 1
    private val _languageChanged = MutableStateFlow(false)
    private val _shouldAnimateToStartOfTheList = MutableStateFlow(false)
    val shouldAnimateToStartOfTheList = _shouldAnimateToStartOfTheList.asStateFlow()

    var searchScreenIsCurrentlyOpen = mutableStateOf(false)

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

    fun onEvent(event: SearchReposUIEvent) {
        when (event) {
            SearchReposUIEvent.LoadMoreRepos -> {
                if (!state.value.isLoadingMoreItems)
                    loadMoreItems()
            }
            is SearchReposUIEvent.SelectNewSort -> {
                viewModelScope.launch(dispatcher + exceptionHandler) {
                    currentPage = 1
                    _shouldAnimateToStartOfTheList.update { true }
                    repoPreferences.changeSortOrder(event.newSort)
                }
            }
            is SearchReposUIEvent.SelectNewLanguage -> {
                viewModelScope.launch(dispatcher + exceptionHandler) {
                    currentPage = 1
                    _shouldAnimateToStartOfTheList.update { true }
                    repoPreferences.changeLanguage(languageSymbolToLanguageChar(event.newLanguage))
                    _languageChanged.update { true }
                }
            }
            is SearchReposUIEvent.InputQuery -> {
                currentPage = 1
                onSearchQueryChanged(event.query)
            }
            SearchReposUIEvent.CloseSearch -> {
                searchJob?.cancel()
                _searchQuery.update { "" }
                _state.update { SearchReposState() }
            }
            SearchReposUIEvent.SearchRepos -> {
                if (_searchQuery.value.isNotEmpty())
                    searchReposLocally()
                else
                    _state.update {
                        it.copy(errorMessage = "Please fill the search query")
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
                formQuery(_searchQuery.value.trim(), language = language.value),
                sort.value,
                currentPage
            )
            _state.update {
                SearchReposState(repos = it.repos + newRepos)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun searchReposLocally() {
        viewModelScope.launch(viewModelScope.coroutineContext + exceptionHandler) {
            // searching in local first
            repoPreferences.language.combine(repoPreferences.sortOrder) { _, _ -> }.flatMapLatest {
                getReposUseCase(sort.value, _searchQuery.value.trim())
            }.collect { localRepos ->
                if (searchScreenIsCurrentlyOpen.value){
                    if (localRepos.isEmpty()) {
                        // search remotely
                        searchReposRemotely()
                    } else {
                        _state.update {
                            SearchReposState(repos = localRepos)
                        }
                    }
                }
            }
        }
    }

    private fun searchReposRemotely() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(dispatcher + exceptionHandler) {
            _state.update {
                it.copy(loading = true)
            }
            val remoteRepos = getReposUseCase(
                query = formQuery(_searchQuery.value.trim(), language.value),
                sort = sort.value,
                page = currentPage
            )
            _state.update {
                SearchReposState(repos = remoteRepos)
            }
        }
        searchJob?.invokeOnCompletion {
            Log.d("DEBUG_MATE", "searchReposRemotely: Job Cancelled: ${it?.stackTrace}")
        }
    }

    private fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.update { newQuery }
    }

    fun onDoneAnimatingToStartOfList() {
        _shouldAnimateToStartOfTheList.update { false }
    }

}