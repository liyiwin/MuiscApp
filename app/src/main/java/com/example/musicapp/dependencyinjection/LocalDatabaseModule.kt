package com.example.musicapp.dependencyinjection

import android.content.Context
import com.example.musicapp.localDatase.ISaveAppSettingInApp
import com.example.musicapp.localDatase.ISaveUserInfoInApp
import com.example.musicapp.localDatase.SaveAppSettingInApp
import com.example.musicapp.localDatase.SaveUserInfoInApp
import com.example.musicapp.localDatase.sqLite.FavoriteTracksStorage
import com.example.musicapp.localDatase.sqLite.IFavoriteTracksStorage
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

    @Singleton
    @Provides
    fun ProvideFavoriteTracksStorage(@ApplicationContext context: Context): IFavoriteTracksStorage = FavoriteTracksStorage(context)


}
