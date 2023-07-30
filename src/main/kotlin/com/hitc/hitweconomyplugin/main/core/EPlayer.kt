package com.hitc.hitweconomyplugin.main.core

import com.hitc.hitweconomyplugin.main.Main
import com.hitc.hitweconomyplugin.main.utils.GameFileUtils
import com.hitc.hitweconomyplugin.main.utils.GeneralUtils
import core.HPlayer
import org.bukkit.entity.Player

class EPlayer(
    hPlayer : HPlayer,
    var dailyScores : Scores,
    var playerData : PlayerData,
    var monthlyData : MonthlyPlayerData
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
                GameFileUtils.initPlayerScores(p),
                GameFileUtils.initPlayerData(p),
                GameFileUtils.initMonthlyData(p)
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
                GameFileUtils.initPlayerScores(player),
                GameFileUtils.initPlayerData(player),
                GameFileUtils.initMonthlyData(player)
            )
            Main.ePlayers.add(ePlayer)
            return ePlayer
        }

        fun removePlayer(p : Player) {
            Main.ePlayers.removeIf { it.player == p }
        }

    }

}
