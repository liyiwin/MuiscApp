package com.example.musicapp.dependencyinjection

import android.content.Context
import com.example.musicapp.tool.file.IPlayListManager
import com.example.musicapp.tool.file.PlayListManager
import com.example.musicapp.tool.network.INetWorkConnectionChecker
import com.example.musicapp.tool.network.NetWorkConnectionChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ToolModule {

    @Singleton
    @Provides
    fun provideNetWorkConnectionChecker(@ApplicationContext context: Context) : INetWorkConnectionChecker = NetWorkConnectionChecker(context)

    @Singleton
    @Provides
    fun providePlayListManage(@ApplicationContext context: Context):IPlayListManager = PlayListManager(context)
}