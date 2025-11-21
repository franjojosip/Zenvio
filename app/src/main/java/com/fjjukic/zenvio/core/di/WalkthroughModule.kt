package com.fjjukic.zenvio.core.di

import com.fjjukic.zenvio.feature.walkthrough.data.WalkthroughRepository
import com.fjjukic.zenvio.feature.walkthrough.data.WalkthroughRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WalkthroughModule {

    @Binds
    @Singleton
    abstract fun bindWalkthroughRepository(
        impl: WalkthroughRepositoryImpl
    ): WalkthroughRepository
}