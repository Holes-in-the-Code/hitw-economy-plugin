package com.hitc.hitweconomyplugin.main.datafixers

object DataFixers {
    val fixers : MutableList<in PlayerDataFixer> = MutableList(0){}

    init {
        fixers.add(MonthlyDatafixer)
    }
}