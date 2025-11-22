package com.fjjukic.zenvio.feature.onboarding.di

import com.fjjukic.zenvio.feature.onboarding.data.repository.OnboardingRepository
import com.fjjukic.zenvio.feature.onboarding.data.repository.OnboardingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingModule {

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        impl: OnboardingRepositoryImpl
    ): OnboardingRepository
}