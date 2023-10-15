package com.beenyan.soultrade.commands

import org.bukkit.command.CommandSender

interface SubCmd {
    val name: String

    fun showHelp(sender: CommandSender): Boolean {
        return true
    }

    fun execute(sender: CommandSender, args: Array<String>) {}
}