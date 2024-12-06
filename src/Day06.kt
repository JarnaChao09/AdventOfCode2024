fun main() {
    val input = readInput("input/Day06").map(String::toList)

    val start = run {
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] == '^') {
                    return@run i to j
                }
            }
        }

        error("start not found")
    }

    val directions= listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)

    fun List<List<Char>>.isLoop(): Boolean {
        var (r, c) = start
        var dir = 0

        val arrived = mutableSetOf<Triple<Int, Int, Int>>()

        while (r in this.indices && c in this[0].indices) {
            if (!arrived.add(Triple(r, c, dir))) {
                return true
            }

            val (dirR, dirC) = directions[dir]

            if (r + dirR !in this.indices || c + dirC !in this.indices) {
                break
            }

            val front = this[r + dirR][c + dirC]

            if (front == '#') {
                dir = (dir + 1).mod(directions.size)
            } else {
                r += dirR
                c += dirC
            }
        }

        return false
    }

    val (part1, part2) = run {
        var (r, c) = start
        var dir = 0

        val arrived = mutableSetOf<Pair<Int, Int>>(r to c)
        // val arrivedDir = mutableSetOf<Triple<Int, Int, Int>>(Triple(r, c, dir))

        // var obstructions = 0

        while (r in input.indices && c in input[0].indices) {
            arrived.add(r to c)

            // val (turnedR, turnedC) = directions[(dir + 1).mod(directions.size)]

            // if (Triple(r + turnedR, c + turnedC, (dir + 1).mod(directions.size)) in arrivedDir) {
            //     obstructions++
            // }

            // arrivedDir.add(Triple(r, c, dir))
            val (dirR, dirC) = directions[dir]

            if (r + dirR !in input.indices || c + dirC !in input.indices) {
                break
            }

            val front = input[r + dirR][c + dirC]

            if (front == '#') {
                dir = (dir + 1).mod(directions.size)
                // arrivedDir.add(Triple(r, c, dir))
            } else {
                r += dirR
                c += dirC
            }
        }

        var obstructions = 0

        for ((r, c) in arrived) {
            if (input[r][c] != '.') {
                continue
            }

            val copy = buildList {
                repeat(input.size) { ir ->
                    add(buildList {
                        repeat(input[ir].size) { ic ->
                            add(if (ir == r && ic == c) '#' else input[ir][ic])
                        }
                    })
                }
            }
            if (copy.isLoop()) {
                obstructions++
            }
        }

        arrived.size to obstructions
    }
    part1.println()
    part2.println()
}