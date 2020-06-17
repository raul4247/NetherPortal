package com.raulfm.netherportal.api

import com.raulfm.netherportal.api.services.LoginService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig {
    private val retrofit: Retrofit
    private val baseUrl = "http://25.4.28.237:3001"

    constructor() {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun loginService(): LoginService = retrofit.create(LoginService::class.java)
}