package com.yveskalume.eventcademy.feature.forumhome

import com.yveskalume.eventcademy.core.domain.repository.PostRepository
import com.yveskalume.eventcademy.core.testing.repository.FailingFakePostRepository
import com.yveskalume.eventcademy.core.testing.repository.FakePostRepository
import com.yveskalume.eventcademy.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import kotlin.test.assertIs

class BlogHomeViewModelTest {
    @get: Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakePostRepository: PostRepository = FakePostRepository()
    lateinit var successViewModel: BlogHomeViewModel

    private val failingFakePostRepository: PostRepository = FailingFakePostRepository()
    lateinit var failingViewModel: BlogHomeViewModel


    @Before
    fun setUp(){
        successViewModel = BlogHomeViewModel(fakePostRepository)
        failingViewModel = BlogHomeViewModel(failingFakePostRepository)
    }

    @Test
    fun `state is Initially Loading`() = runTest {
        val uiState = successViewModel.uiState.value
        assertIs<BlogHomeUiState.Loading>(uiState)
    }

    @Test
    fun `post is loaded successfully`() = runTest{
        val collectJob = launch(UnconfinedTestDispatcher()) { successViewModel.uiState.collect() }
        val uiState = successViewModel.uiState.value
        assertIs<BlogHomeUiState.Success>(uiState)
        collectJob.cancel()
    }

    @Test
    fun `when the repository throw an exception then state is Error`() = runTest{
        val collectJob = launch(UnconfinedTestDispatcher()) { failingViewModel.uiState.collect() }
        val uiState = failingViewModel.uiState.value
        assertIs<BlogHomeUiState.Error>(uiState)
        collectJob.cancel()
    }
}