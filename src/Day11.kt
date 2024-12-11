fun main() {
    val input = readInput("input/Day11")[0].split(" ")

    val part1 = run {
        var stones = input

        repeat(25) {
            stones = buildList {
                stones.forEach { stone ->
                    when {
                        stone == "0" -> {
                            add("1")
                        }

                        stone.length % 2 == 0 -> {
                            add(stone.take(stone.length / 2).toLong().toString())
                            add(stone.drop(stone.length / 2).toLong().toString())
                        }

                        else -> {
                            add("${stone.toLong() * 2024L}")
                        }
                    }
                }
            }
        }
        stones.size
    }
    part1.println()
    
    val part2 = run {
        var stonesMap = buildMap {
            input.forEach {
                put(it, getOrElse(it) { 0L } + 1L)
            }
        }

        repeat(75) {
            stonesMap = buildMap {
                for ((key, value) in stonesMap) {
                    val kl = key.toLong()
                    when {
                        kl == 0L -> {
                            put("1", getOrElse("1") { 0L } + value)
                        }

                        key.length % 2 == 0 -> {
                            val left = key.take(key.length / 2).toLong().toString()
                            val right = key.drop(key.length / 2).toLong().toString()

                            put(left, getOrElse(left) { 0L } + value)
                            put(right, getOrElse(right) { 0L } + value)
                        }

                        else -> {
                            val newKey = "${key.toLong() * 2024L}"

                            put(newKey, getOrElse(newKey) { 0L } + value)
                        }
                    }
                }
            }
        }

        stonesMap.values.sum()
    }
    part2.println()
}