package com.beenyan.soultrade.commands.soul_trade

import com.beenyan.soultrade.commands.SubCmd
import com.beenyan.soultrade.trade.Trade
import com.beenyan.soultrade.trade.TradeStatus
import com.beenyan.soultrade.utils.Configs
import com.beenyan.soultrade.utils.I18n
import com.beenyan.soultrade.utils.distance
import com.beenyan.soultrade.utils.notFoundPlayer
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Invite : SubCmd {
    override val name = "invite"

    override fun showHelp(sender: CommandSender): Boolean {
        sender.sendMessage(I18n.t("cmd.st.invite.help"))
        return true
    }

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            showHelp(sender)
            return
        }
        val target = Bukkit.getPlayer(args[0]) ?: return notFoundPlayer(sender as Player, args[0])

        return execute(sender, target)
    }

    fun execute(sender: CommandSender, target: Player) {
        // 對自己送出貿易請求
        if (sender as Player == target) {
            return sender.sendMessage(I18n.t("cmd.st.invite.sendToSelf"))
        }

        // 超過貿易可互動距離
        if (Configs.trade.reach != 0 && Configs.trade.reach < distance(sender, target)) {
            return sender.sendMessage(I18n.t("cmd.st.overTradeReach", target.name))
        }

        // 互相發出邀請
        Trade.tradeList.find { it.initiator == target && it.receiver == sender }?.let {
            sender.performCommand("soul_trade accept ${target.name}")
            return
        }

        // 接受貿易者已開始交易
        Trade.tradeList.find { it.receiver == target && it.status == TradeStatus.Start }?.let {
            return sender.sendMessage(I18n.t("cmd.st.invite.receiveStartTrade", target.name, it.initiator.name))
        }

        Trade.tradeList.find { it.initiator == sender }?.let { trade ->
            // 重複送出請求
            if (trade.receiver == target) {
                return sender.sendMessage(I18n.t("cmd.st.invite.alreadySendRequest"))
            }

            // 交易對象改變
            target.sendMessage(I18n.t("cmd.st.invite.tradeTargetChange", sender.name))
        }

        val trade = Trade(sender, target)
        val acceptButtonText = I18n.t("cmd.st.invite.button.accept.clickText").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/soul_trade accept ${sender.name}")).hoverEvent(HoverEvent.showText(I18n.t("cmd.st.invite.button.accept.hoverText")))
        val cancelButtonText = I18n.t("cmd.st.invite.button.reject.clickText").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/soul_trade reject ${sender.name}")).hoverEvent(HoverEvent.showText(I18n.t("cmd.st.invite.button.reject.hoverText")))
        sender.sendMessage(I18n.t("cmd.st.invite.sendRequestSuccess"))
        target.sendMessage(I18n.t("cmd.st.invite.receiveRequest", sender.name).append(acceptButtonText).append(cancelButtonText))

        Trade.tradeList.add(trade)
    }
}