package com.example.musicapp.dependencyinjection

import com.example.musicapp.data.dao.GetRequestInterface
import com.example.musicapp.data.dao.LoginRequestInterface
import com.example.musicapp.data.repository.FetchDataRepository
import com.example.musicapp.data.repository.IFetchDataRepository
import com.example.musicapp.data.repository.ILoginRepository
import com.example.musicapp.data.repository.LoginRepository
import com.example.musicapp.localDatase.ISaveUserInfoInApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideFetchDataRepository(saveUserInfoInApp: ISaveUserInfoInApp, getRequestInterface: GetRequestInterface): IFetchDataRepository {
          return FetchDataRepository(saveUserInfoInApp,getRequestInterface)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(saveUserInfoInApp: ISaveUserInfoInApp, loginRequestInterface: LoginRequestInterface):ILoginRepository{
         return LoginRepository(saveUserInfoInApp,loginRequestInterface)
    }
}