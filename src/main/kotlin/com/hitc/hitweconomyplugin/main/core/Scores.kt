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
import java.util.Calendar

@Serializable
data class Scores(
    private val scores : MutableList<Score>,
    @Transient var file : File? = null,
    @Transient var date : Calendar? = null
) {

    fun getScores() : MutableList<Score> {
        return this.scores
    }

    fun addScore(score : Score, ePlayer: EPlayer) {
        this.scores.add(score)
        save(ePlayer.player)
    }

    private fun save(player: Player) {
        val date = this.date ?: throw Error("Cannot find date associated with data.")
        val file = this.file ?: throw Error("Cannot find file to write to.")


        if (GeneralUtils.checkDate(date)) {
            val playerData = GameFileUtils.initPlayerScores(player)
            this.date = playerData.date
            this.file = playerData.file
        }

        val scoresJson = Json.encodeToJsonElement(this)
        val dataFile = DataFile(DATA_VERSION, scoresJson)
        file.writeText(Json.encodeToString(dataFile))
    }

    companion object {
        const val DATA_VERSION = 1
    }
}
