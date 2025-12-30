package com.fjjukic.zenvio.feature.sleep.di

import com.fjjukic.zenvio.feature.sleep.domain.SleepContextManager
import com.fjjukic.zenvio.feature.sleep.domain.SleepContextManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SleepModule {

    @Binds
    @Singleton
    abstract fun bindSleepContextManager(
        impl: SleepContextManagerImpl
    ): SleepContextManager
}
