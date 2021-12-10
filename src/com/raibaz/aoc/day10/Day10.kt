package com.raibaz.aoc.day10

import readInput
import java.util.Stack
import kotlin.math.ceil

class CorruptedStringException(val char: Char): RuntimeException()

fun main() {
    val input = readInput("com/raibaz/aoc/day10/Day10")

    // part1(input)
    part2(input)
}

fun part1(input: List<String>) {
    val corruptedCount = input.map {
        it.getFirstIllegal()
    }
    .filter { it.isFailure }
    .sumOf {
        when ((it.exceptionOrNull() as CorruptedStringException?)?.char) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        } as Int
    }

    println(corruptedCount)
}

fun part2(input: List<String>) {
    val scores = input.map {
        it.getFirstIllegal()
    }
    .filter { it.isSuccess }
    .map {
        val completion = it.getOrDefault(Stack()).countCompletion()
        completion
    }.sorted()

    println(scores[ceil((scores.size / 2).toDouble()).toInt()])

}

fun String.getFirstIllegal(): Result<Stack<Char>> {
    val stack = Stack<Char>()
    forEach {
        when(it) {
            '(' -> stack.push(it)
            '<' -> stack.push(it)
            '{' -> stack.push(it)
            '[' -> stack.push(it)
            ')' -> if(stack.peek() != '(') return Result.failure(CorruptedStringException(it)) else stack.pop()
            '>' -> if(stack.peek() != '<') return Result.failure(CorruptedStringException(it)) else stack.pop()
            ']' -> if(stack.peek() != '[') return Result.failure(CorruptedStringException(it)) else stack.pop()
            '}' -> if(stack.peek() != '{') return Result.failure(CorruptedStringException(it)) else stack.pop()
            else -> error("Illegal character $it")
        }
    }
    return Result.success(stack)
}

fun Stack<Char>.countCompletion(): Long {
    return map {
        when(it) {
            '(' -> 1
            '[' -> 2
            '{' -> 3
            '<' -> 4
            else -> error("Illegal character $it")
        }
    }.reversed().fold(0) { acc, i ->
        (acc * 5) + i
    }
}
