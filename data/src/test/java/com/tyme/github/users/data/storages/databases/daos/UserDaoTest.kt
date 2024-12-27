package com.tyme.github.users.data.storages.databases.daos

import android.content.Context
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.tyme.github.users.data.storages.databases.UserDatabase
import com.tyme.github.users.data.storages.databases.entities.UserEntity
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
internal class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: UserDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun upsertAll() = runTest {
        // Given
        val entities = MutableList(20) { index -> UserEntity(id = index, username = "user$index") }

        // When
        userDao.upsertAll(entities)

        // Then
        val result = getPageResult()
        result.data shouldBe entities
    }

    @Test
    fun deleteAll() = runTest {
        // Given
        val entities = MutableList(20) { index -> UserEntity(id = index, username = "user$index") }
        userDao.upsertAll(entities)

        // When
        userDao.deleteAll()

        // Then
        val result = getPageResult()
        result.data shouldBe emptyList()
    }

    @Test
    fun `upsertAndDeleteAll with needToDelete true`() = runTest {
        // Given
        val entities = MutableList(20) { index -> UserEntity(id = index, username = "user$index") }
        userDao.upsertAll(entities)

        // When
        userDao.upsertAndDeleteAll(needToDelete = true, entities)

        // Then
        val result = getPageResult()
        result.data shouldBe entities
    }

    @Test
    fun `upsertAndDeleteAll with needToDelete false`() = runTest {
        // Given
        val firstEntities = MutableList(20) { index -> UserEntity(id = index, username = "user$index") }
        val secondEntities = MutableList(20) { index -> UserEntity(id = index + 20, username = "user$index") }
        userDao.upsertAll(firstEntities)

        // When
        userDao.upsertAndDeleteAll(needToDelete = false, secondEntities)

        // Then
        val result = getPageResult()
        result.data shouldBe firstEntities + secondEntities
    }

    private suspend fun getPageResult(): PagingSource.LoadResult.Page<Int, UserEntity> {
        val pager = TestPager(
            config = PagingConfig(20),
            pagingSource = userDao.getPagingSource()
        )
        val result = pager.refresh() as PagingSource.LoadResult.Page<Int, UserEntity>
        return result
    }
}