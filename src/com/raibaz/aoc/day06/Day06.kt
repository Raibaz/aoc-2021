package com.raibaz.aoc.day06

import readInput

fun main() {
    val input = readInput("com/raibaz/aoc/day06/Day06")
    val school = initSchool(input.first().split(",").map { it.toInt() })
    val iterations = 256

    (0..iterations).forEach {
        println("$it: ${school.size()}")
        school.day()
    }

    println(school.size())
}

fun initSchool(input: List<Int>): School {
    val school = School()
    input.forEach {
        when(it) {
            0 -> school.zero++
            1 -> school.one++
            2 -> school.two++
            3 -> school.three++
            4 -> school.four++
            5 -> school.five++
            6 -> school.six++
            7 -> school.seven++
            8 -> school.eight++
        }
    }

    return school
}

class School(
    var zero: Long = 0,
    var one: Long = 0,
    var two: Long = 0,
    var three: Long = 0,
    var four: Long = 0,
    var five: Long = 0,
    var six: Long = 0,
    var seven: Long = 0,
    var eight: Long = 0,
) {
    fun day() {
        val prevZero = zero
        zero = one
        one = two
        two = three
        three = four
        four = five
        five = six
        six = seven
        seven = eight
        eight = prevZero
        six += prevZero
    }

    fun size(): Long = zero + one + two + three + four + five + six + seven + eight
}
