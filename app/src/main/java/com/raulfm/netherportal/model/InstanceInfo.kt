package com.raulfm.netherportal.model

import com.raulfm.netherportal.model.enums.InstanceStatus

data class InstanceInfo(
    val endpoint: String,
    val ip: String,
    val status: InstanceStatus,
    val launch_time: String
)