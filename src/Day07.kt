fun main() {
    val input = readInput("input/Day07").map {
        val tmp = it.split(" ")
        val goal = tmp[0].dropLast(1).toLong()
        val rest = tmp.drop(1).map(String::toLong)

        goal to rest
    }

    fun List<Long>.solve(acc: Long, concat: Boolean): Sequence<Long> = sequence {
        if (this@solve.isEmpty()) {
            yield(acc)
        } else {
            yieldAll(this@solve.drop(1).solve(acc + this@solve.first(), concat))
            yieldAll(this@solve.drop(1).solve(acc * this@solve.first(), concat))
            if (concat) {
                yieldAll(this@solve.drop(1).solve("$acc${this@solve.first()}".toLong(), concat))
            }
        }
    }

    val (part1, part2) = run {
        input.fold(0L to 0L) { (p1, p2), (goal, rest) ->
            val part1 = if (goal in rest.solve(0, false)) goal else 0
            val part2 = if (goal in rest.solve(0, true)) goal else 0

            (p1 + part1) to (p2 + part2)
        }
    }
    part1.println()
    part2.println()
}