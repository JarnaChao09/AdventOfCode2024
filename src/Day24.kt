import kotlin.io.path.Path
import kotlin.io.path.readText

sealed interface AST24 {
    fun eval(state: MutableMap<String, AST24>): Boolean
}

data class Value24(val value: Boolean): AST24 {
    override fun eval(state: MutableMap<String, AST24>): Boolean {
        return this.value
    }
}

data class Register24(val name: String): AST24 {
    override fun eval(state: MutableMap<String, AST24>): Boolean {
        return state[name]!!.let {
            if (it is Value24) {
                it.value
            } else {
                val ret = it.eval(state)

                state[name] = Value24(ret)

                ret
            }
        }
    }

    override fun toString(): String {
        return this.name
    }
}

data class And24(val lhs: AST24, val rhs: AST24): AST24 {
    override fun eval(state: MutableMap<String, AST24>): Boolean {
        return this.lhs.eval(state) && this.rhs.eval(state)
    }

    override fun toString(): String {
        return "${this.lhs} AND ${this.rhs}"
    }
}

data class Or24(val lhs: AST24, val rhs: AST24): AST24 {
    override fun eval(state: MutableMap<String, AST24>): Boolean {
        return this.lhs.eval(state) || this.rhs.eval(state)
    }

    override fun toString(): String {
        return "${this.lhs} OR ${this.rhs}"
    }
}

data class Xor24(val lhs: AST24, val rhs: AST24): AST24 {
    override fun eval(state: MutableMap<String, AST24>): Boolean {
        return this.lhs.eval(state) xor this.rhs.eval(state)
    }

    override fun toString(): String {
        return "${this.lhs} XOR ${this.rhs}"
    }
}

fun main() {
    val input = Path("src/input/Day24.txt").readText().trim().split("\n\n")

    val registers = input[0].split("\n").associate<_, _, AST24> {
        val (r, v) = it.split(": ")

        r to Value24(v == "1")
    }.toMutableMap()

    val opRegex = Regex("([a-z0-9]+) (AND|OR|XOR) ([a-z0-9]+) -> ([a-z0-9]+)")
    val registerTable = mutableMapOf<String, String>()
    input[1].split("\n").forEach {
        val (lhs, op, rhs, ret) = opRegex.matchEntire(it)!!.destructured

        val ast = when (op) {
            "AND" -> {
                And24(Register24(lhs), Register24(rhs))
            }
            "OR" -> {
                Or24(Register24(lhs), Register24(rhs))
            }
            "XOR" -> {
                Xor24(Register24(lhs), Register24(rhs))
            }
            else -> error("unreachable")
        }

        registers[ret] = ast

        registerTable["${listOf(lhs, rhs).sorted().joinToString("|")}|$op"] = ret
    }

    fun find(lhs: String, rhs: String, operation: String): String? {
        val key = "${listOf(lhs, rhs).sorted().joinToString("|")}|$operation"

        return registerTable[key]
    }

    val part1 = run {
        registers.keys.filter { it.startsWith("z") }.sortedDescending().joinToString("") {
            if (registers[it]!!.eval(registers)) "1" else "0"
        }.toLong(2)
    }
    part1.println()

    val part2 = run {
        buildList {
            var c0: String? = null

            repeat(45) {
                val n = it.toString().padStart(2, '0')

                var r1: String? = null
                var z1: String? = null
                var c1: String? = null

                // half adder
                // X1 XOR Y1 -> M1
                // X1 AND Y1 -> N1
                // C0 AND M1 -> R1
                // C0 XOR M1 -> Z1
                // R1 OR  N1 -> C1

                var m1 = find("x$n", "y$n", "XOR")
                var n1 = find("x$n", "y$n", "AND")

                if (c0 != null) {
                    r1 = find(c0, m1!!, "AND")

                    if (r1 == null) {
                        val tmp = m1
                        m1 = n1
                        n1 = tmp

                        add(m1!!)
                        add(n1)

                        r1 = find(c0, m1, "AND")
                    }

                    z1 = find(c0, m1, "XOR")

                    if (m1.startsWith("z")) {
                        val tmp = m1
                        m1 = z1
                        z1 = tmp

                        add(m1!!)
                        add(z1)
                    }

                    if (n1?.startsWith("z") == true) {
                        val tmp = z1
                        z1 = n1
                        n1 = tmp

                        add(n1!!)
                        add(z1)
                    }

                    if (r1?.startsWith("z") == true) {
                        val tmp = r1
                        r1 = z1
                        z1 = tmp

                        add(r1!!)
                        add(z1)
                    }

                    c1 = find(r1 ?: "", n1 ?: "", "OR")
                }

                if (c1?.startsWith("z") == true && c1 != "z45") {
                    val tmp = c1
                    c1 = z1
                    z1 = tmp

                    add(c1!!)
                    add(z1)
                }

                c0 = if (c0 != null) {
                    c1
                } else {
                    n1
                }
            }
        }.sorted().joinToString(",")
    }
    part2.println()
}