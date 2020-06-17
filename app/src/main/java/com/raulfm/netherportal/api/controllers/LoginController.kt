package com.raulfm.netherportal.api.controllers

import com.raulfm.netherportal.api.RetrofitConfig
import com.raulfm.netherportal.backend.TokenManager
import com.raulfm.netherportal.model.Token
import com.raulfm.netherportal.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginController {
    @JvmStatic
    fun login(
        user: User,
        onSuccess: () -> Unit,
        onBadCredentials: () -> Unit,
        onError: () -> Unit
    ) {
        val call = RetrofitConfig().loginService().login(user)

        call.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                if (response != null) {
                    when {
                        response.isSuccessful -> {
                            TokenManager.token = response.body()
                            onSuccess()
                        }
                        response.code() == 403 -> onBadCredentials()
                        else -> onError()
                    }
                } else
                    onError()
            }

            override fun onFailure(call: Call<Token>?, t: Throwable?) {
                onError()
            }
        })
    }
}