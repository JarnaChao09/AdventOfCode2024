data class State22(val first: Long, val second: Long, val third: Long, val fourth: Long)

fun main() {
    val input = readInput("input/Day22").map(String::toLong)

    val (part1, part2) = run {
        val totalScores = mutableMapOf<State22, Long>()
        input.sumOf {
            val ret: Long
            val p = buildList {
                var tmp = it
                add(tmp.mod(10L))

                repeat(2000) {
                    tmp = ((tmp * 64L) xor tmp).mod(16777216L)
                    tmp = ((tmp / 32L) xor tmp).mod(16777216L)
                    tmp = ((tmp * 2048L) xor tmp).mod(16777216L)
                    add(tmp.mod(10L))
                }

                ret = tmp
            }

            val changes = p.zipWithNext().map { (l, r) -> r - l }

            val scores = mutableMapOf<State22, Long>()
            for (i in 0..<(changes.size - 3)) {
                val pattern = State22(changes[i], changes[i + 1], changes[i + 2], changes[i + 3])

                if (pattern !in scores) {
                    scores[pattern] = p[i + 4]
                }
            }

            for ((k, v) in scores) {
                totalScores[k] = totalScores.getOrElse(k) { 0L } + v
            }

            ret
        } to totalScores.values.max()
    }
    part1.println()
    part2.println()
}