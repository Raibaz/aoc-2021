package com.raibaz.aoc.day3

import readInput
import kotlin.math.pow

fun main() {
    val input = readInput("com/raibaz/aoc/day3/Day03")

    val mostCommons = (0 until input.first().length).map { index ->
        findMostCommon(input.map { it[index] })
    }.joinToString("")

    val leastCommons = (0 until input.first().length).map { index ->
        findLeastCommon(input.map { it[index] })
    }.joinToString("")

    val mostCommonInt = binaryToDecimal(mostCommons)
    val leastCommonInt = binaryToDecimal(leastCommons)
    println(mostCommonInt * leastCommonInt)

    var oxygenRatings = input
    var index = 0
    while (oxygenRatings.size > 1) {
        val mostCommon = findMostCommon(oxygenRatings.map { line -> line[index] })
        oxygenRatings = oxygenRatings.filter {
            it[index].digitToInt() == mostCommon
        }

        index++
    }

    var co2Ratings = input
    var co2Index = 0
    while(co2Ratings.size > 1) {
        val leastCommon = findLeastCommon(co2Ratings.map { line -> line[co2Index] })
        co2Ratings = co2Ratings.filter {
            it[co2Index].digitToInt() == leastCommon
        }
        co2Index++
    }

    println(binaryToDecimal(oxygenRatings.first()) * binaryToDecimal(co2Ratings.first()))
}

fun findMostCommon(input: List<Char>): Int {
    val zeroes = input.count { it == '0' }
    val ones = input.size - zeroes
    return if(zeroes > ones) {
        0
    } else 1
}

fun findLeastCommon(input: List<Char>): Int {
    val zeroes = input.count { it == '0' }
    val ones = input.size - zeroes
    return if(zeroes > ones) {
        1
    } else 0
}

fun binaryToDecimal(input: String): Int {
    return input.reversed().foldIndexed(0) { index, acc, char ->
        acc + char.digitToInt() * 2.0.pow(index.toDouble()).toInt()
    }
}
