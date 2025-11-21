package com.fjjukic.zenvio.di

import com.fjjukic.zenvio.core.data.repository.OnboardingRepository
import com.fjjukic.zenvio.core.data.repository.OnboardingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {

    @Provides
    @Singleton
    fun provideOnboardingRepository(): OnboardingRepository = OnboardingRepositoryImpl()
}