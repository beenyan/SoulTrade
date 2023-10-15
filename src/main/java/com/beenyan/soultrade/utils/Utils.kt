package com.beenyan.soultrade.utils

import org.bukkit.entity.Player
import kotlin.math.pow
import kotlin.math.sqrt

fun notFoundPlayer(player: Player, targetName: String) {
    player.sendMessage(I18n.t("util.notFoundPlayer", targetName))
}

fun distance(p1: Player, p2: Player): Double {
    val l1 = p1.location
    val l2 = p2.location

    return sqrt((l1.x - l2.x).pow(2) + (l1.y - l2.y).pow(2) + (l1.z - l2.z).pow(2))
}