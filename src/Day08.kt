import kotlin.math.absoluteValue

fun main() {
    val input = readInput("input/Day08").map(String::toList)

    val allNodes = mutableSetOf<Pair<Int, Int>>()
    val nodes = buildMap {
        for (i in input.indices) {
            for (j in input[0].indices) {
                val curr = input[i][j]

                if (curr != '.') {
                    put(curr, getOrElse(curr) { listOf<Pair<Int, Int>>() } + ((i + 1) to (j + 1)))
                    allNodes.add((i + 1) to (j + 1))
                }
            }
        }
    }
    
    val (part1, part2) = run {
        val total = mutableSetOf<Pair<Int, Int>>()
        val total2 = mutableSetOf<Pair<Int, Int>>()

        for (ri in input.indices) {
            for (ci in input[0].indices) {
                val r = ri + 1
                val c = ci + 1

                for ((freq, locations) in nodes) {
                    for (location in locations) {
                        val (locationR, locationC) = location
                        val slope1 = (locationR - r).toDouble() / (locationC - c).toDouble()

                        for (loc in locations) {
                            if (loc == location) {
                                continue
                            }

                            val (locR, locC) = loc
                            val slope2 = (locR - r).toDouble() / (locC - c).toDouble()
                            if (slope1 == slope2) {
                                if (r to c !in allNodes) {
                                    total2.add(r to c)
                                }
                                val dist1 = (locationR - r).absoluteValue + (locationC - c).absoluteValue
                                val dist2 = (locR - r).absoluteValue + (locC - c).absoluteValue
                                if (dist1 == dist2 * 2 || dist2 == dist1 * 2) {
                                    total.add(r to c)
                                }
                            }
                        }
                    }
                }
            }
        }
        total.size to (total2.size + allNodes.size)
    }
    part1.println()
    part2.println()
}