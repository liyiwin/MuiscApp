package com.example.musicapp.dependencyinjection

import com.example.musicapp.data.dao.GetRequestInterface
import com.example.musicapp.data.dao.LoginRequestInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideGetRequestInterface():GetRequestInterface{
        return Retrofit.Builder()
            .baseUrl("https://api.kkbox.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GetRequestInterface::class.java);
    }

    @Singleton
    @Provides
    fun provideLoginRequestInterface():LoginRequestInterface{
        return  Retrofit.Builder()
            .baseUrl("https://account.kkbox.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginRequestInterface::class.java)
    }
}