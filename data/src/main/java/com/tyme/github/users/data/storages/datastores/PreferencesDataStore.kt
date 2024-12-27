package com.tyme.github.users.data.storages.datastores

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.osipxd.security.crypto.encryptedPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val FILE_NAME = "github_users.preferences_pb"
private val Context.preferences by encryptedPreferencesDataStore(FILE_NAME)

internal interface PreferencesDataStore {

    suspend fun setLastUpdatedUserList(lastUpdated: Long)

    fun getLastUpdatedUserList(): Flow<Long>
}

internal class PreferencesDataStoreImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : PreferencesDataStore {

    override suspend fun setLastUpdatedUserList(lastUpdated: Long) {
        context.preferences.edit { preferences ->
            preferences[KEY_LAST_UPDATED_USER_LIST] = lastUpdated
        }
    }

    override fun getLastUpdatedUserList(): Flow<Long> {
        return context.preferences.data.map { preferences ->
            preferences[KEY_LAST_UPDATED_USER_LIST] ?: 0L
        }
    }

    companion object {
        private val KEY_LAST_UPDATED_USER_LIST = longPreferencesKey("last_updated_user_list")
    }
}