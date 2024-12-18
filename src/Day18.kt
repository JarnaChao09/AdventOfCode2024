fun main() {
    val input = readInput("input/Day18").map {
        val (r, c) = it.split(",").map(String::toInt)

        r to c
    }

    val dirs = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

    fun Set<Pair<Int, Int>>.maze(): Int {
        var startR = 0
        var startC = 0

        val endR = 70
        val endC = 70

        val seen = mutableSetOf<Pair<Int, Int>>()
        val queue = ArrayDeque<Triple<Int, Int, Int>>().apply {
            addFirst(Triple(startR, startC, 0))
        }

        while (queue.isNotEmpty()) {
            val (currR, currC, currD) = queue.removeFirst()

            if (currR to currC in seen) {
                continue
            }

            if (currR == endR && currC == endC) {
                return currD
            }

            for ((dr, dc) in dirs) {
                val newR = currR + dr
                val newC = currC + dc
                if (newR in 0..70 && newC in 0..70 && newR to newC !in seen && newR to newC !in this) {
                    queue.addLast(Triple(newR, newC, currD + 1))
                }
            }

            seen.add(currR to currC)
        }

        return -1
    }

    val part1 = run {
        input.take(1024).toSet().maze()
    }
    part1.println()

    val part2 = run {
        var lower = 1024
        var upper = input.size

        while (upper > lower + 1) {
            var m = (upper + lower) / 2

            if (input.take(m).toSet().maze() != -1) {
                lower = m
            } else {
                upper = m
            }
        }

        input[lower]
    }
    part2.println()
}