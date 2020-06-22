package com.raulfm.netherportal.model

data class Players(
    val online: Int,
    val max: Int,
    val list: List<String>
)