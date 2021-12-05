package com.raibaz.aoc.day4

import readInput

const val boardSize = 5

fun main() {
    val input = readInput("com/raibaz/aoc/day4/Day04")

    val extracted = input.first().split(",")

    val boards = input.drop(1).filter { it.isNotEmpty() }.windowed(5, 5).map { window ->
        val cells = window.flatMapIndexed { rowIndex, line->
            line.split(" ").filter { it.isNotEmpty() }.mapIndexed { colIndex, value ->
                Cell(rowIndex, colIndex, value.toInt())
            }
        }
        Board(cells)
    }

    extracted.forEach { number ->
        val notWinningYet = boards.filterNot { it.wins() }
        notWinningYet.forEach { board ->
            board.find(number.toInt())?.marked = true
        }
        if (notWinningYet.size == 1 && notWinningYet.first().wins()) {
            println(notWinningYet.first().sumUnmarked() * number.toInt())
        }
    }

}

data class Cell(
    val row: Int = 0,
    val column: Int = 0,
    val value: Int = 0,
    var marked: Boolean = false
)

data class Board(
    val cells: List<Cell> = listOf()
) {
    fun find(value: Int) = cells.find { it.value == value }

    fun wins(): Boolean {
        return cells.groupBy { it.row }.any { it.value.all { cell -> cell.marked } } ||
        cells.groupBy { it.column }.any { it.value.all { cell -> cell.marked } }
    }

    fun sumUnmarked() = cells.filter { !it.marked }.sumOf { it.value }
}
