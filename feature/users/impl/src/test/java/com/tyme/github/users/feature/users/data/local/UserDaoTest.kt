package com.tyme.github.users.feature.users.data.local

import android.content.Context
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
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

    // region upsertAll

    @Test
    fun `upsertAll inserts entities with isFavorite defaulting to false`() = runTest {
        // Given
        val entities = MutableList(20) { index -> UserEntity(id = index, username = "user$index") }

        // When
        userDao.upsertAll(entities)

        // Then
        val result = getPageResult()
        result.data shouldBe entities
    }

    @Test
    fun `upsertAll updates existing entity preserving isFavorite`() = runTest {
        // Given
        val entity = UserEntity(id = 1, username = "user1", isFavorite = true)
        userDao.upsertAll(listOf(entity))

        // When
        userDao.upsertAll(listOf(entity.copy(avatarUrl = "new_avatar")))

        // Then
        userDao.getFavorites().first() shouldBe listOf(entity.copy(avatarUrl = "new_avatar"))
    }

    // endregion

    // region deleteNonFavorites

    @Test
    fun `deleteNonFavorites removes only non-favorite entities`() = runTest {
        // Given
        val nonFavorites = MutableList(5) { index -> UserEntity(id = index, username = "user$index") }
        val favorites = MutableList(3) { index -> UserEntity(id = index + 10, username = "fav$index", isFavorite = true) }
        userDao.upsertAll(nonFavorites + favorites)

        // When
        userDao.deleteNonFavorites()

        // Then
        val result = getPageResult()
        result.data shouldBe favorites
    }

    @Test
    fun `deleteNonFavorites with all non-favorites clears the table`() = runTest {
        // Given
        val entities = MutableList(5) { index -> UserEntity(id = index, username = "user$index") }
        userDao.upsertAll(entities)

        // When
        userDao.deleteNonFavorites()

        // Then
        val result = getPageResult()
        result.data shouldBe emptyList()
    }

    // endregion

    // region setFavorite

    @Test
    fun `setFavorite true marks entity as favorite`() = runTest {
        // Given
        val entity = UserEntity(id = 1, username = "user1")
        userDao.upsertAll(listOf(entity))

        // When
        userDao.setFavorite(username = "user1", isFavorite = true)

        // Then
        userDao.isFavorite("user1").first() shouldBe true
    }

    @Test
    fun `setFavorite false unmarks entity as favorite`() = runTest {
        // Given
        val entity = UserEntity(id = 1, username = "user1", isFavorite = true)
        userDao.upsertAll(listOf(entity))

        // When
        userDao.setFavorite(username = "user1", isFavorite = false)

        // Then
        userDao.isFavorite("user1").first() shouldBe false
    }

    // endregion

    // region isFavorite

    @Test
    fun `isFavorite returns false for non-favorite user`() = runTest {
        // Given
        val entity = UserEntity(id = 1, username = "user1")
        userDao.upsertAll(listOf(entity))

        // When / Then
        userDao.isFavorite("user1").first() shouldBe false
    }

    @Test
    fun `isFavorite returns true for favorite user`() = runTest {
        // Given
        val entity = UserEntity(id = 1, username = "user1", isFavorite = true)
        userDao.upsertAll(listOf(entity))

        // When / Then
        userDao.isFavorite("user1").first() shouldBe true
    }

    @Test
    fun `isFavorite returns false for unknown username`() = runTest {
        // When / Then
        userDao.isFavorite("unknown").first() shouldBe false
    }

    // endregion

    // region getFavorites

    @Test
    fun `getFavorites returns only favorite entities`() = runTest {
        // Given
        val nonFavorites = MutableList(3) { index -> UserEntity(id = index, username = "user$index") }
        val favorites = MutableList(2) { index -> UserEntity(id = index + 10, username = "fav$index", isFavorite = true) }
        userDao.upsertAll(nonFavorites + favorites)

        // When / Then
        userDao.getFavorites().first() shouldBe favorites
    }

    @Test
    fun `getFavorites returns empty list when no favorites`() = runTest {
        // Given
        val entities = MutableList(5) { index -> UserEntity(id = index, username = "user$index") }
        userDao.upsertAll(entities)

        // When / Then
        userDao.getFavorites().first() shouldBe emptyList()
    }

    // endregion

    // region upsertAndDeleteAll

    @Test
    fun `upsertAndDeleteAll with needToDelete true replaces non-favorites`() = runTest {
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
    fun `upsertAndDeleteAll with needToDelete false appends to existing`() = runTest {
        // Given
        val firstEntities = MutableList(20) { index -> UserEntity(id = index, username = "user$index") }
        val secondEntities = MutableList(20) { index -> UserEntity(id = index + 20, username = "user${index + 20}") }
        userDao.upsertAll(firstEntities)

        // When
        userDao.upsertAndDeleteAll(needToDelete = false, secondEntities)

        // Then
        val result = getPageResult()
        result.data shouldBe firstEntities + secondEntities
    }

    @Test
    fun `upsertAndDeleteAll preserves isFavorite for matching entity ids`() = runTest {
        // Given
        val favoriteEntity = UserEntity(id = 1, username = "user1", isFavorite = true)
        userDao.upsertAll(listOf(favoriteEntity))

        val updatedEntity = UserEntity(id = 1, username = "user1", avatarUrl = "new_avatar")

        // When
        userDao.upsertAndDeleteAll(needToDelete = true, listOf(updatedEntity))

        // Then
        userDao.getFavorites().first() shouldBe listOf(updatedEntity.copy(isFavorite = true))
    }

    @Test
    fun `upsertAndDeleteAll with needToDelete true keeps favorites not in new list`() = runTest {
        // Given
        val favoriteEntity = UserEntity(id = 99, username = "fav_user", isFavorite = true)
        val otherEntities = MutableList(5) { index -> UserEntity(id = index, username = "user$index") }
        userDao.upsertAll(listOf(favoriteEntity) + otherEntities)

        val newEntities = MutableList(5) { index -> UserEntity(id = index + 10, username = "new$index") }

        // When
        userDao.upsertAndDeleteAll(needToDelete = true, newEntities)

        // Then — favoriteEntity (id=99) is NOT in newEntities but was a favorite,
        // so it should survive deleteNonFavorites
        userDao.getFavorites().first() shouldBe listOf(favoriteEntity)
    }

    // endregion

    private suspend fun getPageResult(): PagingSource.LoadResult.Page<Int, UserEntity> {
        val pager = TestPager(
            config = PagingConfig(50),
            pagingSource = userDao.getPagingSource(),
        )
        return pager.refresh() as PagingSource.LoadResult.Page<Int, UserEntity>
    }
}
