package com.raulfm.netherportal.backend.api.services

import com.raulfm.netherportal.model.Token
import com.raulfm.netherportal.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/login")
    fun login(@Body user:User): Call<Token>
}
