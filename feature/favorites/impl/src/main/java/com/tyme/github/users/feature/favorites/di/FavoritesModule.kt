package com.tyme.github.users.feature.favorites.di

import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import com.tyme.github.users.feature.favorites.data.repository.FavoriteRepositoryImpl
import com.tyme.github.users.feature.favorites.domain.usecase.AddFavoriteUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.AddFavoriteUseCaseImpl
import com.tyme.github.users.feature.favorites.domain.usecase.GetFavoritesUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.GetFavoritesUseCaseImpl
import com.tyme.github.users.feature.favorites.domain.usecase.IsFavoriteUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.IsFavoriteUseCaseImpl
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCaseImpl
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
