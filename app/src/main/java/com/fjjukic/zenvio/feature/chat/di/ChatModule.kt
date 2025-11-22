package com.fjjukic.zenvio.feature.chat.di

import com.fjjukic.zenvio.feature.chat.data.repository.ChatRepository
import com.fjjukic.zenvio.feature.chat.data.repository.ChatRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository
}