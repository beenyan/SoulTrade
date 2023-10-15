package com.beenyan.soultrade.listeners

import com.beenyan.soultrade.commands.soul_trade.Invite
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot
import java.util.*

class PlayerListener : Listener {
    companion object {
        val authorUUID: UUID = UUID.fromString("05aa203a-8877-4136-b0b2-2b71bc4e8f5f")
    }

    @EventHandler
    fun onPlayerLogin(event: PlayerJoinEvent) {
        val player = event.player
        if (player.uniqueId == authorUUID) {
            val message = Component.text("靈魂交易 (Soul Trade) 啟用").color(NamedTextColor.DARK_BLUE).decoration(TextDecoration.ITALIC, false)
            event.player.sendMessage(message)
        }

        event.player.sendMessage("靈魂交易 (Soul Trade) 屬於測試階段，可能藏有淺在 BUG，如經發現請上巴哈回報，在此至上 12 萬分的謝意 <3")
    }

    @EventHandler
    fun onPLayerRightClick(event: PlayerInteractEntityEvent) {
        val player = event.player
        val target = event.rightClicked
        if (event.hand == EquipmentSlot.OFF_HAND || target !is Player || !player.isSneaking) {
            return
        }

        Invite().execute(player, target)
    }
}