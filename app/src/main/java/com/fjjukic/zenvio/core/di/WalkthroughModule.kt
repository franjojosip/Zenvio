package com.fjjukic.zenvio.core.di

import com.fjjukic.zenvio.core.data.repository.WalkthroughRepository
import com.fjjukic.zenvio.core.data.repository.WalkthroughRepositoryImpl
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