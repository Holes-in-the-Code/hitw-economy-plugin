package com.hitc.hitweconomyplugin.main.utils

import com.hitc.hitweconomyplugin.main.core.DataFile
import com.hitc.hitweconomyplugin.main.core.MonthlyPlayerData
import com.hitc.hitweconomyplugin.main.core.PlayerData
import com.hitc.hitweconomyplugin.main.core.Scores
import com.hitc.hitweconomyplugin.main.datafixers.DataFixers
import com.hitc.hitweconomyplugin.main.datafixers.PlayerDataFixer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.bukkit.entity.Player
import java.io.File
import java.nio.file.Files
import java.util.*

object GameFileUtils {

    fun loadScores(f: File) : Scores {
        var scores = Scores(file = f)

        val text = f.readText()
        if (text.isNotBlank()) {
            // TODO make a system that maps dataVersions to dataFixers
            val dataJson = Json.decodeFromString<DataFile>(text)
            if (dataJson.dataVersion == Scores.DATA_VERSION) {
                scores = Json.decodeFromJsonElement(dataJson.data)
            }
            else {
                throw Exception("Invalid data version")
            }
        }
        scores.file = f
        return scores
    }

    fun loadPlayerData(f : File) : PlayerData {
        val text = f.readText()
        println(text)
        var playerData = PlayerData(file = f)
        if (text.isNotBlank()) {
            var dataJson = Json.decodeFromString<DataFile>(text)
            while (dataJson.dataVersion != PlayerData.DATA_VERSION) {
                for (fixer in DataFixers.fixers) {
                    if (((fixer) as PlayerDataFixer).getTargetVersion() == dataJson.dataVersion) {
                        dataJson = fixer.fixData(f)
                        continue
                    }
                }
                if (dataJson.dataVersion == PlayerData.DATA_VERSION) {
                    playerData = Json.decodeFromJsonElement(dataJson.data)
                    playerData.file = f
                    playerData.save()
                    break
                }
                throw Exception("Invalid data version")
            }
            playerData = Json.decodeFromJsonElement(dataJson.data)
            playerData.file = f
        }
        return playerData
    }

    fun loadMonthlyData(f : File, month : Int) : MonthlyPlayerData {
        val text = f.readText()
        var monthlyData = MonthlyPlayerData(file = f, month = month)
        if (text.isNotBlank()) {
            val dataJson = Json.decodeFromString<DataFile>(text)
            // TODO make a system that maps dataVersions to dataFixers
            if (dataJson.dataVersion == MonthlyPlayerData.DATA_VERSION) {
                monthlyData = Json.decodeFromJsonElement(dataJson.data)
            }
            else {
                throw Exception("Invalid data version")
            }
        }
        monthlyData.file = f
        return monthlyData
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

    fun initMonthlyData(player : Player) : MonthlyPlayerData {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
        val month = calendar.get(Calendar.MONTH)

        val stringUUID = player.uniqueId.toString()

        val f = File("./plugins/HitW/playerdata/monthly/$stringUUID.json")
        if (!f.exists()) {
            Files.createDirectories(f.parentFile.toPath())
            f.createNewFile()
        }
        val monthlyPlayerData = loadMonthlyData(f, month)
        if (monthlyPlayerData.month != month) {
            f.delete()
            f.createNewFile()
            loadMonthlyData(f, month)
        }
        return monthlyPlayerData
    }

}