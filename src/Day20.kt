import kotlin.math.absoluteValue

fun main() {
    val input = readInput("input/Day20").map(String::toList)

    fun manhattanDistance(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
        return (a.first - b.first).absoluteValue + (a.second - b.second).absoluteValue
    }

    fun Map<Pair<Int, Int>, Long>.endpoints(coordinate: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val (r, c) = coordinate

        return buildSet {
            for (dr in -20..20) {
                val dcmax = 20 - dr.absoluteValue
                for (dc in (-dcmax)..dcmax) {
                    if ((r + dr) to (c + dc) in this@endpoints) {
                        add((r + dr) to (c + dc))
                    }
                }
            }
        }
    }

    val (startPosR, startPosC, endPosR, endPosC) = run {
        var sr = 0
        var sc = 0
        var er = 0
        var ec = 0

        for (i in input.indices) {
            for (j in input[0].indices) {
                when (input[i][j]) {
                    'S' -> {
                        sr = i
                        sc = j
                    }
                    'E' -> {
                        er = i
                        ec = j
                    }
                    else -> {}
                }
            }
        }

        listOf(sr, sc, er, ec)
    }

    val dirs = listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)

    val path = mutableMapOf<Pair<Int, Int>, Long>().apply {
        put(startPosR to startPosC, 0L)
    }
    var currR = startPosR
    var currC = startPosC
    var step = 0L
    while(currR != endPosR || currC != endPosC) {
        step++
        for ((dr, dc) in dirs) {
            val newR = currR + dr
            val newC = currC + dc

            if (newR to newC !in path && input[newR][newC] != '#') {
                currR = newR
                currC = newC
                path[currR to currC] = step
                break
            }
        }
    }

    val part1 = run {
        var ret = 0
        for ((point, pl) in path) {
            val (pr, pc) = point
            for ((dr, dc) in dirs) {
                val next = (pr + dr) to (pc + dc)
                val after = (pr + 2 * dr) to (pc + 2 * dc)

                if (next !in path && after in path && path[after]!! - pl >= 102) {
                    ret++
                }
            }
        }

        ret
    }
    part1.println()

    val part2 = run {
        var ret = 0
        for ((point, pl) in path) {
            val potential = path.endpoints(point)

            for (other in potential) {
                if (path[other]!! - path[point]!! - manhattanDistance(point, other).toLong() >= 100L) {
                    ret++
                }
            }
        }

        ret
    }
    part2.println()
}