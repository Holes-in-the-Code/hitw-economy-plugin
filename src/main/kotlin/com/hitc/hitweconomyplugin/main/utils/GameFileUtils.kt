package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.EPlayer
import com.hitc.hitweconomyplugin.main.core.Score
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.entity.Player
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.util.*

object GameFileUtils {

    fun loadScores(ePlayer: EPlayer, calendar: Calendar) : MutableList<Score?> {
        var scores : MutableList<Score?> = MutableList(0) { null }

        var f = ePlayer.dailyDataFile
        if (calendar.get(Calendar.DAY_OF_YEAR) != f.second.get(Calendar.DAY_OF_YEAR)) {
            f = getDailyDataFile(ePlayer.player.player)
            ePlayer.dailyDataFile = f
        }

        val text = f.first.readText()
        if (text.isNotBlank()) try {
            scores = Json.decodeFromString<MutableList<Score?>>(text)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return scores
    }

    private fun saveScores(ePlayer: EPlayer, scores : MutableList<Score?>, calendar: Calendar) {
        var f = ePlayer.dailyDataFile
        if (calendar.get(Calendar.DAY_OF_YEAR) != f.second.get(Calendar.DAY_OF_YEAR)) {
            f = getDailyDataFile(ePlayer.player.player)
            ePlayer.dailyDataFile = f
        }

        val scoresText = Json.encodeToString(scores)
        f.first.writeText(scoresText)
    }

    fun appendScore(ePlayer: EPlayer, score : Score) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
        val currentScores = loadScores(ePlayer, calendar)
        currentScores.add(score)
        saveScores(ePlayer, currentScores, calendar)
    }

    fun getDailyDataFile(player: Player) : Pair<File, Calendar> {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val stringUUID = player.uniqueId.toString()

        val f = File("./plugins/HitW/playerdata/$day-$month/$stringUUID.json")
        if (!f.exists()) {
            Files.createDirectories(f.parentFile.toPath())
            f.createNewFile()
        }
        return Pair<File, Calendar>(f, calendar)
    }

    fun getDataFile(player: Player) : File {
        val stringUUID = player.uniqueId.toString()

        val f = File("./plugins/HitW/playerdata/$stringUUID.json")
        if (!f.exists()) {
            Files.createDirectories(f.parentFile.toPath())
            f.createNewFile()
        }
        return f
    }

}