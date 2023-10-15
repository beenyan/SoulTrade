package com.beenyan.soultrade.commands.soul_trade

import com.beenyan.soultrade.commands.SubCmd
import com.beenyan.soultrade.utils.I18n
import org.bukkit.command.CommandSender

class History : SubCmd {
    override val name = "history"

    override fun showHelp(sender: CommandSender): Boolean {
        sender.sendMessage(I18n.t("cmd.st.history.help"))
        return true
    }

    override fun execute(sender: CommandSender, args: Array<String>) {
        
    }
}