package com.tyme.github.users.feature.favorites.impl.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyme.github.users.feature.favorites.api.model.FavoriteUser
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import com.tyme.github.users.feature.favorites.impl.domain.usecase.GetFavoritesUseCase
import com.tyme.github.users.feature.favorites.impl.domain.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = getFavoritesUseCase()
        .map { favorites ->
            if (favorites.isEmpty()) FavoritesUiState.Empty
            else FavoritesUiState.Success(favorites)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState.Loading,
        )

    fun onRemoveFavorite(user: FavoriteUser) {
        viewModelScope.launch {
            removeFavoriteUseCase(user.username)
        }
    }
}
