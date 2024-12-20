package com.yveskalume.eventcademy.feature.createBlogPost

import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.repository.PostRepository
import com.yveskalume.eventcademy.core.testing.data.eventTestData
import com.yveskalume.eventcademy.core.testing.data.postTestData
import com.yveskalume.eventcademy.core.testing.repository.FailingFakePostRepository
import com.yveskalume.eventcademy.core.testing.repository.FakePostRepository
import com.yveskalume.eventcademy.core.testing.util.MainDispatcherRule
import com.yveskalume.eventcademy.feature.createevent.CreateEventUiState
import com.yveskalume.eventcademy.feature.createevent.CreateEventUiState.Initial
import com.yveskalume.eventcademy.feature.createevent.CreateEventUiState.Success
import com.yveskalume.eventcademy.feature.createevent.CreateEventViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import kotlin.test.assertContains
import kotlin.test.assertIs

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CreatePostViewModelTest {


   @get: Rule
   val dispatcherRule = MainDispatcherRule()

    private val fakePostRepository: PostRepository = FakePostRepository()
    lateinit var successViewModel: CreatePostViewModel

    private val failingFakePostRepository: PostRepository = FailingFakePostRepository()
    lateinit var failingViewModel: CreatePostViewModel

    @Before
    fun setUp() {
        successViewModel = CreatePostViewModel(fakePostRepository)
        failingViewModel = CreatePostViewModel(failingFakePostRepository)
    }


    @Test
    fun `state is Initially`() = runTest {
        val uiState = successViewModel.uiState.value
        assertIs<CreatePostUiState.Initial>(uiState)
    }

    @Test
    fun `when createPost is called then the Post is added`() = runTest {
        val post = Post(
            uid = "testUid",
        )
        successViewModel.createPost(post)
        val uiState = successViewModel.uiState.value

        assertIs<CreatePostUiState.Success>(uiState)
        assertContains(postTestData, post)
    }


    @Test
    fun `when the repository throws an exception then state is Error`() = runTest {
        val post = Post(
            uid = "testUid",
        )
        failingViewModel.createPost(post)
        val uiState = failingViewModel.uiState.value

        assertIs< CreatePostUiState.Error>(uiState)
    }

}