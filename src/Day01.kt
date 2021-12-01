fun main() {
    fun part1(input: List<String>): Int {
        return countIncreases(input.map { it.toInt() })
    }

    fun part2(input: List<String>): Int {
        return countIncreasesWithSlidingWindow(input.map { it.toInt() }, 3)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 0)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

fun countIncreasesWithSlidingWindow(input: List<Int>, windowSize: Int): Int {
    var lastWindow = 0
    var count = 0
    input.drop(windowSize).forEachIndexed { index, i ->
        val currentWindow = (0..windowSize-1).sumOf {
            input[index + it]
        }

        if(lastWindow < currentWindow) {
            count++
        }
        lastWindow = currentWindow
    }
    return count
}

fun countIncreases(input: List<Int>) = countIncreasesWithSlidingWindow(input, 1)

fun countIncreasesWithListIterator(input: List<Int>): Int {

    if (input.isEmpty()) {
        return 0
    }

    val itr = input.listIterator()
    itr.next()
    var count = 0
    while(itr.hasNext()) {
        val prev = itr.previous()
        itr.next()
        val current = itr.next()
        if (prev < current) {
            count++
        }
    }

    return count
}

fun countIncreasesWithFold(input: List<Int>): Int {

    var prev = input.first().toInt()
    return input.drop(1).fold(0) { acc, current ->
        val prevPrev = prev
        prev = current
        if (prevPrev < current) {
            acc + 1
        } else {
            acc
        }
    }
}
