package com.hitc.hitweconomyplugin.main.core

import com.hitc.hitweconomyplugin.main.Main
import com.hitc.hitweconomyplugin.main.utils.GameFileUtils
import com.hitc.hitweconomyplugin.main.utils.GeneralUtils
import core.Board
import core.HPlayer
import org.bukkit.entity.Player
import java.io.File
import java.util.Calendar

class EPlayer(
    hPlayer: HPlayer,
    var dailyDataFile: Pair<File, Calendar>,
    val dataFile: File
) : HPlayer(
    hPlayer.player,
    hPlayer.wallColor,
    hPlayer.glassColor,
    hPlayer.leverDelay,
    hPlayer.memTime,
    hPlayer.brushLag,
    hPlayer.isFly,
    hPlayer.isTitle,
    hPlayer.isRightSided,
    hPlayer.isOldAnimation,
    hPlayer.isBlind,
    hPlayer.isDestroy,
    hPlayer.isAutoLeave,
    hPlayer.board,
    hPlayer.rank,
    hPlayer.songName,
    hPlayer.scoreQualification,
    hPlayer.scoreFinals,
    hPlayer.scoreWideQualification,
    hPlayer.scoreLobby,
    hPlayer.scoreWideFinals,
    hPlayer.isPerfectOnly,
    hPlayer.isInvisibleGlass
) {

    companion object {
        fun getFromPlayer(p : Player) : EPlayer {
            for (ePlayer in Main.ePlayers) {
                if (ePlayer.player.player == p) {
                    return ePlayer
                }
            }
            val hPlayer = getHPlayer(p) ?: GeneralUtils.createHPlayer(p)

            val ePlayer = EPlayer(
                hPlayer,
                GameFileUtils.getDailyDataFile(p),
                GameFileUtils.getDataFile(p)
            )
            Main.ePlayers.add(ePlayer)
            return ePlayer
        }

        fun getFromPlayer(p : HPlayer) : EPlayer {
            for (ePlayer in Main.ePlayers) {
                if (ePlayer.player == p) {
                    return ePlayer
                }
            }
            val player = p.player
            val ePlayer = EPlayer(
                p,
                GameFileUtils.getDailyDataFile(player),
                GameFileUtils.getDataFile(player)
            )
            Main.ePlayers.add(ePlayer)
            return ePlayer
        }

        fun removePlayer(p : Player) {
            for (ePlayer in Main.ePlayers) {
                if (ePlayer.player.player == p) {
                    Main.ePlayers.remove(ePlayer)
                }
            }
        }

    }

}
