package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.EPlayer
import com.hitc.hitweconomyplugin.main.core.GameType
import com.hitc.hitweconomyplugin.main.core.Score
import kotlin.math.pow
import kotlin.math.roundToInt

object CreditsUtils {

    fun getScores(gameType: GameType, score: Int, ePlayer: EPlayer) : List<Score?> {
        val scores = ePlayer.dailyScores.getScores()
        val filteredScores = scores.filter { it.gameType == gameType }

        val scoreList = filteredScores.toMutableList()
        scoreList.add(Score(gameType, score))
        return scoreList.toList()
    }

    fun addCredits(scores : List<Score?>, ePlayer: EPlayer) : Int {
        val credits = creditsAlgorithm3(scores)
        ePlayer.playerData.addCredits(credits)
        ePlayer.playerData.addCreditsEarned(credits)
        ePlayer.monthlyData.addCreditsEarned(credits, ePlayer.player)
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

    private fun creditsAlgorithm2(scores : List<Score?>) : Int {
        val score = scores.last()?.score ?: 0
        val games = scores.size
        val credits = when {
            games <= 10 -> score
            games <= 20 -> score/10
            else -> 0
        }
        return credits
    }

    fun gamesPlayedStringAlgorithm2(scores : List<Score?>) : String {
        val games = scores.size
        val color = when {
            games < 10 -> "§a"
            games < 20 -> "§e"
            else -> "§c"
        }
        val gameTypeString = scores[0]?.gameType?.text ?: ""
        val amount = when {
            games < 10 -> "10"
            else -> "20"
        }

        return "$color$games/$amount §a$gameTypeString Games"
    }

    private fun creditsAlgorithm3Multiplier(amount: Int): Double {
        val curve1 = 1 / ((2.0).pow((2.0 * amount - 11.0)) + 1) // sigmoid with steep slope
        val curve2 = 1 / ((2.0).pow((0.3 * amount - 0.9)) + 1) // sigmoid with shallow slope
        return 0.95 * maxOf(curve1, curve2) + 0.05 // interpolates between curves
    }

    private fun creditsAlgorithm3(scores: List<Score?>): Int {
        val score = scores.last()?.score ?: 0
        val games = scores.size
        val multiplier = (creditsAlgorithm3Multiplier(games)*100.0).roundToInt()
        return (score * (multiplier/100.0)).roundToInt()
    }

    fun gamesPlayedStringAlgorithm3(scores : List<Score?>) : String {
        val games = scores.size
        val multiplier = creditsAlgorithm3Multiplier(games)
        val percent = (multiplier*100.0).roundToInt()
        val color = when {
            multiplier > 0.5 -> "§a"
            multiplier > 0.25 -> "§e"
            else -> "§c"
        }
        val gameTypeString = scores[0]?.gameType?.text ?: ""

        return "$color$games §a$gameTypeString Games Played, $color$percent%"
    }


}