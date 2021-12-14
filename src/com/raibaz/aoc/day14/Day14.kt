package com.raibaz.aoc.day14

import readInput
import kotlin.math.max

typealias PairCounter = MutableMap<String, Long>
typealias CharCounter = MutableMap<Char, Long>

fun main() {
    runTest("Day14_test", 10, 1588)
    runTest("Day14", 10)

    runTest("Day14_test", 40, 2188189693529)
    runTest("Day14", 40)
}

fun runTest(fileName: String, iterations: Int, expectedValue: Long? = null) {
    val input = readInput("com/raibaz/aoc/day14/$fileName")

    val template = input.first()
    val rules = input.drop(2).map {
        val split = it.split(" -> ")
        split[0] to split[1][0]
    }

    var pairs = countPairs(template)
    var chars = countChars(template)
    (1..iterations).forEach {
        val (nextPairs, nextChars) = process(rules, pairs, chars)
        pairs = nextPairs
        chars = nextChars
        println("After step $it: $pairs, $chars, strlen = ${chars.values.sumOf { l -> l }}")
    }

    val max = chars.maxOf { it.value }
    val min = chars.minOf { it.value }

    val result = max - min
    if (expectedValue == null || result == expectedValue) {
        println("Ok, result = $result")
    } else {
        error(result)
    }
}

fun countPairs(template: String): PairCounter = template.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap()
fun countChars(template: String): CharCounter = template.groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap()

fun process(
    rules: List<Pair<String, Char>>,
    pairs: PairCounter,
    chars: CharCounter
): Pair<PairCounter, CharCounter> {
    val currentPairs = pairs.filter { it.value > 0 }
    currentPairs.forEach { (pair, increment) ->
        val rule = rules.find { it.first == pair }
        if (rule != null) {
            chars[rule.second] = chars.getOrDefault(rule.second, 0) + increment
            val firstPair = "${pair[0]}${rule.second}"
            pairs[firstPair] = pairs.getOrDefault(firstPair, 0) + increment
            val secondPair = "${rule.second}${pair[1]}"
            pairs[secondPair] = pairs.getOrDefault(secondPair, 0) + increment
            pairs[pair] = max(pairs.getOrDefault(pair, 1) - increment, 0)
        }
    }

    return pairs to chars
}
