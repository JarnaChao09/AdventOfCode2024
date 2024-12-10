fun main() {
    val input = readInput("input/Day10").map { it.toList().map(Char::digitToInt) }

    val starts = buildList {
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] == 0) {
                    add(i to j)
                }
            }
        }
    }

    val directions = listOf(
        -1 to  0, // up
         0 to  1, // right
         1 to  0, // down
         0 to -1, // left
    )

    fun search(curr: Pair<Int, Int>, currentTarget: Int, maxTarget: Int, onMaxTarget: (Pair<Int, Int>) -> Unit): Set<Pair<Int, Int>> {
        val (currR, currC) = curr

        if (currR !in input.indices || currC !in input[0].indices) {
            return emptySet()
        }

        if (input[currR][currC] == maxTarget && currentTarget == maxTarget) {
            val target = currR to currC
            onMaxTarget(target)
            return setOf(target)
        } else if (input[currR][currC] != currentTarget) {
            return emptySet()
        }

        val total = mutableSetOf<Pair<Int, Int>>()

        for ((dirR, dirC) in directions) {
            total += search((currR + dirR) to (currC + dirC), currentTarget + 1, maxTarget, onMaxTarget)
        }

        return total
    }

    val (part1, part2) = run {
        var total = 0
        val rating = buildMap {
            for (start in starts) {
                total += search(start, 0, 9) {
                    put(start, getOrElse(start) { 0 } + 1)
                }.size
            }
        }

        total to rating.values.sum()
    }
    part1.println()
    part2.println()
}