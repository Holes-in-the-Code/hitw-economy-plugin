package com.hitc.hitweconomyplugin.main.core

import com.hitc.hitweconomyplugin.main.utils.GameFileUtils
import com.hitc.hitweconomyplugin.main.utils.GeneralUtils
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.bukkit.entity.Player
import java.io.File

@Serializable
data class MonthlyPlayerData(
    private var creditsEarned: Int = 0,
    var month : Int,
    @Transient var file : File? = null
) {
    fun addCreditsEarned(credits : Int, player : Player) {
        this.creditsEarned += credits
        save(player, credits)
    }

    fun save(player: Player, credits: Int) {
        val file = this.file ?: throw Error("Cannot find file to write to.")

        if (!GeneralUtils.checkMonth(month)) {
            val monthlyData = GameFileUtils.initMonthlyData(player)

            creditsEarned = monthlyData.creditsEarned + credits
            month = monthlyData.month
        }

        val scoresJson = Json.encodeToJsonElement(this)
        val dataFile = DataFile(DATA_VERSION, scoresJson)
        file.writeText(Json.encodeToString(dataFile))
    }
    companion object {
        const val DATA_VERSION = 1
    }
}
