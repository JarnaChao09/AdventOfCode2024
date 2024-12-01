import kotlin.math.absoluteValue

fun main() {
    val (left, right) = readInput("input/Day01").map {
        it.split(Regex("\\s+"))
    }.fold(listOf<Int>() to listOf<Int>()) { acc, (l, r) ->
        (acc.first + l.toInt()) to (acc.second + r.toInt())
    }

    val part1 = left.sorted().zip(right.sorted()).sumOf { (l, r) -> (l - r).absoluteValue }
    part1.println()

    val part2 = run {
        val counts = right.groupingBy { it }.eachCount()

        left.fold(0) { acc, value ->
            acc + (value * (counts[value] ?: 0))
        }
    }
    part2.println()
}
