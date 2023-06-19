package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.DataFile
import com.hitc.hitweconomyplugin.main.core.EPlayer
import com.hitc.hitweconomyplugin.main.core.PlayerData
import com.hitc.hitweconomyplugin.main.core.Score
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.bukkit.entity.Player
import java.io.File
import java.nio.file.Files
import java.util.*
import java.util.zip.DataFormatException
import kotlin.Exception

object GameFileUtils {

    fun loadScores(ePlayer: EPlayer,
                   calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
    ) : MutableList<Score?> {
        var scores = MutableList<Score?>(0) { null }

        var f = ePlayer.dailyDataFile
        if (calendar.get(Calendar.DAY_OF_YEAR) != f.second.get(Calendar.DAY_OF_YEAR)) {
            f = getDailyDataFile(ePlayer.player.player)
            ePlayer.dailyDataFile = f
        }

        val text = f.first.readText()
        if (text.isNotBlank()) {
            val dataJson = Json.decodeFromString<DataFile>(text)
            // TODO make a system that maps dataVersions to dataFixers
            if (dataJson.dataVersion == 0) {
                scores = Json.decodeFromJsonElement<MutableList<Score?>>(dataJson.data)
            }
            else {
                throw Exception("Invalid data version")
            }
        }
        return scores
    }

    fun loadPlayerData(ePlayer: EPlayer) : PlayerData {
        val f = ePlayer.dataFile
        val text = f.readText()
        var playerData = PlayerData(0,0)
        if (text.isNotBlank()) {
            val dataJson = Json.decodeFromString<DataFile>(text)
            // TODO make a system that maps dataVersions to dataFixers
            if (dataJson.dataVersion == 0) {
                playerData = Json.decodeFromJsonElement<PlayerData>(dataJson.data)
            }
            else {
                throw Exception("Invalid data version")
            }
        }
        return playerData
    }

    fun savePlayerData(ePlayer: EPlayer, playerData: PlayerData) {
        val f = ePlayer.dataFile
        val playerDataElement = Json.encodeToJsonElement(playerData)
        val dataJson = DataFile(0, playerDataElement)

        f.writeText(Json.encodeToString(dataJson))
    }

    fun addCredits(ePlayer: EPlayer, credits : Int) {
        val playerData : PlayerData
        try {
             playerData = loadPlayerData(ePlayer)
        } catch (e : Exception) {
            e.printStackTrace()
            ePlayer.player.player.sendMessage("§cThere was an error in saving this game, report this to staff.")
            return
        }
        playerData.credits += credits
        playerData.creditsEarned += credits
        savePlayerData(ePlayer, playerData)
    }

    private fun saveScores(ePlayer: EPlayer, scores : MutableList<Score?>, calendar: Calendar) {
        var f = ePlayer.dailyDataFile
        if (calendar.get(Calendar.DAY_OF_YEAR) != f.second.get(Calendar.DAY_OF_YEAR)) {
            f = getDailyDataFile(ePlayer.player.player)
            ePlayer.dailyDataFile = f
        }
        val scoresElement = Json.encodeToJsonElement(scores)
        val dataJson = DataFile(0, scoresElement)
        f.first.writeText(Json.encodeToString(dataJson))
    }

    fun appendScore(ePlayer: EPlayer, score : Score) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
        val currentScores : MutableList<Score?>
        try {
             currentScores = loadScores(ePlayer, calendar)
        } catch (e : Exception) {
            e.printStackTrace()
            ePlayer.player.player.sendMessage("§cThere was an error in saving this game, report this to staff.")
            return
        }
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