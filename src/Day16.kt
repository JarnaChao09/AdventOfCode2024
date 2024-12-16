import java.util.PriorityQueue

fun main() {
    val input = readInput("input/Day16").map(String::toList)

    var sr = 0
    var sc = 0
    var er = 0
    var ec = 0

    for (i in input.indices) {
        for (j in input[0].indices) {
            if (input[i][j] == 'S') {
                sr = i
                sc = j
            }

            if (input[i][j] == 'E') {
                er = i
                ec = j
            }
        }
    }

    val dirs = listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)

    val part1 = run {
        val queue = PriorityQueue<Triple<Pair<Int, Int>, Int, Int>>(compareBy { it.third })
        queue.add(Triple(sr to sc, 1, 0))

        val seen = mutableMapOf<Triple<Int, Int, Int>, Int>()

        while (queue.isNotEmpty()) {
            val (p, dir, s) = queue.poll()
            val (pr, pc) = p

            if (pr == er && pc == ec) {
                return@run s
            }

            val state = Triple(pr, pc, dir)
            if (state in seen && seen[state]!! < s ) {
                continue
            }
            seen[state] = s

            val (dr, dc) = dirs[dir]
            val nr = pr + dr
            val nc = pc + dc
            if (nr in input.indices && nc in input[0].indices && input[nr][nc] != '#') {
                queue.add(Triple(nr to nc, dir, s + 1))
            }
            queue.add(Triple(pr to pc, (dir + 1).mod(4), s + 1000))
            queue.add(Triple(pr to pc, (dir + 3).mod(4), s + 1000))
        }

        return@run 0
    }
    part1.println()

    val part2 = run {
        val queue = PriorityQueue<Triple<List<Pair<Int, Int>>, Int, Int>>(compareBy { it.third })
        queue.add(Triple(listOf(sr to sc), 1, 0))

        var min = Int.MAX_VALUE
        val best = mutableSetOf<Pair<Int, Int>>()

        val seen = mutableMapOf<Triple<Int, Int, Int>, Int>()

        while (queue.isNotEmpty()) {
            val (p, dir, s) = queue.poll()
            val (pr, pc) = p.last()

            if (pr == er && pc == ec) {
                if (s <= min) {
                    min = s
                } else {
                    return@run best.size
                }
                best.addAll(p)
            }

            val state = Triple(pr, pc, dir)
            if (state in seen && seen[state]!! < s) {
                continue
            }
            seen[state] = s

            val (dr, dc) = dirs[dir]
            val nr = pr + dr
            val nc = pc + dc
            if (nr in input.indices && nc in input[0].indices && input[nr][nc] != '#') {
                queue.add(Triple(p + (nr to nc), dir, s + 1))
            }
            queue.add(Triple(p, (dir + 1).mod(4), s + 1000))
            queue.add(Triple(p, (dir + 3).mod(4), s + 1000))
        }

        return@run 0
    }
    part2.println()
}