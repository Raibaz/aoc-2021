package com.raibaz.aoc.day07

import readInput
import kotlin.math.abs

fun main() {
    val input = readInput("com/raibaz/aoc/day07/Day07").first().split(",").map { it.toInt() }
    val maxPos = input.maxOf { it } / 2

    val min = (0..maxPos).minOf { candidatePosition ->
        val cost = input.sumOf { computeCost(it, candidatePosition) }
        cost
    }

    println(min)
}

fun computeCost(startingPosition: Int, candidatePosition: Int): Int {
    val distance = abs(startingPosition - candidatePosition)
    return (0 until distance).sumOf { it + 1 }
}
