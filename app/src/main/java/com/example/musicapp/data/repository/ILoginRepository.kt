package com.example.musicapp.data.repository

import com.example.musicapp.bean.remote.LoginResult
import com.example.musicapp.bean.remote.ReLoginResult
import com.example.musicapp.data.requestResult.RequestResultWithData

interface ILoginRepository {
    suspend fun login(code:String) : RequestResultWithData<LoginResult>
    suspend fun reLogin() : RequestResultWithData<ReLoginResult>
}