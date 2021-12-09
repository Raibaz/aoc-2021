package com.raibaz.aoc.day09

import readInput

typealias Grid = Array<IntArray>
typealias Point = Pair<Int, Int>

fun main() {

    val input = readInput("com/raibaz/aoc/day09/Day09").map { line ->
        line.map { it.digitToInt() }.toIntArray()
    }.toTypedArray()

    // part1(input)
    part2(input)
}

fun part1(input: Grid) {
    val riskSum = input.flatMapIndexed { rowIndex, row ->
        row.filterIndexed { colIndex, i ->
            getAdjacents(input, rowIndex, colIndex).all { input[it.first][it.second] > i }
        }
    }.sumOf { it + 1 }

    println(riskSum)
}

fun part2(input: Grid) {

    val lowPointCoords = (input.indices).mapNotNull { row ->
        (input.first().indices).mapNotNull { col ->
            if(getAdjacents(input, row, col).all { input[it.first][it.second] > input[row][col] }) {
                row to col
            } else {
                null
            }
        }
    }.flatten()

    val basinSizes = lowPointCoords
        .map { getBasinSize(input, it) }
        .sorted()
        .map {
            println(it)
            it
        }
        .takeLast(3)
        .fold(1) { acc, i -> acc * i }

        println(basinSizes)
}

fun getBasinSize(input: Grid, coords: Point): Int {
    val basinComponents = mutableSetOf(coords)
    val visited = mutableListOf<Pair<Int, Int>>()
    var count = 0
    while(basinComponents.isNotEmpty()) {
        val cur = basinComponents.first()
        basinComponents.remove(cur)
        val nextComponents = getAdjacents(input, cur.first, cur.second).filter {
            input[it.first][it.second] < 9 && !visited.contains(it)
        }
        visited.add(cur)
        basinComponents.addAll(nextComponents)
        count++
    }
    return count
}

fun getAdjacents(input: Grid, row: Int, col: Int): List<Point> {
    val ret = mutableListOf<Pair<Int, Int>>()
    if (row >= 1) {
        ret.add(row-1 to col)
    }

    if (row < input.size - 1) {
        ret.add(row+1 to col)
    }

    if (col >= 1) {
        ret.add(row to col-1)
    }

    if (col < input.first().size - 1) {
        ret.add(row to col+1)
    }

    return ret
}


