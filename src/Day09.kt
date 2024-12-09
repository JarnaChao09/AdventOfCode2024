fun main() {
    val input = readInput("input/Day09")[0].map { it.digitToInt() }

    fun List<Int>.debug(): String = this.joinToString("") {
        if (it == -1) { "." } else { it.toString() }
    }

    val part1 = run {
        val buffer = buildList {
            var id = 0
            input.forEachIndexed { index, digit ->
                if (index % 2 == 0) {
                    repeat(digit) {
                        add(id)
                    }
                    id++
                } else {
                    repeat(digit) {
                        add(-1)
                    }
                }
            }
        }

        val fixed = buildList {
            var l = 0
            var r = buffer.size - 1
            while (l <= r) {
                if (buffer[l] == -1) {
                    while (buffer[r] == -1) {
                        r--
                    }
                    add(buffer[r--])
                    l++
                } else {
                    add(buffer[l++])
                }
            }
        }

        fixed.mapIndexed { index, x -> x.toLong() * index }.sum()
    }
    part1.println()

    val part2 = run {
        val holes = mutableMapOf<Int, Int>()
        val files = mutableListOf<Triple<Int, Int, Int>>()
        val buffer = buildList {
            var id = 0
            input.forEachIndexed { index, digit ->
                if (index % 2 == 0) {
                    files.add(Triple(id, digit, this.size))
                    repeat(digit) {
                        add(id)
                    }
                    id++
                } else {
                    if (digit != 0) {
                        holes[this.size] = digit
                        repeat(digit) {
                            add(-1)
                        }
                    }
                }
            }
        }

        files.reverse()

        /*
        I feel like this solution *shouldn't* work as it is a search
        from the beginning of the buffer to the end and filling in
        with the largest file id

        take the test input for example
        the algorithm described in the example is

        00...111...2...333.44.5555.6666.777.888899
        0099.111...2...333.44.5555.6666.777.8888..
        0099.1117772...333.44.5555.6666.....8888..
        0099.111777244.333....5555.6666.....8888..
        00992111777.44.333....5555.6666.....8888..

        the algorithm implemented below is

        00...111...2...333.44.5555.6666.777.888899
        0099.111...2...333.44.5555.6666.777.8888..
        00992111.......333.44.5555.6666.777.8888..
        00992111777....333.44.5555.6666.....8888..
        00992111777.44.333....5555.6666.....8888..

        based solely off of how holes move, the algorithm is wrong
        however, holes are never recalculated so in essence, there's
        two separate holes right next to each other. This prevents
        "incorrect" moves from being moved into the hole during traversal
        which may actually correctly implement the desired algorithm
         */

        val fixed = buffer.toMutableList()
        var l = 0
        while (l < fixed.size) {
            if (fixed[l] == -1) {
                val sizeOfHole = holes[l]!!

                val fileIndex = files.indexOfFirst { (id, size, index) ->
                    size <= sizeOfHole && l < index
                }

                if (fileIndex != -1) {
                    holes.remove(l)

                    val (fileId, fileSize, originalIndex) = files.removeAt(fileIndex)

                    repeat(fileSize) {
                        fixed[l++] = fileId
                    }
                    repeat(fileSize) {
                        fixed[originalIndex + it] = -1
                        holes[originalIndex] = fileSize
                    }

                    if (sizeOfHole > fileSize) {
                        holes[l] = sizeOfHole - fileSize
                    }
                } else {
                    l += holes[l]!!
                }
            } else {
                l++
            }
        }

        fixed.mapIndexed { index, i -> (if (i == -1) 0L else i.toLong()) * index }.sum()
    }
    part2.println()
}