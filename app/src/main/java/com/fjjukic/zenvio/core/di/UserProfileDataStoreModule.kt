package com.fjjukic.zenvio.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.fjjukic.zenvio.core.data.profile.UserProfileSerializer
import com.fjjukic.zenvio.core.datastore.UserProfileProto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserProfileDataStoreModule {

    @Provides
    @Singleton
    fun provideUserProfileDataStore(
        @ApplicationContext context: Context
    ): DataStore<UserProfileProto> {
        return DataStoreFactory.create(
            serializer = UserProfileSerializer
        ) {
            context.dataStoreFile("user_profile.pb")
        }
    }
}
