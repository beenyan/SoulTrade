package com.beenyan.soultrade.shulkerPreview

import com.beenyan.soultrade.SoulTrade
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class Listener : Listener {
    @EventHandler
    fun onPlayerClickTradeGUI(event: InventoryClickEvent) {
        if (event.view.topInventory.holder !is GUI) return

        event.isCancelled = true
        val trade = Shulker.findByPlayer(event.whoClicked) ?: return
        trade.shulker.stopPreview(event.whoClicked)
        return
    }

    @EventHandler
    fun onPlayerCloseTradeGUI(event: InventoryCloseEvent) {
        if (event.view.topInventory.holder !is GUI) return

        if (event.reason != InventoryCloseEvent.Reason.OPEN_NEW) {
            val trade = Shulker.findByPlayer(event.player) ?: return
            Bukkit.getScheduler().scheduleSyncDelayedTask(SoulTrade.plugin) {
                trade.shulker.stopPreview(event.player)
            }
        }
        return
    }
}