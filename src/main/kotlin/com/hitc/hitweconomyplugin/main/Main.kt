package com.hitc.hitweconomyplugin.main

import com.hitc.hitweconomyplugin.main.core.GameType
import com.hitc.hitweconomyplugin.main.core.Score
import com.hitc.hitweconomyplugin.main.utils.GameFileUtils
import core.GameEndEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Files
import kotlin.io.path.Path

class Main: JavaPlugin(), Listener {

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        Files.createDirectories(Path("./plugins/HitW/scores"))
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent?) {
        event?.player?.sendMessage("Boop!")
    }

    @EventHandler
    fun onGameEnd(event: GameEndEvent) {
        val gameType = GameType.fromString(event.game.name) ?: return
        val score = Pair(gameType, event.player.score)
        if (!event.game.isClassic) return

        CoroutineScope(Dispatchers.IO).launch {
            GameFileUtils.appendScore(event.player.player.uniqueId, Score(score))
        }
    }

}