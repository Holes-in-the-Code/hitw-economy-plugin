package com.hitc.hitweconomyplugin.main.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File


@Serializable
class PlayerData(
    private var credits: Int = 0,
    private var creditsEarned: Int = 0,
    @Transient var file : File? = null
) {

    fun addCredits(credits : Int) {
        this.credits += credits
        save()
    }

    fun subCredits(credits : Int) {
        this.credits -= credits
        save()
    }

    fun getCredits() : Int {
        return this.credits
    }

    fun addCreditsEarned(credits : Int) {
        this.creditsEarned += credits
        save()
    }

    fun getCreditsEarned() : Int {
        return this.creditsEarned
    }

    private fun save() {
        val file = this.file ?: throw Error("Cannot find file to write to.")

        val playerDataJson = Json.encodeToJsonElement(this)
        val dataFile = DataFile(DATA_VERSION, playerDataJson)

        file.writeText(Json.encodeToString(dataFile))
    }

    companion object {
        const val DATA_VERSION = 1
    }

}
