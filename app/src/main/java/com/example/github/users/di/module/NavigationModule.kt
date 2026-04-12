package com.example.github.users.di.module

import com.example.github.users.core.navigation.AppNavigator
import com.example.github.users.navigation.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface NavigationModule {

    @Binds
    @Singleton
    fun bindAppNavigator(impl: AppNavigatorImpl): AppNavigator
}
