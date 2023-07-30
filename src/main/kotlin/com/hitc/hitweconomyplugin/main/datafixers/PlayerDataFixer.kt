package com.hitc.hitweconomyplugin.main.datafixers

import com.hitc.hitweconomyplugin.main.core.DataFile
import java.io.File

interface PlayerDataFixer {

    fun getTargetVersion() : Int

    fun fixData(f : File) : DataFile

}