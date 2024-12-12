fun main() {
    val input = readInput("input/Day12").map(String::toList)

    fun List<List<Char>>.safeMatIndex(r: Int, c: Int): Char? {
        return if (r !in this.indices || c !in this[r].indices) {
            null
        } else {
            this[r][c]
        }
    }

    fun neighbors(currR: Int, currC: Int): List<Pair<Int, Int>> {
        return listOf(
            (currR) to (currC - 1),
            (currR) to (currC + 1),
            (currR + 1) to (currC),
            (currR - 1) to (currC),
        )
    }

    fun calculateAreaAndPerimeter(r: Int, c: Int, globalVisited: MutableSet<Pair<Int, Int>>): Pair<Int, Int> {
        val visited = mutableSetOf<Pair<Int, Int>>()
        var perimeter = 0

        val original = input[r][c]

        fun search(currR: Int, currC: Int) {
            if (currR !in input.indices || currC !in input[0].indices) {
                return
            }

            val current = input[currR][currC]

            if (current != original) {
                return
            }

            if (!visited.add(currR to currC)) {
                return
            }

            val neighbors = listOf(
                (currR - 1) to (currC),
                (currR + 1) to (currC),
                (currR) to (currC - 1),
                (currR) to (currC + 1),
            )

            perimeter += neighbors.count { (nr, nc) ->
                input.safeMatIndex(nr, nc) != original
            }

            search(currR - 1, currC)
            search(currR + 1, currC)
            search(currR, currC - 1)
            search(currR, currC + 1)
        }

        search(r, c)

        globalVisited.addAll(visited)

        return visited.size to perimeter
    }

    fun calculateAreaAndSides(r: Int, c: Int, globalVisited: MutableSet<Pair<Int, Int>>): Pair<Int, Int> {
        val visited = mutableSetOf<Pair<Int, Int>>()
        var edges = 0
        val edgeVisited = mutableSetOf<Triple<Int, Int, Int>>()

        val original = input[r][c]

        val queue = ArrayDeque<Pair<Int, Int>>()

        queue.addFirst(r to c)

        while (queue.isNotEmpty()) {
            val (currR, currC) = queue.removeFirst()

            if (currR !in input.indices || currC !in input[0].indices) {
                continue
            }

            if (!visited.add(currR to currC)) {
                continue
            }

            val neighbors = neighbors(currR, currC)

            for (dir in neighbors.indices) {
                val (nr, nc) = neighbors[dir]
                val neighbor = input.safeMatIndex(nr, nc)
                if (neighbor == null || neighbor != original) {
                    edges++

                    edgeVisited.add(Triple(dir, nr, nc))

                    for (secondaryNeighbor in neighbors(nr, nc)) {
                        val (snr, snc) = secondaryNeighbor
                        if (Triple(dir, snr, snc) in edgeVisited) {
                            edges--
                        }
                    }
                } else {
                    queue.addFirst(nr to nc)
                }
            }
        }

        globalVisited.addAll(visited)

        return visited.size to edges
    }

    val part1 = run {
        var total = 0
        val visited = mutableSetOf<Pair<Int, Int>>()
        for (r in input.indices) {
            for (c in input[0].indices) {
                if (r to c in visited) {
                    continue
                }
                val (area, perimeter) = calculateAreaAndPerimeter(r, c, visited)

                total += area * perimeter
            }
        }

        total
    }
    part1.println()

    val part2 = run {
        var total = 0
        val visited = mutableSetOf<Pair<Int, Int>>()
        for (r in input.indices) {
            for (c in input[0].indices) {
                if (r to c in visited) {
                    continue
                }
                val (area, side) = calculateAreaAndSides(r, c, visited)
                
                total += area * side
            }
        }

        total
    }
    part2.println()
}