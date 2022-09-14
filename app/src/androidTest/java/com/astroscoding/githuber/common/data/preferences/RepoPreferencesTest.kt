package com.astroscoding.githuber.common.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.astroscoding.githuber.common.util.Constants
import com.astroscoding.githuber.common.domain.model.Sort
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import javax.inject.Inject

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RepoPreferencesTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repoPreferences: RepoPreferencesImpl

    companion object {
        private const val LANGUAGE = "C++"
        private val SORT_ORDER = Sort.Forks
    }


    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun getInitialLanguage_returnsDefaultValue() = runTest {
        repoPreferences.language.test {
            assertThat(awaitItem()).isEqualTo(Constants.DEFAULT_LANGUAGE)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getInitialSort_returnsDefaultValue() = runTest {
        repoPreferences.sortOrder.test {
            assertThat(awaitItem()).isEqualTo(Constants.DEFAULT_SORT_TYPE)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun storeLanguage_returnsNewlyStoredLanguage() = runTest {
        repoPreferences.changeLanguage(LANGUAGE)

        repoPreferences.language.test {
            assertThat(awaitItem()).isEqualTo(LANGUAGE)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun storeSortOrder_returnsNewlyStoredSortOrder() = runTest {
        repoPreferences.changeSortOrder(SORT_ORDER)

        repoPreferences.sortOrder.test {
            assertThat(awaitItem()).isEqualTo(SORT_ORDER)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun teardown() {
        File(context.filesDir, "datastore").deleteRecursively()
    }

}