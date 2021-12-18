package com.raibaz.aoc.day17

import kotlin.math.abs

data class Target(
    val xMin: Int,
    val xMax: Int,
    val yMin: Int,
    val yMax: Int
)

class Velocity(
    val x: Int,
    val y: Int
)

data class Point(
    val x: Int,
    val y: Int
)

enum class TrajectoryOutcome {
    FALLS_INTO_TARGET,
    TOO_LONG,
    TOO_SHORT
}

data class Trajectory(
    val initialVelocity: Velocity,
    val steps : MutableList<Point> = mutableListOf(Point(0, 0)),
    var currentVelocity: Velocity = initialVelocity,
    var outcome: TrajectoryOutcome = TrajectoryOutcome.TOO_LONG
)

fun main() {
    // runPart1(Target(20, 30, -10, -5), 45)
    // runPart1(Target(155, 182, -117, -67), null)

    runPart2(Target(20, 30, -10, -5), 112)
    runPart2(Target(155, 182, -117, -67), null)
}

fun runPart1(target: Target, expectedResult: Int?) {
    val result = computeMaxHeightAndCount(target)
    if (expectedResult == null || result.first == expectedResult) {
        println("Ok, result = $result")
    } else {
        error("Result = $result, expected result = $expectedResult")
    }
}

fun runPart2(target: Target, expectedResult: Int?) {
    val result = computeMaxHeightAndCount(target)
    if (expectedResult == null || result.second == expectedResult) {
        println("Ok, result = $result")
    } else {
        error("Result = $result, expected result = $expectedResult")
    }
}

fun computeMaxHeightAndCount(target: Target): Pair<Int, Int> {
    val trajectory = Trajectory(Velocity(7, 8))
    trajectory.computeSteps(target)


    var maxHeight = 0
    var goodTrajectoriesCount = 0
    (1..target.xMax).forEach { x ->
        (target.yMin..abs(target.yMin)).forEach { y ->
            val trajectory = Trajectory(Velocity(x, y))
            trajectory.computeSteps(target)
            if (trajectory.outcome == TrajectoryOutcome.FALLS_INTO_TARGET) {
                println("${trajectory.initialVelocity.x}, ${trajectory.initialVelocity.y}")
                goodTrajectoriesCount++
                if (trajectory.maxHeight() > maxHeight) {
                    maxHeight = trajectory.maxHeight()
                }
            }
        }
    }
    return maxHeight to goodTrajectoriesCount
    /*do {


        val nextInitialVelocity = when (trajectory.outcome) {
            TrajectoryOutcome.FALLS_INTO_TARGET -> {
                goodTrajectoriesCount++
                Velocity(trajectory.initialVelocity.x, trajectory.initialVelocity.y + 1)
            }
            TrajectoryOutcome.TOO_SHORT -> Velocity(trajectory.initialVelocity.x, trajectory.initialVelocity.y + 1)
            TrajectoryOutcome.TOO_LONG -> Velocity(trajectory.initialVelocity.x - 1, trajectory.initialVelocity.y - 1)
        }
        if (nextInitialVelocity.x < 0 || nextInitialVelocity.y < target.yMin) {
            break
        }
        trajectory = Trajectory(nextInitialVelocity)
    } while(true)*/
}

fun Point.fallsInto(target: Target) = x <= target.xMax && x >= target.xMin && y >= target.yMin && y <= target.yMax

fun Trajectory.computeSteps(target: Target) {
    while(!steps.last().fallsInto(target)) {
        val lastStep = steps.last()
        val nextStep = Point(lastStep.x + currentVelocity.x, lastStep.y + currentVelocity.y)
        if (nextStep.x > target.xMax) {
            outcome = TrajectoryOutcome.TOO_LONG
            return
        }

        if (nextStep.y < target.yMin) {
            outcome = TrajectoryOutcome.TOO_SHORT
            return
        }

        steps.add(nextStep)
        currentVelocity = Velocity(
            if (currentVelocity.x > 0) {
                currentVelocity.x - 1
            } else if (currentVelocity.x < 0) {
                currentVelocity.x + 1
            } else { 0 },
            currentVelocity.y - 1
        )
    }
    outcome = TrajectoryOutcome.FALLS_INTO_TARGET
}

fun Trajectory.maxHeight() = steps.maxOf { it.y }