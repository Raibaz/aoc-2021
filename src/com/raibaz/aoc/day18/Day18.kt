package com.raibaz.aoc.day18

import readInput
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

sealed interface Token {
    object Open : Token
    object Close : Token
    object Separator : Token // Separators are not really needed, but they make testing and debugging easier
    data class Value(val value: Int) : Token

    companion object {
        fun tokenFor(char: Any) = when (char) {
            '[' -> Open
            ']' -> Close
            ',' -> Separator
            else -> when (char) {
                is Char -> Value(char.digitToInt())
                is Int -> Value(char)
                else -> error(char)
            } // Allows for chars with value higher than 10 (used in a few tests)
        }

        fun stringify(token: Token): String {
            return when (token) {
                Close -> "]"
                Open -> "["
                Separator -> ","
                is Value -> "${token.value}"
            }
        }
    }
}

typealias Number = LinkedList<Token>

fun Number.stringify() = joinToString("") { Token.stringify(it) }
fun parseNumber(string: String) = LinkedList(string.map { Token.tokenFor(it) }).mergeDigits()

fun Number.mergeDigits(): Number {
    val toRemove = mutableListOf<Int>()
    for (i in (0 until (lastIndex - 1))) {
        val cur = this[i]
        val next = this[i+1]
        if (cur is Token.Value && next is Token.Value) {
            this[i] = Token.Value("${cur.value.digitToChar()}${next.value.digitToChar()}".toInt())
            toRemove.add(i+1)
        }
    }
    toRemove.reversed().forEach { removeAt(it) }
    return this
}

fun Number.addToFirstRegularNumber(value: Int, range: IntProgression) {
    for(i in range) {
        val cur = this[i]
        if (cur is Token.Value) {
            this[i] = Token.Value(cur.value + value)
            return
        }
    }
}

fun Number.explode(): Boolean {
    var depth = 0
    for (i in indices) {
        when (this[i]) {
            Token.Open -> depth++
            Token.Close -> depth--
            else -> Unit
        }

        if (depth == 5) {
            val left = (this[i+1] as Token.Value).value
            addToFirstRegularNumber(left, i - 1 downTo 0)

            val right = (this[i+3] as Token.Value).value
            addToFirstRegularNumber(right, i + 5 until size)

            repeat(5) { removeAt(i) }
            add(i, Token.Value(0))
            return true
        }
    }
    return false
}

fun Number.split(): Boolean {
    for (i in indices) {
        val cur = this[i]
        if (cur is Token.Value && cur.value >= 10) {
            this.remove(cur)
            this.add(i, Token.Close)
            this.add(i, Token.Value(ceil((cur.value.toDouble() / 2)).toInt()))
            this.add(i, Token.Separator)
            this.add(i, Token.Value(floor((cur.value.toDouble() / 2)).toInt()))
            this.add(i, Token.Open)
            return true
        }
    }
    return false
}

infix operator fun Number.plus(other: Number): Number {
    val ret = Number()
    ret.add(Token.Open)
    this.forEach {
        ret.add(it)
    }
    ret.add(Token.Separator)
    other.forEach {
        ret.add(it)
    }
    ret.add(Token.Close)
    return ret.reduce()
}

fun Number.reduce(): Number {
    while(explode() || split()) {
        // Do nothing
    }

    return this
}

fun Number.magnitude(): Int {
    val stack = Stack<MutableList<Int>>()
    for (i in indices) {
        val cur = this[i]
        when(cur) {
            Token.Open -> stack.push(mutableListOf())
            Token.Separator -> Unit
            Token.Close -> {
                val (left, right) = stack.pop()
                val magnitude = 3 * left + 2 * right
                if (stack.isEmpty()) {
                    return magnitude
                }
                stack.peek().add(magnitude)
            }
            is Token.Value -> stack.peek().add(cur.value)
        }
    }
    return -1
}

fun main() {
    val numbers = readInput("com/raibaz/aoc/day18/Day18").map {
        parseNumber(it)
    }

    // part1(numbers)
    part2(numbers)
}

fun part1(numbers: List<Number>) {
    var cur = numbers.first()
    numbers.drop(1).forEach {
        cur = cur + it
        cur.reduce()
    }

    println(cur.stringify())
    println(cur.magnitude())
}

fun part2(numbers: List<Number>) {
    var maxMagnitude = 0
    for (i in numbers.indices) {
        for (j in numbers.indices) {
            if (i == j) continue

            val curMagnitude = (numbers[i] + numbers[j]).magnitude()
            if (curMagnitude > maxMagnitude) {
                maxMagnitude = curMagnitude
            }
        }
    }
    println(maxMagnitude)
}