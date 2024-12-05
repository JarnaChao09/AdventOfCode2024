import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val input = Path("src/input/Day05.txt").readText().trim().split("\n\n")

    val (rules, updates) = input.map { it.split("\n") }

    val firstToSecond = mutableMapOf<Int, List<Int>>()

    for (rule in rules) {
        val (f, s) = rule.split("|").map(String::toInt)

        firstToSecond[f] = firstToSecond.getOrElse(f) { emptyList() } + s
    }

    /* old solution
    fun isOrdered(row: List<Int>): Boolean {
        var ordered = true
        for ((ruleKey, ruleValues) in firstToSecond) {
            for (ruleValue in ruleValues) {
                if (ruleKey in row && ruleValue in row) {
                    val ki = row.indexOf(ruleKey)
                    val kv = row.indexOf(ruleValue)

                    if (kv < ki) {
                        ordered = false
                    }
                }
            }
        }

        return ordered
    } */

    fun isOrdered(row: List<Int>): Boolean = row.zipWithNext().all { (a, b) -> b in (firstToSecond[a] ?: emptyList()) }

    val (part1, part2) = run {
        updates.fold(0 to 0) { (f, s), it ->
            val tmp = it.split(",").map(String::toInt)

            if (isOrdered(tmp)) {
                (f + tmp[tmp.size / 2]) to s
            } else {
                val stmp = tmp.sortedWith { x, y ->
                    if (x in firstToSecond && y in firstToSecond[x]!!) -1 else 1
                }
                f to (s + stmp[stmp.size / 2])
            }
        }
    }
    part1.println()
    part2.println()
}