package com.hitc.hitweconomyplugin.main.core

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Score(
    var gameType : GameType,
    var score : Int
)
