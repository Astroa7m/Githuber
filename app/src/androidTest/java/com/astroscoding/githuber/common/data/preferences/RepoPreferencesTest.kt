package com.astroscoding.githuber.common.data.preferences

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.astroscoding.githuber.common.util.Constants
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
import java.io.File
import javax.inject.Inject

/**
 * Unfortunately each test should run alone and manually as there's an error
 * will be thrown complaining about having multiple instances of datastore though it is singleton in
 * test module. However, the error seems like it does not happen often so I will leave it for now.
 */

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
    fun getInitialLanguageFromInitially_ReturnsDefaultValue() = runTest {
        repoPreferences.language.test {
            assertThat(awaitItem()).isEqualTo(Constants.DEFAULT_LANGUAGE)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getInitialSortFromInitially_ReturnsDefaultValue() = runTest {
        repoPreferences.sortOrder.test {
            assertThat(awaitItem()).isEqualTo(Constants.DEFAULT_SORT_TYPE)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun storeLanguage_ReturnsNewlyStoredLanguage() = runTest {
        repoPreferences.changeLanguage(LANGUAGE)

        repoPreferences.language.test {
            assertThat(awaitItem()).isEqualTo(LANGUAGE)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun storeSortOrder_ReturnsNewlyStoredSortOrder() = runTest {
        repoPreferences.changeSortOrder(SORT_ORDER)

        repoPreferences.sortOrder.test {
            assertThat(awaitItem()).isEqualTo(SORT_ORDER)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun teardown() {
        //useless
        File(context.filesDir, "datastore/test_pref_file.preferences_pb").delete()
    }

}