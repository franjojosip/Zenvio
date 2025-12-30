package com.fjjukic.zenvio.core.di

import androidx.datastore.core.DataStore
import com.fjjukic.zenvio.core.data.profile.UserProfileManager
import com.fjjukic.zenvio.core.datastore.UserProfileProto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserProfileManagerModule {

    @Provides
    @Singleton
    fun provideUserProfileManager(
        dataStore: DataStore<UserProfileProto>
    ): UserProfileManager = UserProfileManager(dataStore)
}
