data class Robot(val px: Int, val py: Int, val vx: Int, val vy: Int)

fun main() {
    val input = readInput("input/Day14")
    
    val robots = input.map {
        val (ps, vs) = it.split(" ")

        val (px, py) = ps.drop(2).split(",").map(String::toInt)
        val (vx, vy) = vs.drop(2).split(",").map(String::toInt)

        Robot(px, py, vx, vy)
    }

    // val gridX = 11
    val gridX = 101
    val gridY = 103
    // val gridY = 7

    fun quadrant(r: Int, c: Int): Int {
        val midX = (gridX - 1) / 2
        val midY = (gridY - 1) / 2
        return if (r == midX || c == midY) {
            0
        } else if (r < midX && c < midY) {
            1
        } else if (r > midX && c < midY) {
            2
        } else if (r < midX && c > midY) {
            3
        } else {
            4
        }
    }

    val part1 = run {
        val finalRobots = robots.map {
            val (px, py, vx, vy) = it

            val fx = px + vx * 100
            val fy = py + vy * 100

            fx.mod(gridX) to fy.mod(gridY)
        }.groupBy {
            quadrant(it.first, it.second)
        }

        (finalRobots[1]?.size?.toLong() ?: 0L) * (finalRobots[2]?.size?.toLong() ?: 0L) * (finalRobots[3]?.size?.toLong() ?: 0L) * (finalRobots[4]?.size?.toLong() ?: 0L)
    }
    part1.println()

    val part2 = run {
        var time = 0
        var pos = mutableListOf<Pair<Int, Int>>()
        var vel = mutableListOf<Pair<Int, Int>>()
        robots.forEach { robot ->
            pos.add(robot.let { it.px to it.py })
            vel.add(robot.let { it.vx to it.vy })
        }
        while (true) {
            if (pos.toSet().size == pos.size) {
                break
            }

            for (i in pos.indices) {
                val (px, py) = pos[i]
                val (vx, vy) = vel[i]

                pos[i] = (px + vx).mod(gridX) to (py + vy).mod(gridY)
            }

            time++
        }

        time
    }
    part2.println()
}