package com.tyme.github.users.feature.users.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.tyme.github.users.core.common.extensions.orZero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal interface UserPreferencesDataStore {

    suspend fun setLastUpdatedUserList(lastUpdated: Long)

    fun getLastUpdatedUserList(): Flow<Long>
}

internal class UserPreferencesDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesDataStore {

    override suspend fun setLastUpdatedUserList(lastUpdated: Long) {
        dataStore.edit { preferences -> preferences[KEY_LAST_UPDATED_USER_LIST] = lastUpdated }
    }

    override fun getLastUpdatedUserList(): Flow<Long> {
        return dataStore.data.map { preferences -> preferences[KEY_LAST_UPDATED_USER_LIST].orZero() }
    }

    companion object {
        private val KEY_LAST_UPDATED_USER_LIST = longPreferencesKey("last_updated_user_list")
    }
}
