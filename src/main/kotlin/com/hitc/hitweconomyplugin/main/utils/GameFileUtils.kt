package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.Score
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.util.*

object GameFileUtils {

    fun loadScores(uuid : UUID, calendar: Calendar) : MutableList<Score?> {
        val stringUUID = uuid.toString()
        var scores : MutableList<Score?> = MutableList(0) { null }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)

        val f = File("./plugins/HitW/playerdata/$day-$month/$stringUUID.json")
        if (!f.exists()) {
            Files.createDirectories(f.parentFile.toPath())
            f.createNewFile()
        }
        val text = f.readText()
        if (text.isNotBlank()) try {
            scores = Json.decodeFromString<MutableList<Score?>>(f.readText())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return scores
    }

    private fun saveScores(uuid : UUID, scores : MutableList<Score?>, calendar : Calendar) {
        val stringUUID = uuid.toString()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)

        val f = File("./plugins/HitW/playerdata/$day-$month/$stringUUID.json")
        if (!f.exists()) {
            Files.createDirectories(f.parentFile.toPath())
            f.createNewFile()
        }
        val scoresText = Json.encodeToString(scores)
        f.writeText(scoresText)
    }

    fun appendScore(uuid : UUID, score : Score) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
        val currentScores = loadScores(uuid, calendar)
        currentScores.add(score)
        saveScores(uuid, currentScores, calendar)
    }

}