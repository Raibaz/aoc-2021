package com.raibaz.aoc.day19

import readInput
import kotlin.math.abs

data class Point(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun rotations() = setOf(
        Point(x, y, z), Point(x, -z, y), Point(x, -y, -z), Point(x, z, -y), Point(-x, -y, z),
        Point(-x, -z, -y), Point(-x, y, -z), Point(-x, z, y), Point(-z, x, -y), Point(y, x, -z),
        Point(z, x, y), Point(-y, x, z), Point(z, -x, -y), Point(y, -x, z), Point(-z, -x, y),
        Point(-y, -x, -z), Point(-y, -z, x), Point(z, -y, x), Point(y, z, x), Point(-z, y, x),
        Point(z, y, -x), Point(-y, z, -x), Point(-z, -y, -x), Point(y, -z, -x),
    )

    infix operator fun minus(other: Point) = Point(x - other.x, y - other.y, z - other.z)
    infix operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
    infix fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

}

fun List<Set<Point>>.transpose(): List<Set<Point>> {
    return if(all { it.isNotEmpty() }) {
        listOf(map { it.first() }.toSet()) + map { it.drop(1).toSet() }.transpose()
    } else {
        emptyList()
    }
}

class Scanner(
    val id: Int,
    val beacons: MutableList<Point> = mutableListOf(),
    val position: Point = Point(0, 0, 0)
) {
    private fun allRotations() = beacons.map(Point::rotations).transpose().map { Scanner(id, it.toMutableList())}

    fun findRelativePosition(other: Scanner): Scanner? {
        return other.allRotations().firstNotNullOfOrNull { reoriented ->
            beacons.firstNotNullOfOrNull { first ->
                reoriented.beacons.firstNotNullOfOrNull { second ->
                    val otherPosition = first - second
                    val otherTransformed = reoriented.beacons.map { it + otherPosition }.toSet()
                    if ((otherTransformed intersect beacons.toSet()).size >= 12) {
                        Scanner(other.id, otherTransformed.toMutableList(), otherPosition)
                    } else {
                        null
                    }
                }
            }
        }
    }
}

class ComputedMap(
    val beacons: Set<Point>,
    val scanners: Set<Point>
) {
    fun maxDistance(): Int {
        var maxDistance = 0

        val scannersList = scanners.toList()
        for (i in scannersList.indices) {
            for (j in scannersList.indices) {
                if (i == j) continue
                val distance = scannersList[i] distanceTo scannersList[j]
                if (distance > maxDistance) {
                    maxDistance = distance
                }
            }
        }

        return maxDistance
    }
}

fun computeMap(scanners: List<Scanner>): ComputedMap {
    val beacons = scanners.first().beacons.toMutableSet()
    val foundScannerPositions = mutableSetOf(scanners.first().position)

    val remaining = ArrayDeque<Scanner>().apply { addAll(scanners.drop(1)) }
    while (remaining.isNotEmpty()) {
        val cur = remaining.removeFirst()
        println("Processing scanner ${cur.id}...")
        val transformed = Scanner(0, beacons.toMutableList()).findRelativePosition(cur)
        if (transformed == null) {
            remaining.add(cur)
        } else {
            beacons.addAll(transformed.beacons)
            foundScannerPositions.add(transformed.position)
        }
        println("Done with scanner ${cur.id}, still ${remaining.size} to go...")
    }
    return ComputedMap(beacons, foundScannerPositions)
}

fun main() {
    val scanners = mutableListOf<Scanner>()
    readInput("com/raibaz/aoc/day19/Day19").filter(String::isNotBlank).forEach {
        when {
            it.matches("--- scanner \\d+ ---".toRegex()) -> {
                val id = it.replace("--- scanner", "").replace(" ---", "").trim().toInt()
                scanners.add(Scanner(id))
            }
            else -> {
                val split = it.split(",").map(String::toInt)
                scanners.last().beacons.add(Point(split[0], split[1], split[2]))
            }
        }
    }

    val map = computeMap(scanners)
    println(map.beacons.size)

    println(map.maxDistance())
}