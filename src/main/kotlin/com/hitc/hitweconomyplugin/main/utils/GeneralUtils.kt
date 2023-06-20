package com.hitc.hitweconomyplugin.main.utils

import core.Board
import core.HPlayer
import main.Main
import org.bukkit.entity.Player
import utils.RankManager
import java.util.*

object GeneralUtils {

    fun createHPlayer(p : Player) : HPlayer {
        val rank = RankManager.loadRank(p)
        val board = Board(p, rank, "§r")
        val wallColor: Short = 9
        val glassColor: Short = 5
        val leverDelay = 0.5f
        val memTime = 1.0f
        val brushLag = 100.0f
        val fly = true
        val title = true
        val rightSided = false
        val oldAnimation = false
        val blind = false
        val destroy = false
        val autoLeave = true
        val perfectOnly = false
        val invisibleGlass = false
        val songName = "Hyperdron - Inter-Dimensional Existence Kontrol"
        val scoreFinals = 0
        val scoreQualification = 0
        val scoreWideQualification = 0
        val scoreLobby = 0
        val scoreWideFinals = 0

        val hPlayer = HPlayer(
            p,
            wallColor,
            glassColor,
            leverDelay,
            memTime,
            brushLag,
            fly,
            title,
            rightSided,
            oldAnimation,
            blind,
            destroy,
            autoLeave,
            board,
            rank,
            songName,
            scoreQualification,
            scoreFinals,
            scoreWideQualification,
            scoreLobby,
            scoreWideFinals,
            perfectOnly,
            invisibleGlass
        )

        hPlayer.restorePlayerData()
        Main.hPlayers.add(hPlayer)
        return hPlayer
    }

    fun checkDate(date : Calendar) : Boolean {
        val currentDate = Calendar.getInstance(TimeZone.getTimeZone("EST"))
        return date.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR)
    }

}