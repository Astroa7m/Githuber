package com.astroscoding.githuber.popularrepos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astroscoding.githuber.common.data.preferences.RepoPreferences
import com.astroscoding.githuber.common.domain.model.Sort
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
    private val dispatcher: CoroutineContext,
    private val deleteAllReposUseCase: DeleteAllReposUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PopularReposState())
    val state = _state.asStateFlow()
    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable -> onFailure(throwable) }

    // TODO: think of something so we don't leave the ui empty while fetching
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        getRepos()
    }

    private fun getRepos() {
        viewModelScope.launch(viewModelScope.coroutineContext + exceptionHandler) {
            combine(
                repoPreferences.language,
                repoPreferences.sortOrder,
                searchQuery
            ) { language, sortOrder, searchQuery ->
                getLocalRepos(language, sortOrder, searchQuery)
            }.collect()
        }
    }

    private suspend fun getLocalRepos(language: String, sortOrder: Sort, searchQuery: String) {
        withContext(dispatcher) {
            getPopularReposUseCase(sortOrder)
                .collect { reposFromDatabase ->
                    if (reposFromDatabase.isEmpty())
                        getReposFromRemote(formQuery(searchQuery, language), sortOrder)
                    _state.update {
                        PopularReposState(repos = reposFromDatabase)
                    }
                }
        }
    }

    private fun onFailure(throwable: Throwable) {
        _state.update {
            PopularReposState(
                errorMessage = throwable.localizedMessage ?: "Some went wrong"
            )
        }
    }


    private suspend fun getReposFromRemote(query: String, sortOrder: Sort) {
        _state.update { PopularReposState(loading = true) }
        val reposFromRemote = getPopularReposUseCase(query = query, sort = sortOrder)
        storeReposUseCase(reposFromRemote)
    }

    fun onEvent(event: PopularReposUIEvent) {
        when (event) {
            // since deletion of items in db will fire-off the collection of the
            // flow returned from db, so we delete all the items in order to invoke
            // network call when db list is empty
            PopularReposUIEvent.RefreshRepos -> {
                deleteAllRepos()
            }
            PopularReposUIEvent.RequestMoreRepos -> {

            }
        }
    }

    private fun deleteAllRepos() {
        viewModelScope.launch(dispatcher) {
            deleteAllReposUseCase()
        }
    }

    private fun formQuery(queryString: String, language: String) = "$queryString+language:$language"

}