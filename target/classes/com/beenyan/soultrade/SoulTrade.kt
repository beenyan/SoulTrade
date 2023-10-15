package com.beenyan.soultrade

import com.beenyan.soultrade.commands.SoulTradeCmd
import com.beenyan.soultrade.listeners.PlayerListener
import com.beenyan.soultrade.trade.Trade
import com.beenyan.soultrade.utils.DB
import com.beenyan.soultrade.utils.I18n
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class SoulTrade : JavaPlugin() {
    companion object {
        lateinit var plugin: SoulTrade
    }

    override fun onEnable() {
        // Plugin startup logic
        plugin = this
        DB.connect()
        I18n.load()
        commandManager()
        listenerManager()
        Bukkit.getLogger().info(I18n.text("log.onEnable"))
    }

    override fun onDisable() {
        // Plugin shutdown logic
        closeAllTrade()
        HandlerList.unregisterAll(this)
    }

    private fun commandManager() {
        getCommand("soul_trade")?.setExecutor(SoulTradeCmd())
    }

    private fun listenerManager() {
        server.pluginManager.registerEvents(PlayerListener(), this)
        server.pluginManager.registerEvents(com.beenyan.soultrade.trade.Listener(), this)
        server.pluginManager.registerEvents(com.beenyan.soultrade.shulkerPreview.Listener(), this)
    }

    private fun closeAllTrade() {
        Trade.tradeList.forEach { trade ->
            trade.initiator.closeInventory()
            trade.receiver.closeInventory()
        }
    }
}
