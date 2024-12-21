data class State(val currR: Int, val currC: Int, val destR: Int, val destC: Int, val nRobot: Int)

val cache = mutableMapOf<State, Long>()

fun cheapestDirPad(currR: Int, currC: Int, destR: Int, destC: Int, nRobots: Int): Long {
    val state = State(currR, currC, destR, destC, nRobots)

    if (state in cache) {
        return cache[state]!!
    }

    var ret = Long.MAX_VALUE
    val queue = ArrayDeque<Triple<Int, Int, String>>().apply {
        addLast(Triple(currR, currC, ""))
    }

    while (queue.isNotEmpty()) {
        val (vr, vc, vpresses) = queue.removeFirst()

        if (vr == destR && vc == destC) {
            val rec = cheapestRobot(vpresses + "A", nRobots - 1)
            ret = minOf(ret, rec)
            continue
        }

        if (vr == 0 && vc == 0) {
            continue
        } else {
            if (vr < destR) {
                queue.addLast(Triple(vr + 1, vc, vpresses + "v"))
            } else if (vr > destR) {
                queue.addLast(Triple(vr - 1, vc, "$vpresses^"))
            }
            if (vc < destC) {
                queue.addLast(Triple(vr, vc + 1, "$vpresses>"))
            } else if (vc > destC) {
                queue.addLast(Triple(vr, vc - 1, "$vpresses<"))
            }
        }
    }

    cache[state] = ret
    return ret
}

fun cheapestRobot(presses: String, nRobots: Int): Long {
    if (nRobots == 1) {
        return presses.length.toLong()
    }

    var ret = 0L
    val pad = listOf(
        listOf('X', '^', 'A'),
        listOf('<', 'v', '>'),
    )

    var currR = 0
    var currC = 2

    for (i in presses.indices) {
        for (nextR in pad.indices) {
            for (nextC in pad[0].indices) {
                if (pad[nextR][nextC] == presses[i]) {
                    ret += cheapestDirPad(currR, currC, nextR, nextC, nRobots)
                    currR = nextR
                    currC = nextC
                }
            }
        }
    }

    return ret
}

fun cheapest(currR: Int, currC: Int, destR: Int, destC: Int, nRobots: Int): Long {
    var ret = Long.MAX_VALUE

    val queue = ArrayDeque<Triple<Int, Int, String>>().apply {
        addLast(Triple(currR, currC, ""))
    }

    while (queue.isNotEmpty()) {
        val (vr, vc, vpresses) = queue.removeFirst()

        if (vr == destR && vc == destC) {
            val rec = cheapestRobot(vpresses + "A", nRobots)
            ret = minOf(ret, rec)
            continue
        }

        if (vr == 3 && vc == 0) {
            continue
        } else {
            if (vr < destR) {
                queue.addLast(Triple(vr + 1, vc, vpresses + "v"))
            } else if (vr > destR) {
                queue.addLast(Triple(vr - 1, vc, "$vpresses^"))
            }
            if (vc < destC) {
                queue.addLast(Triple(vr, vc + 1, "$vpresses>"))
            } else if (vc > destC) {
                queue.addLast(Triple(vr, vc - 1, "$vpresses<"))
            }
        }
    }

    return ret
}

fun main() {
    val input = readInput("input/Day21")

    val pad = listOf(
        listOf('7', '8', '9'),
        listOf('4', '5', '6'),
        listOf('1', '2', '3'),
        listOf('X', '0', 'A'),
    )

    val (part1, part2) = run {
        input.fold(0L to 0L) { acc, it ->
            var currR = 3
            var currC = 2

            var ret1 = 0L
            var ret2 = 0L

            for (i in it.indices) {
                for (nextR in pad.indices) {
                    for (nextC in pad[0].indices) {
                        if (pad[nextR][nextC] == it[i]) {
                            ret1 += cheapest(currR, currC, nextR, nextC, 3)
                            ret2 += cheapest(currR, currC, nextR, nextC, 26)
                            currR = nextR
                            currC = nextC
                        }
                    }
                }
            }

            val code = it.dropLast(1).toLong()
            (acc.first + ret1 * code) to (acc.second + ret2 * code)
        }
    }
    part1.println()
    part2.println()
}