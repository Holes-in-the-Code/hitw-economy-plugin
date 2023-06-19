package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.EPlayer
import com.hitc.hitweconomyplugin.main.core.GameType
import com.hitc.hitweconomyplugin.main.core.Score

object CreditsUtils {

    fun addCredits(gameType: GameType, score: Int, ePlayer: EPlayer) : Int {
        val scores = GameFileUtils.loadScores(ePlayer)
        val filteredScores = MutableList<Score?>(0) { null }
        for (currentScore in scores) {
            if (currentScore?.gameType == gameType) {
                filteredScores.add(currentScore)
            }
        }
        val credits = creditsAlgorithm1(filteredScores, score)
        GameFileUtils.addCredits(ePlayer, credits)
        return credits
    }

    private fun creditsAlgorithm1(filteredScores : MutableList<Score?>, score : Int) : Int {
        val amount = filteredScores.size
        return if (amount <= 10) score else score/10
    }


}