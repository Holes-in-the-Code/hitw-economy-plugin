package com.hitc.hitweconomyplugin.main.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class DataFile(
    val dataVersion : Int,
    val data : JsonElement
)
