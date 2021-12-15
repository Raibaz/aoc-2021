package com.raibaz.aoc.day15

import readInput
import java.lang.Integer.min

data class Point(
    val x: Int,
    val y: Int,
    val cost: Int,
)

data class Grid(
    val points: MutableMap<Int, MutableMap<Int, Point>> = mutableMapOf()
) {
    fun addPoint(point: Point) {
        val y = points.getOrDefault(point.y, mutableMapOf())
        y[point.x] = point
        points[point.y] = y
    }

    fun traverse(): Map<Point, Int> {
        val destination = points[points.keys.maxOf { it }]!![points.values.first().keys.maxOf { it }]
        val visited = mutableMapOf<Point, Int>()
        val unvisited = points.values.flatMap { row -> row.values.map { it to Int.MAX_VALUE} }.toMap().toMutableMap()
        var current = points[0]!![0]!!
        unvisited[current] = 0
        while (unvisited.isNotEmpty()) {
            current = unvisited.minByOrNull { it.value }!!.key
            visited[current] = unvisited[current]!!
            unvisited.remove(current)

            if (current == destination) {
                return visited
            }

            val neighbors = getAdjacents(current).filter { !visited.contains(it) }
            neighbors.forEach {
                unvisited[it] = min(unvisited[it]!!, visited[current]!! + it.cost)
            }

        }
        return visited
    }

    fun getAdjacents(point: Point): List<Point> {
        val ret = mutableSetOf<Point>()
        points[point.y]?.get(point.x - 1)?.let {
            ret.add(it)
        }
        points[point.y]?.get(point.x + 1)?.let {
            ret.add(it)
        }
        points[point.y - 1]?.get(point.x)?.let {
            ret.add(it)
        }
        points[point.y + 1]?.get(point.x)?.let {
            ret.add(it)
        }

        return ret.toList()
    }

    infix operator fun plus(other: Grid): Grid {
        other.points.flatMap { (_, line) ->
            line.map { (_, point) ->
            this.addPoint(point)
        } }
        return this
    }

    fun print() {
        val maxX = points.values.flatMap { it.values }.maxOf { it.x }
        val maxY = points.values.flatMap { it.values }.maxOf { it.y }
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                print(points[y]!![x]!!.cost)
            }
            print("\n")
        }
    }
}

fun main() {
    // runPart1("Day15_test", 40)
    // runPart1("Day15", null)

    runPart2("Day15_test", 315)
    runPart2("Day15", null)
}

fun runPart1(fileName: String, expectedValue: Int?) {
    val input = readInput("com/raibaz/aoc/day15/$fileName")
    val grid = buildGrid(input)
    grid.print()
    doStuff(grid, expectedValue)
}

fun runPart2(fileName: String, expectedValue: Int?) {
    val input = readInput("com/raibaz/aoc/day15/$fileName")
    val grid = buildGridForPart2(input)
    grid.print()
    doStuff(grid, expectedValue)
}

fun doStuff(grid: Grid, expectedValue: Int?) {
    val allCosts = grid.traverse()
    // println(allCosts)
    val maxX = allCosts.keys.maxOf { it.x }
    val maxY = allCosts.keys.maxOf { it.y }
    val destCost = allCosts[allCosts.keys.find { it.x == maxX && it.y == maxY }]
    if (expectedValue == null || destCost == expectedValue) {
        println("Ok, destCost = $destCost")
    } else {
        error("Destcost = $destCost")
    }
}

fun buildGrid(input: List<String>, tileX: Int = 0, tileY: Int = 0) = Grid(
    input.mapIndexed { y: Int, line: String ->
        val computedY = y + tileY * input.size
        computedY to line.mapIndexed { x, cost ->
            val computedX = x + tileX * input.size
            var computedCost = cost.digitToInt() + tileX + tileY
            if (computedCost > 9) {
                computedCost -= 9
            }
            computedX to Point(computedX, computedY, computedCost)
        }.toMap().toMutableMap()
    }.toMap().toMutableMap()
)

fun buildGridForPart2(input: List<String>): Grid {
    var ret = Grid(mutableMapOf())
    (0 .. 4).flatMap { tileX ->
        (0..4).map { tileY ->
            ret += buildGrid(input, tileX, tileY)
        }
    }
    return ret
}