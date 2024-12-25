import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val input = Path("src/input/Day25.txt").readText().trim().split("\n\n")

    val locks = mutableListOf<List<List<Char>>>()
    val keys = mutableListOf<List<List<Char>>>()
    for (s in input) {
        val line = s.split("\n").map(String::toList)

        if (line[0][0] == '#') {
            locks.add(line)
        } else {
            keys.add(line)
        }
    }

    val part1 = run {
        var ret = 0

        for (lock in locks) {
            for (key in keys) {
                var ok = true
                for (r in lock.indices) {
                    for (c in lock[0].indices) {
                        if (lock[r][c] == '#' && key[r][c] == '#') {
                            ok = false
                        }
                    }
                }

                if (ok) {
                    ret++
                }
            }
        }

        ret
    }
    part1.println()

    val part2 = run {}
    part2.println()
}