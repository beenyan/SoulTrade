package com.beenyan.soultrade.trade.item

import com.beenyan.soultrade.utils.I18n
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Border {
    companion object {
        val item = init()

        private fun init(): ItemStack {
            val border = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            val meta = border.itemMeta
            meta.displayName(I18n.t("tradeGUI.item.border"))
            border.itemMeta = meta

            return border
        }
    }
}
