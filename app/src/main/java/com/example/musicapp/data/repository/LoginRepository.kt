package com.example.musicapp.data.repository

import com.example.musicapp.bean.remote.LoginResult
import com.example.musicapp.bean.remote.ReLoginResult
import com.example.musicapp.data.api.LoginApiRequest
import com.example.musicapp.data.dao.LoginRequestInterface
import com.example.musicapp.data.paramter.ApiParameterManager
import com.example.musicapp.data.requestResult.RequestResultWithData
import com.example.musicapp.localDatase.ISaveUserInfoInApp

class LoginRepository(private val saveUserInfoInApp: ISaveUserInfoInApp, private val loginRequestInterface: LoginRequestInterface) :ILoginRepository{

    private val loginApiRequest = LoginApiRequest(loginRequestInterface);

    override suspend fun login(code: String): RequestResultWithData<LoginResult> = loginApiRequest.requestLogin("authorization_code",code,ApiParameterManager.clientId,ApiParameterManager.clientSecret)

    override suspend fun reLogin(): RequestResultWithData<ReLoginResult> = loginApiRequest.requestReLogin("refresh_token",saveUserInfoInApp.getRefreshToken(),"Basic "+ApiParameterManager.refeshAuth)
}