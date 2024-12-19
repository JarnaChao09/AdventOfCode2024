import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val input = Path("src/input/Day19.txt").readText().trim().split("\n\n")

    val given = input[0].split(", ")
    val towels = input[1].split("\n")

    fun String.isPossible(strings: List<String>, cache: MutableMap<String, Boolean> = mutableMapOf()): Boolean {
        if (this in cache) {
            return cache[this]!!
        }

        if (this.isEmpty()) {
            return true
        }

        var ret = false
        for (string in strings) {
            if (this.startsWith(string)) {
                ret = ret || this.substring(string.length).isPossible(strings, cache)
            }
        }

        cache[this] = ret
        return ret
    }

    fun String.possibleCount(strings: List<String>, cache: MutableMap<String, Long> = mutableMapOf()): Long {
        if (this in cache) {
            return cache[this]!!
        }

        if (this.isEmpty()) {
            return 1
        }

        var ret = 0L
        for (string in strings) {
            if (this.startsWith(string)) {
                ret += this.substring(string.length).possibleCount(strings, cache)
            }
        }

        cache[this] = ret
        return ret
    }

    val part1 = run {
        towels.count {
            it.isPossible(given)
        }
    }
    part1.println()

    val part2 = run {
        towels.sumOf {
            it.possibleCount(given)
        }
    }
    part2.println()
}