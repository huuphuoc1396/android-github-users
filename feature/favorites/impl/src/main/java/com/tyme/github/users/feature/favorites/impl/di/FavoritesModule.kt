package com.tyme.github.users.feature.favorites.impl.di

import android.content.Context
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import com.tyme.github.users.feature.favorites.impl.data.local.FavoriteDao
import com.tyme.github.users.feature.favorites.impl.data.local.FavoritesDatabase
import com.tyme.github.users.feature.favorites.impl.data.repository.FavoriteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FavoritesModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    companion object {

        @Provides
        @Singleton
        fun provideFavoritesDatabase(
            @ApplicationContext context: Context,
        ): FavoritesDatabase = FavoritesDatabase.create(context)

        @Provides
        @Singleton
        fun provideFavoriteDao(db: FavoritesDatabase): FavoriteDao = db.favoriteDao()
    }
}
