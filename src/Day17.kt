import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val input = Path("src/input/Day17.txt").readText().trim().split("\n\n")

    val (registerA, registerB, registerC) = input[0].split("\n").map { it.split(": ").last().toLong() }
    val program = input[1].split(": ").last().split(",").map { it.toInt() }

    fun runProgram(a: Long, b: Long, c: Long): List<Int> {
        var A = a
        var B = b
        var C = c

        fun Int.decodeCombo(): Long {
            return when (this) {
                0, 1, 2, 3 -> this.toLong()
                4 -> A
                5 -> B
                6 -> C
                7 -> error("reserved")
                else -> error("unreachable")
            }
        }

        return buildList {
            var pc = 0

            while (pc < program.size) {
                val opcode = program[pc]
                val operand = program[pc + 1]

                when (opcode) {
                    0 -> {
                        val rhs = operand.decodeCombo()

                        A = A shr rhs.toInt()
                    }

                    1 -> {
                        B = B xor operand.toLong()
                    }

                    2 -> {
                        B = operand.decodeCombo().mod(8L)
                    }

                    3 -> {
                        if (A != 0L) {
                            pc = operand - 2
                        }
                    }

                    4 -> {
                        B = B xor C
                    }

                    5 -> {
                        add(operand.decodeCombo().mod(8))
                    }

                    6 -> {
                        val rhs = operand.decodeCombo()

                        B = A shr rhs.toInt()
                    }

                    7 -> {
                        val rhs = operand.decodeCombo()

                        C = A shr rhs.toInt()
                    }
                }

                pc += 2
            }
        }
    }

    fun search(a: Long, b: Long, c: Long, length: Int): Long? {
        if (length == program.size + 1) {
            return a
        }

        return buildList {
            for (word in 0L..7L) {
                if (runProgram(8 * a + word.toLong(), b, c) == program.subList(program.size - length, program.size)) {
                    search(8 * a + word.toLong(), b, c, length + 1)?.let {
                        add(it)
                    }
                }
            }
        }.minOrNull()
    }

    val part1 = run {
        runProgram(registerA, registerB, registerC).joinToString(",")
    }
    part1.println()

    val part2 = run {
        val a = search(0, registerB, registerC, 1)

        val ret = runProgram(a!!, registerB, registerC)
        println("$a | ${program == ret} | $program == $ret")

        a
    }
    part2.println()
}