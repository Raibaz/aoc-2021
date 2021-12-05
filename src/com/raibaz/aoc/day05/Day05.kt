package com.raibaz.aoc.day05

import readInput
import kotlin.math.max
import kotlin.math.abs

fun main() {

    val input = readInput("com/raibaz/aoc/day05/Day05")

    val segments = parseInput(input)

    val map = Map()

    segments.forEach { segment ->

        val start = segment.start
        val end = segment.end

        val segmentLength = max(abs(start.x - end.x), abs(start.y - end.y))
        (0..segmentLength).map { step ->
            val x = when {
                end.x > start.x -> start.x + step
                end.x < start.x -> start.x - step
                else -> start.x
            }

            val y = when {
                end.y > start.y -> start.y + step
                end.y < start.y -> start.y - step
                else -> start.y
            }
            map.increment(x, y)
        }

    }

    println(map.countAbove(2))
}

fun parseInput(input: List<String>) = input.map {
    val coords = it.split(" -> ")
    val start = coords[0]
    val end = coords[1]
    val startCoords = start.split(",")
    val startPoint = Point(startCoords[0].toInt(), startCoords[1].toInt())
    val endCoords = end.split(",")
    val endPoint = Point(endCoords[0].toInt(), endCoords[1].toInt())
    Segment(startPoint, endPoint)
}

data class Point(
    val x: Int,
    val y: Int
)

data class Segment(
    val start: Point,
    val end: Point
) {
    fun isHorizontalOrVertical() = start.x == end.x || start.y == end.y
}

data class Cell(
    val x: Int = 0,
    val y: Int = 0,
    var count: Int = 0,
)

data class Map(
    val cells : MutableMap<Int, MutableMap<Int, Cell>> = mutableMapOf()
) {
    fun find(x: Int, y: Int): Cell {
        val foundX = cells.getOrDefault(x, mutableMapOf())
        val cell = foundX.getOrDefault(y, Cell(x, y, 0))
        foundX.putIfAbsent(y, cell)
        cells.putIfAbsent(x, foundX)
        return cell
    }

    fun increment(x: Int, y: Int) {
        val found = find(x, y)
        found.count++
    }

    fun countAbove(threshold: Int): Int {
        return cells.values.sumOf { it.count { cell -> cell.value.count >= threshold } }
    }
}
