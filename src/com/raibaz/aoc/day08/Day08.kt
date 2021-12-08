package com.raibaz.aoc.day08

import readInput

fun main() {
    val input = readInput("com/raibaz/aoc/day08/Day08")

    // part1(input)
    part2(input)
}

fun part1(input: List<String>) {
    val count = input
        .map { it.substringAfter("|") }
        .flatMap { it.split(" ") }
        .count { listOf(2, 3, 4, 7).contains(it.length) }

    println(count)
}

fun part2(input: List<String>) {
    val sum = input.sumOf { inputLine ->
        println(inputLine)
        val split = inputLine.split("|")
        val line = Line()
        line.parse(split.first().split(" "))
        line.computeValue(split.last().split(" "))
    }
    println(sum)
}

class Line(
    var zero: String? = null,
    var one: String? = null,
    var two: String? = null,
    var three: String? = null,
    var four: String? = null,
    var five: String? = null,
    var six: String? = null,
    var seven: String? = null,
    var eight: String? = null,
    var nine: String? = null,

    var top: Char? = null,
    var topLeft: Char? = null,
    var topRight: Char? = null,
    var center: Char? = null,
    var bottomLeft: Char? = null,
    var bottomRight: Char? = null,
    var bottom: Char? = null
) {
    fun parse(input: List<String>) {
        println("Parsing $input")
        one = input.find { it.length == 2 }!!.sort()
        seven = input.find { it.length == 3 }!!.sort()
        four = input.find { it.length == 4 }!!.sort()
        eight = input.find { it.length == 7 }!!.sort()

        top = seven!!.find { !one!!.contains(it) }
        center = four!!.find { input.filter { it.length == 5 }.all { lengthFive -> lengthFive.contains(it) } }

        zero = input.find { it.length == 6 && !it.contains(center!!) }!!.sort()

        bottomRight = one!!.find { candidate -> input.filter { lengthSix -> lengthSix.length == 6 && lengthSix != zero }.all { it.contains(candidate) } }

        topRight = one!!.find { it != bottomRight }

        six = input.find { it.length == 6 && !it.contains(topRight!!) }!!.sort()
        five = input.find { it.length == 5 && !it.contains(topRight!!) }!!.sort()

        bottomLeft = six!!.find { !five!!.contains(it) }
        three = input.find { it.length == 5 && it.sort() != five && !it.contains(bottomLeft!!) }!!.sort()
        two = input.find { it.length == 5 && it.sort() != five && it.contains(bottomLeft!!) }!!.sort()

        nine = input.find { it.length == 6 && it.sort() != six && !it.contains(bottomLeft!!)}!!.sort()

        printOut()
    }

    fun computeValue(input: List<String>): Int {
        println("Computing value for $input")
        val ret = input.filter { it.isNotEmpty() }.joinToString("") {
            when (it.sort()) {
                zero -> "0"
                one -> "1"
                two -> "2"
                three -> "3"
                four -> "4"
                five -> "5"
                six -> "6"
                seven -> "7"
                eight -> "8"
                nine -> "9"
                else -> error(it)
            }
        }.toInt()
        println("Computed value = $ret")
        return ret
    }

    private fun printOut() {
        println("top = $top")
        println("topRight = $topRight")
        println("topLeft = $topLeft")
        println("center = $center")
        println("bottom = $bottom")
        println("bottomLeft = $bottomLeft")
        println("bottomRight = $bottomRight")

        println("0 = $zero")
        println("1 = $one")
        println("2 = $two")
        println("3 = $three")
        println("4 = $four")
        println("5 = $five")
        println("6 = $six")
        println("7 = $seven")
        println("8 = $eight")
        println("9 = $nine")
    }
}

fun String.sort() = toCharArray().sorted().joinToString("")
