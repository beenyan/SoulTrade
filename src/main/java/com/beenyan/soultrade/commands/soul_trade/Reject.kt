package com.beenyan.soultrade.commands.soul_trade

import com.beenyan.soultrade.commands.SubCmd
import com.beenyan.soultrade.trade.Trade
import com.beenyan.soultrade.utils.I18n
import com.beenyan.soultrade.utils.notFoundPlayer
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Reject : SubCmd {
    override val name = "reject"

    override fun showHelp(sender: CommandSender): Boolean {
        sender.sendMessage(I18n.t("cmd.st.reject.help"))
        return true
    }

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            showHelp(sender)
            return
        }
        val target = Bukkit.getPlayer(args[0]) ?: return notFoundPlayer(sender as Player, args[0])

        Trade.tradeList.find { it.initiator == target && it.receiver == sender }?.let { trade ->
            target.sendMessage(I18n.t("cmd.st.reject.requestBeReject", sender.name))
            sender.sendMessage(I18n.t("cmd.st.reject.rejectRequestSuccess"))
            Trade.tradeList.remove(trade)
            return
        }

        sender.sendMessage(I18n.t("cmd.st.reject.notFoundRequest"))
    }
}