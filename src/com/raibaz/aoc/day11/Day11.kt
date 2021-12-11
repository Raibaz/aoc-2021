package com.raibaz.aoc.day11

import readInput

data class Point(
    val row: Int,
    val col: Int
) {
    var value: Int = 0
}

fun main() {
    val input = readInput("com/raibaz/aoc/day11/Day11").flatMapIndexed { row, line ->
        line.mapIndexed { col, value ->
            Point(row, col).also {
                it.value = value.digitToInt()
            }
        }
    }

    part1(input)
}

fun part1(input: List<Point>) {

    var nextInput = input
    var sum = 0L
    (0..999999999).forEach {i ->
        val result = step(nextInput)
        nextInput = result.second
        println("$i: ${result.first}")
        sum += result.first
        println(sum)
        if (result.first == result.second.size) {
            println(i)
            return
        }
    }

}

fun step(input: List<Point>): Pair<Int, List<Point>> {
    printGrid(input)

    input.forEach {
        it.value++
    }

    val flashed = mutableSetOf<Point>()

    while(input.any { it.value > 9 && !flashed.contains(it) }) {
        val flashing = input.filter { it.value > 9 && !flashed.contains(it) }
        flashed.addAll(flashing)

        flashing.forEach {
            // println("${it.row}, ${it.col} flashed!")
            getAdjacents(it, input).forEach { adjacent ->
                input.find { p -> p.row == adjacent.row && p.col == adjacent.col }!!.value++
            }
        }
    }

    input.filter { flashed.contains(it) }.forEach { it.value = 0 }

    return flashed.size to input
}

fun getAdjacents(point: Point, input: List<Point>): List<Point> {
    val ret = mutableSetOf<Point>()

    if (point.row > 0) {
        ret.add(input.find { it.row == point.row - 1 && it.col == point.col }!!)
        if (point.col > 0) {
            ret.add(input.find { it.row == point.row - 1 && it.col == point.col - 1 }!!)
        }

        if (point.col < 9) {
            ret.add(input.find { it.row == point.row - 1 && it.col == point.col + 1 }!!)
        }
    }

    if (point.row < 9) {
        ret.add(input.find { it.row == point.row + 1 && it.col == point.col }!!)
        if (point.col > 0) {
            ret.add(input.find { it.row == point.row + 1 && it.col == point.col - 1 }!!)
        }

        if (point.col < 9) {
            ret.add(input.find { it.row == point.row + 1 && it.col == point.col + 1 }!!)
        }
    }

    if (point.col > 0) {
        ret.add(input.find { it.row == point.row && it.col == point.col - 1 }!!)
    }

    if (point.col < 9) {
        ret.add(input.find { it.row == point.row && it.col == point.col + 1 }!!)
    }

    return ret.toList()
}

fun printGrid(input: List<Point>) {
    (0..9).forEach { row ->
        (0..9).forEach { col ->
            print(input.find { it.row == row && it.col == col}!!.value)
        }
        print("\n")
    }
}
