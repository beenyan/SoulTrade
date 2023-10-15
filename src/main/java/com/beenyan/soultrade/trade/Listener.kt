package com.beenyan.soultrade.trade

import com.beenyan.soultrade.shulkerPreview.Shulker
import com.beenyan.soultrade.utils.Configs
import org.bukkit.block.ShulkerBox
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.meta.BlockStateMeta

class Listener : Listener {
    @EventHandler
    fun onPlayerClickTradeGUI(event: InventoryClickEvent) {
        val topInv = event.view.topInventory
        if (topInv.holder !is GUI) return
        event.isCancelled = true
        val item = event.currentItem ?: return

        val trade = Trade.findByInv(topInv) ?: return
        if (event.clickedInventory?.holder is GUI) {
            when (event.slot) {
                Configs.trade.closeButtonSlot -> trade.close.click(event.whoClicked)

                Configs.trade.initiatorLockButtonSlot, Configs.trade.receiverLockButtonSlot -> trade.lock.click(event)

                Configs.trade.confirmButtonSlot -> trade.confirm.click(event)

                else -> {
                    if (Shulker.isShulkerBox(item) && event.action != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        return trade.shulker.preview((item.itemMeta as BlockStateMeta).blockState as ShulkerBox, event)
                    }

                    trade.gui.click(event)
                }
            }
        } else {
            trade.gui.click(event)
        }
    }

    @EventHandler
    fun onPlayerCloseTradeGUI(event: InventoryCloseEvent) {
        val topInv = event.view.topInventory
        if (topInv.holder !is GUI || event.reason == InventoryCloseEvent.Reason.OPEN_NEW) return

        val trade = Trade.findByInv(topInv) ?: return
        trade.close.click(event.player)
    }
}