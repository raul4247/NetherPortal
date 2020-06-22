package com.raulfm.netherportal.backend.api.services

import com.raulfm.netherportal.model.ServerData
import com.raulfm.netherportal.model.Token
import retrofit2.Call
import retrofit2.http.*

interface ServerService {
    @GET("/server/status")
    fun getServerStatus(@Header("Authorization") token: String): Call<ServerData>

    @POST("/server/{action}")
    fun sendServerAction(@Header("Authorization") token: String, @Path("action") action: String): Call<Unit>
}
