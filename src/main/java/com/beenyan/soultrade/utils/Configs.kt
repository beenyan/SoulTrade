package com.beenyan.soultrade.utils

import com.beenyan.soultrade.SoulTrade
import org.bukkit.configuration.file.FileConfiguration

class TradeConfigs {
    companion object {
        var reach = Configs.config.getInt("trade.reach", 5)
        var closeButtonSlot = Configs.config.getInt("trade.closeButtonSlot", 0)
        var initiatorLockButtonSlot = Configs.config.getInt("trade.initiatorLockButtonSlot", 1)
        var receiverLockButtonSlot = Configs.config.getInt("trade.receiverLockButtonSlot", 7)
        var confirmButtonSlot = Configs.config.getInt("trade.confirmButtonSlot", 8)
    }
}

class Configs {
    companion object {
        var config: FileConfiguration = SoulTrade.plugin.config
        val trade = TradeConfigs
    }
}