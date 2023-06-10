package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.Score
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.Exception
import java.util.*

object GameFileUtils {

    fun loadScores(uuid : UUID) : MutableList<Score?> {
        var scores : MutableList<Score?> = MutableList(0) { null }
        val stringUUID = uuid.toString()
        val f = File("./plugins/HitW/scores/$stringUUID.json")
        if (!f.exists()) f.createNewFile()
        val text = f.readText()
        if (text.isNotBlank()) try {
            scores = Json.decodeFromString<MutableList<Score?>>(f.readText())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return scores
    }

    private fun saveScores(uuid : UUID, scores : MutableList<Score?>) {
        val stringUUID = uuid.toString()
        val f = File("./plugins/HitW/scores/$stringUUID.json")
        if (!f.exists()) f.createNewFile()
        val scoresText = Json.encodeToString(scores)
        f.writeText(scoresText)
    }

    fun appendScore(uuid : UUID, score : Score) {
        val currentScores = loadScores(uuid)
        currentScores.add(score)
        saveScores(uuid, currentScores)
    }

}