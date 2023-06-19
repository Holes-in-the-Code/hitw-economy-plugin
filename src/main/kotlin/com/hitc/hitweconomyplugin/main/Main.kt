package com.hitc.hitweconomyplugin.main

import com.hitc.hitweconomyplugin.main.core.EPlayer
import com.hitc.hitweconomyplugin.main.core.GameType
import com.hitc.hitweconomyplugin.main.core.Score
import com.hitc.hitweconomyplugin.main.utils.CreditsUtils
import com.hitc.hitweconomyplugin.main.utils.GameFileUtils
import core.GameEndEvent
import core.HPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Files
import kotlin.io.path.Path

class Main: JavaPlugin(), Listener {

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        Files.createDirectories(Path("./plugins/HitW/scores"))
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = HPlayer.getHPlayer(event.player) ?: return
        val dailyFile = GameFileUtils.getDailyDataFile(event.player)
        val dataFile = GameFileUtils.getDataFile(event.player)

        val ePlayer = EPlayer(player, dailyFile, dataFile)
        ePlayers.add(ePlayer)
    }

    @EventHandler
    fun onPlayerQuit(event : PlayerQuitEvent) {
        EPlayer.removePlayer(event.player)
    }


    @EventHandler
    fun onGameEnd(event: GameEndEvent) {
        val player = event.player
        val gameType = GameType.fromString(event.game.name) ?: return
        val score = Score(gameType, player.score)
        if (!event.game.isClassic) return
        if (event.manualEnd) {
            player.player.sendMessage("§cNo credits were awarded, as you ended the game early!")
            return
        }
        val ePlayer = EPlayer.getFromPlayer(player)

        CoroutineScope(Dispatchers.IO).launch {
            val credits = CreditsUtils.addCredits(gameType, score.score, ePlayer)
            GameFileUtils.appendScore(ePlayer, score)
            withContext(Dispatchers.Default) {
                player.player.sendMessage("§dYou earned §a$credits §dcredits!")
            }
        }
    }

    companion object {
        val ePlayers = ArrayList<EPlayer>()
    }
}