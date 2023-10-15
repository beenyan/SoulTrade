package com.beenyan.soultrade.shulkerPreview.item

import com.beenyan.soultrade.utils.I18n
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Border {
    companion object {
        fun getByColor(color: DyeColor?): ItemStack {
            val material = when (color) {
                DyeColor.WHITE -> Material.WHITE_STAINED_GLASS_PANE
                DyeColor.ORANGE -> Material.ORANGE_STAINED_GLASS_PANE
                DyeColor.MAGENTA -> Material.MAGENTA_STAINED_GLASS_PANE
                DyeColor.LIGHT_BLUE -> Material.LIGHT_BLUE_STAINED_GLASS_PANE
                DyeColor.YELLOW -> Material.YELLOW_STAINED_GLASS_PANE
                DyeColor.LIME -> Material.LIME_STAINED_GLASS_PANE
                DyeColor.PINK -> Material.PINK_STAINED_GLASS_PANE
                DyeColor.GRAY -> Material.GRAY_STAINED_GLASS_PANE
                DyeColor.LIGHT_GRAY -> Material.LIGHT_GRAY_STAINED_GLASS_PANE
                DyeColor.CYAN -> Material.CYAN_STAINED_GLASS_PANE
                DyeColor.PURPLE -> Material.PURPLE_STAINED_GLASS_PANE
                DyeColor.BLUE -> Material.BLUE_STAINED_GLASS_PANE
                DyeColor.BROWN -> Material.BROWN_STAINED_GLASS_PANE
                DyeColor.GREEN -> Material.GREEN_STAINED_GLASS_PANE
                DyeColor.RED -> Material.RED_STAINED_GLASS_PANE
                DyeColor.BLACK -> Material.BLACK_STAINED_GLASS_PANE
                null -> Material.GLASS_PANE
            }


            val item = ItemStack(material)
            val meta = item.itemMeta
            meta.displayName(I18n.t("ShulkerGUI.item.border"))
            item.itemMeta = meta

            return item
        }
    }
}
