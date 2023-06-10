package com.hitc.hitweconomyplugin.main.core

enum class GameType(var text: String) {
    QUALIFIERS("Qualification"),
    FINALS("Finals"),
    LOBBY("Wide Qualification"),
    WIDE_QUALIFIERS("Lobby Wall"),
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