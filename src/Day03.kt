fun main() {
    val input = readInput("input/Day03")

    val part1 = run {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")

        var total = 0L

        input.forEach {
            total += regex.findAll(it).toList().sumOf {
                val (_, l, r) = it.groupValues

                l.toLong() * r.toLong()
            }
        }

        total
    }
    part1.println()

    val part2 = run {
        val regex = Regex("(do\\(\\))|(don't\\(\\))|mul\\((\\d+),(\\d+)\\)")

        var total = 0L
        var active = true

        input.forEach {
            total += regex.findAll(it).toList().sumOf {
                val (_, `do`, dont, l, r) = it.groupValues

                if (`do`.isNotEmpty()) {
                    active = true
                }
                if (dont.isNotEmpty()) {
                    active = false
                }

                if (l.isEmpty() && r.isEmpty()) {
                    0L
                } else {
                    if (active) l.toLong() * r.toLong() else 0L
                }
            }
        }

        total
    }
    part2.println()
}