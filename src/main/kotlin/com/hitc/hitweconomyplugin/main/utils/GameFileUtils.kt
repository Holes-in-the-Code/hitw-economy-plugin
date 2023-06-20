package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.bukkit.entity.Player
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.Exception

object GameFileUtils {

    fun loadScores(f: File) : Scores {
        var scores = Scores(emptyList<Score>().toMutableList(), f)

        val text = f.readText()
        if (text.isNotBlank()) {
            val dataJson = Json.decodeFromString<DataFile>(text)
            // TODO make a system that maps dataVersions to dataFixers
            if (dataJson.dataVersion == Scores.DATA_VERSION) {
                scores = Json.decodeFromJsonElement<Scores>(dataJson.data)
            }
            else {
                throw Exception("Invalid data version")
            }
        }
        return scores
    }

    fun loadPlayerData(f : File) : PlayerData {
        val text = f.readText()
        var playerData = PlayerData(0,0, f)
        if (text.isNotBlank()) {
            val dataJson = Json.decodeFromString<DataFile>(text)
            // TODO make a system that maps dataVersions to dataFixers
            if (dataJson.dataVersion == PlayerData.DATA_VERSION) {
                playerData = Json.decodeFromJsonElement<PlayerData>(dataJson.data)
            }
            else {
                throw Exception("Invalid data version")
            }
        }
        return playerData
    }

    fun initPlayerData(player : Player) : PlayerData {
        val stringUUID = player.uniqueId.toString()

        val f = File("./plugins/HitW/playerdata/$stringUUID.json")
        if (!f.exists()) {
            Files.createDirectories(f.parentFile.toPath())
            f.createNewFile()
        }
        return loadPlayerData(f)
    }

    fun initPlayerScores(player : Player) : Scores {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val stringUUID = player.uniqueId.toString()

        val f = File("./plugins/HitW/playerdata/$day-$month/$stringUUID.json")
        if (!f.exists()) {
            Files.createDirectories(f.parentFile.toPath())
            f.createNewFile()
        }
        val scores = loadScores(f)
        scores.date = calendar
        return scores
    }

}