package com.raulfm.netherportal.model

data class MinecraftInfo(
    val online: Boolean,
    val port: Int,
    val players: Players?,
    val version: String?
)
