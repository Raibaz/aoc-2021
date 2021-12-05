package com.raibaz.aoc.day02

import readInput

fun main() {
    val input = readInput("com/raibaz/aoc/day02/Day02")

    println(processInstructions(input))
}

fun processInstructions(input: List<String>): Int {
    var horizontalPosition = 0
    var depth = 0
    var aim = 0

    input.forEach { line ->
        val tokens = line.split(" ")
        when(tokens[0]) {
            "forward" -> {
                horizontalPosition += tokens[1].toInt()
                depth += aim * tokens[1].toInt()
            }
            "up" -> aim -= tokens[1].toInt()
            "down" -> aim += tokens[1].toInt()
        }
    }

    return horizontalPosition * depth
}
