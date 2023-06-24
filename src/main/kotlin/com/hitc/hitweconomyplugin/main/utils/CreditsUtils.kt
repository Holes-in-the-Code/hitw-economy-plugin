package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.EPlayer
import com.hitc.hitweconomyplugin.main.core.GameType
import com.hitc.hitweconomyplugin.main.core.Score

object CreditsUtils {

    fun getScores(gameType: GameType, score: Int, ePlayer: EPlayer) : List<Score?> {
        val scores = ePlayer.dailyScores.getScores()
        val filteredScores = scores.filter { it.gameType == gameType }

        val scoreList = filteredScores.toMutableList()
        scoreList.add(Score(gameType, score))
        return scoreList.toList()
    }

    fun addCredits(scores : List<Score?>, ePlayer: EPlayer) : Int {
        val credits = creditsAlgorithm1(scores)
        ePlayer.playerData.addCredits(credits)
        ePlayer.playerData.addCreditsEarned(credits)
        return credits
    }

    private fun creditsAlgorithm1(scores : List<Score?>) : Int {
        val score = scores.last()?.score ?: 0
        val games = scores.size
        return if (games <= 10) score else score/10
    }

    fun gamesPlayedStringAlgorithm1(scores : List<Score?>) : String {
        val games = scores.size
        val color = if (games <= 10) "§a" else "§c"
        val gameTypeString = scores[0]?.gameType?.text ?: ""
        return "$color$games§a/10 $gameTypeString Games"
    }



}