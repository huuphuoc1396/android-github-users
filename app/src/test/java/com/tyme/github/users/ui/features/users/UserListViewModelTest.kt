package com.tyme.github.users.ui.features.users

import com.tyme.github.users.providers.dispatchers.DispatchersProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before

@ExperimentalCoroutinesApi
internal class UserListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val dispatchersProvider: DispatchersProvider = mockk()

    private lateinit var userListViewModel: UserListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { dispatchersProvider.io } returns testDispatcher

        userListViewModel = UserListViewModel(dispatchersProvider)
    }

}