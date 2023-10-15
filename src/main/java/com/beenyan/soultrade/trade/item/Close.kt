package com.beenyan.soultrade.trade.item

import com.beenyan.soultrade.SoulTrade
import com.beenyan.soultrade.trade.Trade
import com.beenyan.soultrade.utils.Configs
import com.beenyan.soultrade.utils.I18n
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemStack

class Close(private val trade: Trade) {
    companion object {
        val item = init()

        private fun init(): ItemStack {
            val item = ItemStack(Material.STRUCTURE_VOID)
            val meta = item.itemMeta
            meta.displayName(I18n.t("tradeGUI.item.close"))
            item.itemMeta = meta

            return item
        }
    }

    init {
        trade.gui.inventory.setItem(Configs.trade.closeButtonSlot, item)
    }

    fun click(whoClicked: HumanEntity) {
        val passive = if (trade.initiator == whoClicked) trade.receiver else trade.initiator

        Bukkit.getLogger().info(I18n.text("log.closeTrade", arrayOf(whoClicked.name, passive.name)))
        passive.sendMessage(I18n.t("tradeGUI.cancelTrade", whoClicked.name))

        Trade.tradeList.remove(trade)
        Bukkit.getScheduler().scheduleSyncDelayedTask(SoulTrade.plugin) {
            trade.gui.inventory.close()
        }
    }
}