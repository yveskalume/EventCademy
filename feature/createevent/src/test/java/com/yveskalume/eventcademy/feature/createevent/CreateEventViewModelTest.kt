package com.yveskalume.eventcademy.feature.createevent

import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import com.yveskalume.eventcademy.core.testing.data.eventTestData
import com.yveskalume.eventcademy.core.testing.repository.FailingFakeEventRepository
import com.yveskalume.eventcademy.core.testing.repository.FakeEventRepository
import com.yveskalume.eventcademy.core.testing.util.MainDispatcherRule
import com.yveskalume.eventcademy.feature.createevent.CreateEventUiState.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertIs

class CreateEventViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeEventRepository: EventRepository = FakeEventRepository()
    lateinit var successViewModel: CreateEventViewModel

    private val failingFakeEventRepository: EventRepository = FailingFakeEventRepository()
    lateinit var failingViewModel: CreateEventViewModel

    @Before
    fun setUp() {
        successViewModel = CreateEventViewModel(fakeEventRepository)
        failingViewModel = CreateEventViewModel(failingFakeEventRepository)
    }

    @Test
    fun `state is Initially`() = runTest {
        val uiState = successViewModel.uiState.value
        assertIs<Initial>(uiState)
    }

    @Test
    fun `when createEvent is called then the Event is added`() = runTest {
        val event = Event(
            uid = "testUid",
        )
        successViewModel.createEvent(event)
        val uiState = successViewModel.uiState.value

        assertIs<Success>(uiState)
        assertContains(eventTestData, event)
    }

    @Test
    fun `when the repository throws an exception then state is Error`() = runTest {
        val event = Event(
            uid = "testUid",
        )
        failingViewModel.createEvent(event)
        val uiState = failingViewModel.uiState.value

        assertIs<Error>(uiState)
    }

}