fun main() {
    val input = readInput("input/Day04")

    val grid = input.map(String::toList)

    fun search1(r: Int, c: Int, toFind: String, direction: Pair<Int, Int>): Int {
        if (r !in grid.indices || c !in grid[0].indices || toFind.isEmpty()) {
            return 0
        }
        return if (grid[r][c] == toFind.first() && toFind.length == 1) {
            1
        } else if (grid[r][c] == toFind.first()) {
            search1(r + direction.first, c + direction.second, toFind.drop(1), direction)
        } else {
            0
        }
    }

    fun search2(r: Int, c: Int): Int {
        if (r + 2 !in grid.indices || c + 2 !in grid[0].indices) {
            return 0
        }

        // tl - tr
        // -- m --
        // bl - br
        val tl = grid[r][c]
        val tr = grid[r][c + 2]
        val bl = grid[r + 2][c]
        val br = grid[r + 2][c + 2]
        val middle = grid[r + 1][c + 1]

        if (middle != 'A') {
            return 0
        }

        val top = tl == 'M' && tl == tr && bl == 'S' && bl == br
        val left = tl == 'M' && tl == bl && br == 'S' && tr == br
        val bottom = bl == 'M' && bl == br && tl == 'S' && tl == tr
        val right = br == 'M' && br == tr && bl == 'S' && tl == bl

        return if (top || left || bottom || right) {
            1
        } else {
            0
        }
    }

    val part1 = run {
        var total = 0

        for (i in grid.indices) {
            for (j in grid[0].indices) {
                val curr = grid[i][j]

                if (curr == 'X') {
                    for (dir in listOf(-1 to -1, -1 to 0, -1 to 1, 0 to 1, 1 to 1, 1 to 0, 1 to -1, 0 to -1)) {
                        total += search1(i, j, "XMAS", dir)
                    }
                }
            }
        }

        total
    }
    part1.println()

    val part2 = run {
        var total = 0

        for (i in grid.indices) {
            for (j in grid[0].indices) {
                val curr = grid[i][j]

                total += search2(i, j)
            }
        }

        total
    }
    part2.println()
}