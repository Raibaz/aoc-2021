package com.raibaz.aoc.day12

import readInput

typealias Node = Map.Entry<String, List<String>>
typealias Graph = Map<String, Node>
typealias Path = List<String>

fun main() {

    runPart1Test("Day12_test", 10)
    runPart1Test("Day12_test2", 19)
    runPart1Test("Day12_test3", 226)
    runPart1Test("Day12", null)

    runPart2Test("Day12_test", 36)
    runPart2Test("Day12_test2", 103)
    runPart2Test("Day12_test3", 3509)
    runPart2Test("Day12", null)
}

fun runPart1Test(fileName: String, expectedResult: Int?) {
    val input = readInput("com/raibaz/aoc/day12/$fileName")

    val maze = initMaze(input)
    val allPaths = maze.findPaths(smallTwice = 0)
    if(expectedResult != null && allPaths.count() != expectedResult) {
        println(allPaths)
        error("With file $fileName, expected = $expectedResult, actual = ${allPaths.count()}")
    } else {
        println("Test ok, paths = ${allPaths.count()}")
    }
}

fun runPart2Test(fileName: String, expectedResult: Int?) {
    val input = readInput("com/raibaz/aoc/day12/$fileName")

    val maze = initMaze(input)
    val allPaths = maze.findPaths(smallTwice = 1)
    if(expectedResult != null && allPaths.count() != expectedResult) {
        println(allPaths)
        error("With file $fileName, expected = $expectedResult, actual = ${allPaths.count()}")
    } else {
        println("Test ok, paths = ${allPaths.count()}")
    }
}

fun initMaze(input: List<String>): Graph = input.map { it.split("-") }
    .flatMap { listOf(it, it.reversed()) }
    .groupBy({it[0]}, {it[1]})
    .mapValues { it }

fun Graph.findPaths(name: String = "start", path: Path = listOf(), smallTwice: Int): List<Path> =
    when {
        name == "end" -> listOf(path + name)
        name == "start" && path.isNotEmpty() -> emptyList()
        path.noRevisit(name, smallTwice) -> emptyList()
        else -> targets(name).flatMap { findPaths(it, path + name, smallTwice) }
    }

fun Path.noRevisit(name: String, smallTwice: Int) = contains(name) && name.isLowercase() &&
        groupingBy { it }.eachCount().count { it.key.isLowercase() && it.value >= 2 } == smallTwice

fun Graph.targets(name: String) = get(name)!!.value

fun String.isLowercase() = this[0].isLowerCase()
