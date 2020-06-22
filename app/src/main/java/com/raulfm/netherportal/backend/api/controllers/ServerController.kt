package com.raulfm.netherportal.backend.api.controllers

import com.raulfm.netherportal.backend.api.RetrofitConfig
import com.raulfm.netherportal.backend.singletons.TokenManager
import com.raulfm.netherportal.model.ServerAction
import com.raulfm.netherportal.model.ServerData
import com.raulfm.netherportal.model.Token
import com.raulfm.netherportal.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ServerController {
    fun getServerStatus(
        onSuccess: (serverData: ServerData?) -> Unit,
        onBadCredentials: () -> Unit,
        onError: () -> Unit
    ) {
        val token = TokenManager.token?.value

        if (token == null) onBadCredentials()

        token?.let { token ->
            val call = RetrofitConfig().serverService().getServerStatus(token)

            call.enqueue(object : Callback<ServerData> {
                override fun onResponse(call: Call<ServerData>?, response: Response<ServerData>?) {
                    if (response != null) {
                        when {
                            response.isSuccessful -> {
                                val serverData = response.body()
                                onSuccess(serverData)
                            }
                            response.code() == 403 -> onBadCredentials()
                            else -> onError()
                        }
                    } else
                        onError()
                }

                override fun onFailure(call: Call<ServerData>?, t: Throwable?) {
                    onError()
                }
            })
        }
    }

    fun sendServerAction(
        actionType: ServerAction,
        onSuccess: () -> Unit,
        onBadCredentials: () -> Unit,
        onError: () -> Unit
    ) {
        val token = TokenManager.token?.value

        if (token == null) onBadCredentials()

        token?.let { token ->
            val call = RetrofitConfig().serverService().sendServerAction(token, actionType.action)

            call.enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                    if (response != null) {
                        when {
                            response.isSuccessful -> {
                                onSuccess()
                            }
                            response.code() == 403 -> onBadCredentials()
                            else -> onError()
                        }
                    } else
                        onError()
                }

                override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                    onError()
                }
            })
        }
    }
}