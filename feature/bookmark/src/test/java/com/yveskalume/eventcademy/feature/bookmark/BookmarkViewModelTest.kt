package com.yveskalume.eventcademy.feature.bookmark

import com.yveskalume.eventcademy.core.domain.repository.EventBookingRepository
import com.yveskalume.eventcademy.core.testing.data.eventBookingTestData
import com.yveskalume.eventcademy.core.testing.repository.FakeEventBookingRepository
import com.yveskalume.eventcademy.core.testing.repository.FailingFakeEventBookingRepository
import com.yveskalume.eventcademy.core.testing.util.MainDispatcherRule
import com.yveskalume.eventcademy.feature.bookmark.BookmarkUiState.Error
import com.yveskalume.eventcademy.feature.bookmark.BookmarkUiState.Loading
import com.yveskalume.eventcademy.feature.bookmark.BookmarkUiState.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class BookmarkViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val eventBookingRepository: EventBookingRepository = FakeEventBookingRepository()
    private lateinit var viewModel: BookmarkViewModel

    private val failingEventBookingRepository: EventBookingRepository =
        FailingFakeEventBookingRepository()
    private lateinit var failingBookViewModel: BookmarkViewModel

    @Before
    fun setUp() {
        viewModel = BookmarkViewModel(eventBookingRepository)
        failingBookViewModel = BookmarkViewModel(failingEventBookingRepository)
    }

    @Test
    fun `state is Initially Loading`() {
        val uiState = viewModel.uiState.value
        assertIs<Loading>(uiState)
    }

    @Test
    fun `when getAllUserEventBookings is called then return a list of event bookings`() = runTest {

        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val uiState = viewModel.uiState.value
        assertIs<Success>(uiState)
        assertEquals(eventBookingTestData.size, uiState.eventBookings.size)

        collectJob.cancel()
    }

    @Test
    fun `when getAllUserEventBookings throws an exception then state is Error`() = runTest {

        val collectJob = launch(UnconfinedTestDispatcher()) {
            failingBookViewModel.uiState.collect()
        }
        val uiState = failingBookViewModel.uiState.value

        assertIs<Error>(uiState)
        assertEquals("An error occurred", uiState.message)

        collectJob.cancel()
    }
}