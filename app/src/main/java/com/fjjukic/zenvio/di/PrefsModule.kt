package com.fjjukic.zenvio.di

import com.fjjukic.zenvio.core.data.preferences.PrefsManager
import com.fjjukic.zenvio.core.data.preferences.PrefsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PrefsModule {

    @Binds
    @Singleton
    abstract fun bindPrefsManager(
        impl: PrefsManagerImpl
    ): PrefsManager
}
