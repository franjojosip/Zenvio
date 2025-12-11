// Create new file: core/data/di/DataModule.kt

package com.fjjukic.zenvio.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fjjukic.zenvio.core.data.preferences.DataStorePrefsManager
import com.fjjukic.zenvio.core.data.preferences.DataStorePrefsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "zenvio_user_prefs"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindDataStorePrefsManager(
        dataStorePrefsManager: DataStorePrefsManagerImpl
    ): DataStorePrefsManager

    companion object {
        @Provides
        @Singleton
        fun providePreferencesDataStore(
            @ApplicationContext applicationContext: Context
        ): DataStore<Preferences> {
            return applicationContext.dataStore
        }
    }
}