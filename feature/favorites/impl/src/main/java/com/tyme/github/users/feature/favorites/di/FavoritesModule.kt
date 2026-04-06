package com.tyme.github.users.feature.favorites.di

import com.tyme.github.users.core.database.AppDatabase
import com.tyme.github.users.core.database.dao.FavoriteDao
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import com.tyme.github.users.feature.favorites.data.repository.FavoriteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FavoritesModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
}
