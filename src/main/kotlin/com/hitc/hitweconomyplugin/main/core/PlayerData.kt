package com.hitc.hitweconomyplugin.main.core

import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(
    var credits : Int,
    var creditsEarned : Int
)
