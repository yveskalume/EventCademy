package com.yveskalume.eventcademy.feature.home

import com.yveskalume.eventcademy.core.domain.repository.AdvertisementRepository
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import com.yveskalume.eventcademy.core.testing.repository.FailingFakeAdvertisementRepository
import com.yveskalume.eventcademy.core.testing.repository.FailingFakeEventRepository
import com.yveskalume.eventcademy.core.testing.repository.FakeAdvertisementRepository
import com.yveskalume.eventcademy.core.testing.repository.FakeEventRepository
import com.yveskalume.eventcademy.core.testing.util.MainDispatcherRule
import com.yveskalume.eventcademy.feature.home.HomeUiState.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeEventRepository: EventRepository = FakeEventRepository()
    private val fakeAdvertisementRepository: AdvertisementRepository = FakeAdvertisementRepository()
    lateinit var successViewModel: HomeViewModel

    private val failingFakeEventRepository: EventRepository = FailingFakeEventRepository()
    private val failingFakeAdvertisementRepository: AdvertisementRepository =
        FailingFakeAdvertisementRepository()
    lateinit var failingViewModel: HomeViewModel

    @Before
    fun setUp() {
        successViewModel = HomeViewModel(fakeAdvertisementRepository, fakeEventRepository)
        failingViewModel =
            HomeViewModel(failingFakeAdvertisementRepository, failingFakeEventRepository)
    }

    @Test
    fun `state is Initially Loading`() = runTest {
        val uiState = successViewModel.uiState.value
        assertIs<Loading>(uiState)
    }

    @Test
    fun `event and advertisement are loaded successfuly`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { successViewModel.uiState.collect() }
        val uiState = successViewModel.uiState.value
        assertIs<Success>(uiState)
        collectJob.cancel()
    }

    @Test
    fun `when the repository throws an exception then state is Error`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { failingViewModel.uiState.collect() }
        val uiState = failingViewModel.uiState.value
        assertIs<Error>(uiState)
        collectJob.cancel()
    }

}