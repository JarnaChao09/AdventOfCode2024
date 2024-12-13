import kotlin.io.path.Path
import kotlin.io.path.readText
// import kotlin.math.absoluteValue

// typealias Matrix<T> = MutableList<MutableList<T>>

data class Prompt(val a: Pair<Int, Int>, val b: Pair<Int, Int>, val p: Pair<Int, Int>) {
    // fun toMatrix(): Matrix<Double> {
    //     val (x1, y1) = this.a
    //     val (x2, y2) = this.b
    //     val (x3, y3) = this.p
    //
    //     return mutableListOf(
    //         mutableListOf(x1.toDouble(), x2.toDouble(), x3.toDouble()),
    //         mutableListOf(y1.toDouble(), y2.toDouble(), y3.toDouble()),
    //     )
    // }
}

fun main() {
    val input = Path("src/input/Day13.txt").readText().trim().split("\n\n")

    // fun Double.isInt(): Boolean {
    //     return (this % 1).absoluteValue <= 1e-12
    // }

    // code from: https://rosettacode.org/wiki/Reduced_row_echelon_form#Kotlin
    // fun Matrix<Double>.rref() {
    //     var lead = 0
    //     val rowCount = this.size
    //     val colCount = this[0].size
    //     for (r in 0 until rowCount) {
    //         if (colCount <= lead) return
    //         var i = r
    //
    //         while (this[i][lead] == 0.0) {
    //             i++
    //             if (rowCount == i) {
    //                 i = r
    //                 lead++
    //                 if (colCount == lead) return
    //             }
    //         }
    //
    //         val temp = this[i]
    //         this[i] = this[r]
    //         this[r] = temp
    //
    //         if (this[r][lead] != 0.0) {
    //             val div = this[r][lead]
    //             for (j in 0 until colCount) {
    //                 this[r][j] /= div
    //             }
    //         }
    //
    //         for (k in 0 until rowCount) {
    //             if (k != r) {
    //                 val mult = this[k][lead]
    //                 for (j in 0 until colCount) {
    //                     this[k][j] -= this[r][j] * mult
    //                 }
    //             }
    //         }
    //
    //         lead++
    //     }
    // }

    val points = input.map {
        val buttonA = Regex("Button A: X\\+(\\d+), Y\\+(\\d+)")
        val buttonB = Regex("Button B: X\\+(\\d+), Y\\+(\\d+)")
        val prize = Regex("Prize: X=(\\d+), Y=(\\d+)")

        val (a, b, p) = it.split("\n")

        val (ax, ay) = buttonA.find(a)!!.destructured
        val (bx, by) = buttonB.find(b)!!.destructured
        val (px, py) = prize.find(p)!!.destructured

        Prompt(ax.toInt() to ay.toInt(), bx.toInt() to by.toInt(), px.toInt() to py.toInt())
    }

    fun Prompt.solve1(): Int {
        val (x1, y1) = this.a
        val (x2, y2) = this.b
        val (x3, y3) = this.p

        val a = (x3*y2 - x2*y3) / (x1*y2 - x2*y1)
        val b = (x3*y1 - x1*y3) / (x2*y1 - x1*y2)

        return if (x1 * a + x2 * b == x3 && y1 * a + y2 * b == y3) {
            3 * a + b
        } else {
            0
        }
    }

    fun Prompt.solve2(): Long {
        val (x1, y1) = this.a.let { (x, y) -> x.toLong() to y.toLong() }
        val (x2, y2) = this.b.let { (x, y) -> x.toLong() to y.toLong() }
        val (x3, y3) = this.p.let { (x, y) -> (10000000000000 + x) to (10000000000000 + y)}

        val a = (x3*y2 - x2*y3) / (x1*y2 - x2*y1)
        val b = (x3*y1 - x1*y3) / (x2*y1 - x1*y2)

        return if (x1 * a + x2 * b == x3 && y1 * a + y2 * b == y3) {
            3 * a + b
        } else {
            0
        }
    }

    val part1 = run {
        points.sumOf {
            it.solve1()
        }
    }
    part1.println()

    val part2 = run {
        points.sumOf {
            it.solve2()
        }
    }
    part2.println()
}