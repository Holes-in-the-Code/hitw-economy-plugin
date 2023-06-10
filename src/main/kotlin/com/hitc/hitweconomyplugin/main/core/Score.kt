package com.hitc.hitweconomyplugin.main.core

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Score(
    var score: Pair<GameType,Int>
)
