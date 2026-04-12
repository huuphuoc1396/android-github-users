package com.example.github.users.feature.favorites.di

import com.example.github.users.feature.favorites.data.repository.FavoriteRepositoryImpl
import com.example.github.users.feature.favorites.domain.repository.FavoriteRepository
import com.example.github.users.feature.favorites.domain.usecase.AddFavoriteUseCase
import com.example.github.users.feature.favorites.domain.usecase.AddFavoriteUseCaseImpl
import com.example.github.users.feature.favorites.domain.usecase.GetFavoritesUseCase
import com.example.github.users.feature.favorites.domain.usecase.GetFavoritesUseCaseImpl
import com.example.github.users.feature.favorites.domain.usecase.IsFavoriteUseCase
import com.example.github.users.feature.favorites.domain.usecase.IsFavoriteUseCaseImpl
import com.example.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.example.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FavoritesModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    abstract fun bindAddFavoriteUseCase(impl: AddFavoriteUseCaseImpl): AddFavoriteUseCase

    @Binds
    abstract fun bindRemoveFavoriteUseCase(impl: RemoveFavoriteUseCaseImpl): RemoveFavoriteUseCase

    @Binds
    abstract fun bindGetFavoritesUseCase(impl: GetFavoritesUseCaseImpl): GetFavoritesUseCase

    @Binds
    abstract fun bindIsFavoriteUseCase(impl: IsFavoriteUseCaseImpl): IsFavoriteUseCase
}
