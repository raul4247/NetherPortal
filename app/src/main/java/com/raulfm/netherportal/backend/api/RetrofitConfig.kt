package com.raulfm.netherportal.backend.api

import com.raulfm.netherportal.backend.api.services.LoginService
import com.raulfm.netherportal.backend.api.services.ServerService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig {
    private val retrofit: Retrofit
    private val baseUrl = "https://nether-portal-api.herokuapp.com"

    constructor() {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun loginService(): LoginService = retrofit.create(LoginService::class.java)
    fun serverService(): ServerService = retrofit.create(ServerService::class.java)
}