import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val input = Path("src/input/Day15.txt").readText().trim().split("\n\n")

    val map = input[0].split("\n").map(String::toMutableList).toMutableList()

    val instructions = input[1].split("\n").flatMap(String::toList)

    fun Char.toDirection(): Pair<Int, Int> {
        return when (this) {
            '<' ->  0 to -1
            '>' ->  0 to  1
            '^' -> -1 to  0
            'v' ->  1 to  0
            else -> error("unreachable")
        }
    }

    fun MutableList<MutableList<Char>>.debug() {
        this.forEach {
            println(it.joinToString(""))
        }
    }

    fun MutableList<MutableList<Char>>.calculate(c: Char): Long {
        var ret = 0L
        for (i in this.indices) {
            for (j in this[0].indices) {
                if (this[i][j] == c) {
                    ret += 100L * i.toLong() + j.toLong()
                }
            }
        }

        return ret
    }

    fun MutableList<MutableList<Char>>.double(): Pair<MutableList<MutableList<Char>>, Pair<Int, Int>> {
        val ret = mutableListOf<MutableList<Char>>()
        var newStart = Pair(0, 0)

        for (i in this.indices) {
            val tmp = mutableListOf<Char>()
            for (j in this[0].indices) {
                when (this[i][j]) {
                    '#' -> {
                        tmp.add('#')
                        tmp.add('#')
                    }
                    'O' -> {
                        tmp.add('[')
                        tmp.add(']')
                    }
                    '.' -> {
                        tmp.add('.')
                        tmp.add('.')
                    }
                    '@' -> {
                        tmp.add('@')
                        newStart = Pair(ret.size, tmp.lastIndex)
                        tmp.add('.')
                    }
                }
            }
            ret.add(tmp)
        }

        return ret to newStart
    }

    val start = run {
        for (r in map.indices) {
            for (c in map[0].indices) {
                if (map[r][c] == '@') {
                    return@run r to c
                }
            }
        }

        error("didn't find start")
    }

    val (newMap, newStart) = map.double()

    val part1 = run {
        var (currR, currC) = start

        for (instruction in instructions) {
            val (dirR, dirC) = instruction.toDirection()

            val nextR = currR + dirR
            val nextC = currC + dirC

            val front = map[nextR][nextC]

            if (front == '#') {
                continue
            } else if (front == '.') {
                map[nextR][nextC] = '@'
                map[currR][currC] = '.'
            } else if (front == 'O') {
                var cR = nextR
                var cC = nextC

                while (map[cR][cC] == 'O') {
                    cR += dirR
                    cC += dirC
                }

                if (map[cR][cC] == '#') {
                    continue
                } else if (map[cR][cC] == '.') {
                    map[cR][cC] = 'O'

                    map[nextR][nextC] = '@'
                    map[currR][currC] = '.'
                }
            }

            currR += dirR
            currC += dirC
        }

        map.calculate('O')
    }
    part1.println()
    
    val part2 = run {
        var (currR, currC) = newStart

        for (instruction in instructions) {
            val (dirR, dirC) = instruction.toDirection()

            var moveQueue = ArrayDeque<List<Triple<Int, Int, Char>>>().apply {
                addLast(listOf(Triple(currR, currC, '@')))
            }

            var canMove = true

            while (true) {
                val newQueue = ArrayDeque<List<Triple<Int, Int, Char>>>()
                var queuesDone = 0
                for (q in moveQueue) {
                    val qc = q.toMutableList()
                    val (currMR, currMC, _) = qc.last()
                    val nextMR = currMR + dirR
                    val nextMC = currMC + dirC

                    val front = newMap[nextMR][nextMC]

                    if (front == '.') {
                        newQueue.addLast(qc.toList())
                        queuesDone++
                    } else if (front == '[' && instruction in "^v") {
                        val nq = listOf(Triple(nextMR, nextMC + 1, ']'))
                        qc.add(Triple(nextMR, nextMC, '['))
                        newQueue.addLast(nq)
                        newQueue.addLast(qc.toList())
                    } else if (front == ']' && instruction in "^v") {
                        val nq = listOf(Triple(nextMR, nextMC - 1, '['))
                        qc.add(Triple(nextMR, nextMC, ']'))
                        newQueue.addLast(nq)
                        newQueue.addLast(qc.toList())
                    } else if (front in "[]") {
                        qc.add(Triple(nextMR, nextMC, front))
                        val nitem = newMap[nextMR + dirR][nextMC + dirC]
                        qc.add(Triple(nextMR + dirR, nextMC + dirC, nitem))
                        newQueue.addLast(qc.toList())
                    } else if (front == '#') {
                        newQueue.clear()
                        canMove = false
                        break
                    }
                }

                if (queuesDone == moveQueue.size || !canMove) {
                    break
                }

                moveQueue = newQueue
            }

            if (canMove) {
                val alreadyMovedTo = mutableSetOf<Pair<Int, Int>>()

                for (q in moveQueue) {
                    for ((posR, posC, item) in q.reversed()) {
                        if (item == '@') {
                            currR += dirR
                            currC += dirC
                        }

                        if (posR to posC !in alreadyMovedTo) {
                            newMap[posR][posC] = '.'
                        }
                        newMap[posR + dirR][posC + dirC] = item
                        alreadyMovedTo.add((posR + dirR) to (posC + dirC))
                    }
                }
            }
        }

        newMap.calculate('[')
    }
    part2.println()
}