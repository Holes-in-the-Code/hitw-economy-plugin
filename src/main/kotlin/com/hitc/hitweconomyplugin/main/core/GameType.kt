package com.hitc.hitweconomyplugin.main.core

enum class GameType(var text: String) {
    QUALIFIERS("Qualification"),
    FINALS("Finals"),
    LOBBY("Lobby Wall"),
    WIDE_QUALIFIERS("Wide Qualification"),
    WIDE_FINALS("Wide Finals");

    companion object {
        fun fromString(text: String): GameType? {
            for (type: GameType in GameType.values()) {
                if (type.text == text) return type
            }
            return null
        }
    }
}