package com.example.musicapp.dependencyinjection

import android.content.Context
import com.example.musicapp.localDataase.ISaveAppSettingInApp
import com.example.musicapp.localDataase.ISaveUserInfoInApp
import com.example.musicapp.localDataase.SaveAppSettingInApp
import com.example.musicapp.localDataase.SaveUserInfoInApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {

    @Singleton
    @Provides
    fun ProvideSaveAppSettingInApp(@ApplicationContext context: Context): ISaveAppSettingInApp = SaveAppSettingInApp(context);
    @Singleton
    @Provides
    fun ProvideSaveUserInfoInApp(@ApplicationContext context: Context):ISaveUserInfoInApp = SaveUserInfoInApp(context);


}