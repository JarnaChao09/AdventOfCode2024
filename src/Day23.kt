fun Map<String, Set<String>>.findLargest(): Set<String> {
    var ret = setOf<String>()
    val nodes = this.keys.toList()

    fun isConnected(nodes: Set<String>): Boolean {
        return nodes.all {
            this[it]!!.containsAll(nodes - it)
        }
    }

    fun go(current: Set<String>, remaining: List<String>) {
        if (current.size > ret.size) {
            ret = current
        }

        remaining.forEachIndexed { i, node ->
            val new = current union setOf(node)

            if (isConnected(new)) {
                go(new, remaining.drop(i + 1))
            }
        }
    }

    go(emptySet(), nodes)

    return ret
}

fun main() {
    val input = readInput("input/Day23").map {
        val (l, r) = it.split("-")

        l to r
    }

    val graph = buildMap {
        input.forEach { (a, b) ->
            put(a, getOrElse(a) { emptySet<String>() } + b)
            put(b, getOrElse(b) { emptySet<String>() } + a)
        }
    }

    val part1 = run {
        val triangles = buildSet {
            for ((node, neighbors) in graph) {
                val neighbors = neighbors

                for (neighbor in neighbors) {
                    val commonNeighbors = neighbors intersect graph[neighbor]!!

                    for (commonNeighbor in commonNeighbors) {
                        add(listOf(node, neighbor, commonNeighbor).sorted())
                    }
                }
            }
        }

        triangles.sumOf {
            if (it.any { t -> t.startsWith("t") }) {
                1
            } else {
                0
            } as Int
        }
    }
    part1.println()

    val part2 = run {
        graph.findLargest().sorted().joinToString(",")
    }
    part2.println()
}