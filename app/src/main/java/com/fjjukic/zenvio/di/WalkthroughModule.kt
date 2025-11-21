package com.fjjukic.zenvio.di

import com.fjjukic.zenvio.core.data.repository.WalkthroughRepository
import com.fjjukic.zenvio.core.data.repository.WalkthroughRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WalkthroughModule {

    @Provides
    @Singleton
    fun provideWalkthroughRepository(): WalkthroughRepository = WalkthroughRepositoryImpl()
}