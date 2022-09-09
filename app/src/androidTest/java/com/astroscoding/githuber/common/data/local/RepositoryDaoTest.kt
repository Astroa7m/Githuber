package com.astroscoding.githuber.common.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.astroscoding.githuber.common.data.remote.util.TestFunctions.generateRepoEntity
import com.astroscoding.githuber.common.data.remote.util.TestFunctions.generateReposEntity
import com.astroscoding.githuber.common.domain.model.Sort
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class RepositoryDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val taskRuler = InstantTaskExecutorRule()

    @Inject
    lateinit var db: RepositoryDatabase

    private lateinit var repoDao: RepositoryDao

    @Before
    fun setup() {
        hiltRule.inject()
        repoDao = db.repositoriesDao
    }

    @Test
    fun testInsertingOneRepo() = runTest {
        val repo = generateReposEntity().first()
        repoDao.insertRepo(repo)
        repoDao.getAllRepos(Sort.Stars.sort).test {
            assertThat(awaitItem()).contains(repo)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testInsertingMultipleRepos() = runTest {
        val repos = generateReposEntity(3)
        repos.forEach {
            repoDao.insertRepo(it)
        }
        repoDao.getAllRepos(Sort.Stars.sort).test {
            assertThat(awaitItem()).hasSize(3)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDeletingRepos() = runTest {
        val repos = generateReposEntity(3)
        repos.forEach {
            repoDao.insertRepo(it)
        }
        repoDao.getAllRepos(Sort.Stars.sort).test {
            assertThat(awaitItem()).containsExactlyElementsIn(repos)
            cancelAndIgnoreRemainingEvents()
        }
        repoDao.deleteAllRepos()
        repoDao.getAllRepos(Sort.Stars.sort).test {
            assertThat(awaitItem()).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testAddingMultipleItemsWithSameIdResultsReplacing() = runTest {
        val repo0 = generateRepoEntity(1)
        val repo1 = generateRepoEntity(1)
        repoDao.insertRepo(repo0)
        repoDao.insertRepo(repo1)
        repoDao.getAllRepos(Sort.Stars.sort).test {
            assertThat(awaitItem()).hasSize(1)
        }
    }

    @Test
    fun verifyItemsAreSortedAccordingToForks() = runTest {
        val repos = generateReposEntity(3, forksCounts = listOf(3, 5, 10))
        repos.forEach { repo->
            repoDao.insertRepo(repo)
        }
        repoDao.getAllRepos(Sort.Forks.sort).test {
            assertThat(awaitItem()).isEqualTo(repos.sortedByDescending { it.forksCount })
        }
    }

    @Test
    fun verifyItemsAreSortedAccordingToStars() = runTest {
        val repos = generateReposEntity(3, starsCounts = listOf(3, 5, 10))
        repos.forEach { repo->
            repoDao.insertRepo(repo)
        }
        repoDao.getAllRepos(Sort.Stars.sort).test {
            assertThat(awaitItem()).isEqualTo(repos.sortedByDescending { it.starsCount })
        }
    }

    @Test
    fun verifyItemsAreSortedAccordingToIssues() = runTest {
        val repos = generateReposEntity(3, starsCounts = listOf(3, 5, 10))
        repos.forEach { repo->
            repoDao.insertRepo(repo)
        }
        repoDao.getAllRepos(Sort.Forks.sort).test {
            assertThat(awaitItem()).isEqualTo(repos.sortedByDescending { it.issuesCount })
        }
    }

    @After
    fun teardown() {
        db.close()
    }
}