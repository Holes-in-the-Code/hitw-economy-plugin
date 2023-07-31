package com.hitc.hitweconomyplugin.main.datafixers

import com.hitc.hitweconomyplugin.main.core.DataFile
import com.hitc.hitweconomyplugin.main.core.MonthlyPlayerData
import com.hitc.hitweconomyplugin.main.core.PlayerData
import com.hitc.hitweconomyplugin.main.core.Scores
import com.hitc.hitweconomyplugin.main.utils.CreditsUtils
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.io.path.name

object MonthlyDatafixer : PlayerDataFixer {
    override fun getTargetVersion(): Int {
        return 1
    }

    override fun fixData(f : File) : DataFile {
        val dataJson = Json.decodeFromString<DataFile>(f.readText())
        val playerData = Json.decodeFromJsonElement<PlayerData>(dataJson.data)
        playerData.file = f
        createMonthly(f)
        return DataFile(2, dataJson.data)
    }

    private fun createMonthly(lifetimeFile : File) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("EST"))
        val fileName = lifetimeFile.name
        val stringUUID = fileName.subSequence(0, fileName.lastIndex-4)
        println(stringUUID)
        val uuid = UUID.fromString(stringUUID.toString())
        println(uuid)
        val lifetimeDataPath = lifetimeFile.toPath()
        val monthlyDataPath = lifetimeDataPath.parent.toString() + "/monthly/" + lifetimeDataPath.name
        val monthlyFile = File(monthlyDataPath)
        if (!monthlyFile.exists()) {
            Files.createDirectories(monthlyFile.parentFile.toPath())
            monthlyFile.createNewFile()
        }
        val monthlyData = MonthlyPlayerData(
            CreditsUtils.getCreditsByMonth(calendar.get(Calendar.MONTH), uuid),
            calendar.get(Calendar.MONTH),
            monthlyFile)
        val monthlyJson = Json.encodeToJsonElement(monthlyData)
        val dataFile = DataFile(MonthlyPlayerData.DATA_VERSION, monthlyJson)
        monthlyFile.writeText(Json.encodeToString(dataFile))
    }

}