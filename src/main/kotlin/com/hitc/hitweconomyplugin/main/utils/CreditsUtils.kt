package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.EPlayer
import com.hitc.hitweconomyplugin.main.core.GameType
import com.hitc.hitweconomyplugin.main.core.Score

object CreditsUtils {

    fun addCredits(gameType: GameType, score: Int, ePlayer: EPlayer) : Int {
        val scores = ePlayer.dailyScores.getScores()
        val filteredScores = scores.filter { it.gameType == gameType }

        val credits = creditsAlgorithm1(filteredScores, score)
        ePlayer.playerData.addCredits(credits)
        ePlayer.playerData.addCreditsEarned(credits)
        return credits
    }

    private fun creditsAlgorithm1(filteredScores : List<Score?>, score : Int) : Int {
        val amount = filteredScores.size
        return if (amount <= 10) score else score/10
    }


}