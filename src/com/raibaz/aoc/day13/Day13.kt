package com.raibaz.aoc.day13

import readInput

data class Point(
    val x: Int,
    val y: Int
)

data class Grid(
    val points: Set<Point>,
    val xMax: Int,
    val yMax: Int
)

enum class FoldType { X, Y; }

fun main() {
    // runPart1("Day13_test", 17)
    // runPart1("Day13", null)

    // runPart2("Day13_test", null)
    runPart2("Day13", null)
}

fun runPart1(fileName: String, expectedValue: Int?) {
    runFolding(fileName, expectedValue, 1)
}

fun runPart2(fileName: String, expectedValue: Int?) {
    runFolding(fileName, expectedValue, 100)
}

fun runFolding(fileName: String, expectedValue: Int?, foldCount: Int) {
    val input = readInput("com/raibaz/aoc/day13/$fileName")
    var grid = buildGrid(input.takeWhile { it.isNotEmpty() })
    val folds = input.dropWhile { it.isNotEmpty() }.drop(1)
    println("Before folds grid size = ${grid.points.size}")
    folds.take(foldCount).forEach {
        val split = it.removePrefix("fold along ").split("=")
        val type = FoldType.valueOf(split[0].uppercase())
        val value = split[1].toInt()
        grid = grid.fold(type, value)
        if (expectedValue == null || grid.points.size == expectedValue) {
            println("Ok, result = ${grid.points.size}")
        } else {
            println(grid)
            error("Result = $grid, Expected value = $expectedValue")
        }
    }
}

fun buildGrid(input: List<String>): Grid {
    val points = input.map {
        val split = it.split(",")
        Point(split[0].toInt(), split[1].toInt())
    }.toSet()
    val xMax = points.maxOf { it.x }
    val yMax = points.maxOf { it.y }
    return Grid(points, xMax, yMax)
}


fun Grid.fold(type: FoldType, value: Int): Grid {
    println("Folding along $type with value $value")
    val toBeFolded = this.points.filter {
        when (type) {
            FoldType.X -> it.x > value
            FoldType.Y -> it.y > value
        }
    }

    val folded = toBeFolded.map {
        val result = when (type) {
            FoldType.X -> Point(value - (it.x - value), it.y)
            FoldType.Y -> Point(it.x, value - (it.y - value))
        }
        println("Folding $it to $result")
        result
    }

    val newPoints = (this.points + folded).filter {
        when (type) {
            FoldType.X -> it.x < value
            FoldType.Y -> it.y < value
        }
    }.toSet()
    val ret = when(type) {
        FoldType.X -> Grid(newPoints, value - 1, yMax)
        FoldType.Y -> Grid(newPoints, xMax, value - 1)
    }

    ret.print()
    return ret
}

fun Grid.print() {
    (0..yMax).forEach { y ->
        (0..xMax).forEach { x ->
            if (this.points.contains(Point(x, y))) {
                print("#")
            } else {
                print(".")
            }
        }
        print("\n")
    }
}
