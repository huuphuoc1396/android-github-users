package com.tyme.github.users.data.storages.datastores

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PreferencesDataStoreImplTest {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("github_users.preferences_pb")

    private lateinit var preferencesDataStore: PreferencesDataStoreImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        preferencesDataStore = PreferencesDataStoreImpl(context.dataStore)
    }

    @Test
    fun `set and get lastUpdatedUserList`() = runTest {
        // Given
        val lastUpdated = System.currentTimeMillis()

        // When
        preferencesDataStore.setLastUpdatedUserList(lastUpdated)

        // Then
        preferencesDataStore.getLastUpdatedUserList().test {
            expectMostRecentItem() shouldBe lastUpdated
        }
    }
}