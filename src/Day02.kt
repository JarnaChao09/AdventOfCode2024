fun main() {
    val input = readInput("input/Day02").map {
        it.split(" ").map(String::toInt)
    }

    fun List<Int>.checkIncreasing(): Boolean {
        return this.zipWithNext().all { (l, r) -> (r - l) in 1..3 }
    }

    // fun List<Int>.checkIncreasing2(): Boolean {
    //     var tol = 0
    //     for (i in 0..<this.size - 1) {
    //         val l = this[i]
    //         val r = this[i + 1]
    //
    //         if (r - l in 1..3) {
    //             continue
    //         } else if (i + 2 < this.size) {
    //             if (this[i + 2] - l in 1..3 && tol == 0) {
    //                 tol++
    //                 continue
    //             } else {
    //                 return false
    //             }
    //         }  else {
    //             if (tol == 0) {
    //                 tol++
    //             } else {
    //                 return false
    //             }
    //         }
    //     }
    //     return true
    // }

    fun List<Int>.checkDecreasing(): Boolean {
        return this.zipWithNext().all { (l, r) -> (l - r) in 1..3 }
    }

    // fun List<Int>.checkDecreasing2(): Boolean {
    //     var tol = 0
    //     for (i in 0..<this.size - 1) {
    //         val l = this[i]
    //         val r = this[i + 1]
    //
    //         if (l - r in 1..3) {
    //             continue
    //         } else if (i + 2 < this.size) {
    //             if (l - this[i + 2] in 1..3 && tol == 0) {
    //                 tol++
    //                 continue
    //             } else {
    //                 return false
    //             }
    //         } else {
    //             if (tol == 0) {
    //                 tol++
    //             } else {
    //                 return false
    //             }
    //         }
    //     }
    //     return true
    // }

    val part1 = run {
        val safe = input.count { it.checkIncreasing() || it.checkDecreasing() }

        safe
    }
    part1.println()

    val part2 = run {
        var safe = 0

        // attempt at clever solution above did not work
        // guess we will just brute force it then
        // maybe come back and fix above attempts

        input.forEach {
            if (it.checkIncreasing() || it.checkDecreasing()) {
                safe++
            } else {
                for (i in it.indices) {
                    val copy = buildList {
                        for (j in it.indices) {
                            if (i != j) {
                                add(it[j])
                            }
                        }
                    }

                    if (copy.checkIncreasing() || copy.checkDecreasing()) {
                        safe++
                        break
                    }
                }
            }
        }

        safe
    }
    part2.println()
}