package com.astroscoding.githuber.popularrepos.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.astroscoding.githuber.common.domain.repository.PopularRepositoriesRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PopularReposViewModelTest{

    @get:Rule
    val hiltRule = HiltAndroidRule(this)




    @Before
    fun setup(){
        hiltRule.inject()

    }

}