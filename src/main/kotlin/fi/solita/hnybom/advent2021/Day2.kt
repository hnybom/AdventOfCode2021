package fi.solita.hnybom.advent2021

import java.io.File

class Day2 {

    enum class Command {
        UP, DOWN, FORWARD;

        companion object {
            fun fromString(str : String) : Command {
                return when (str) {
                    "forward" -> FORWARD
                    "down" -> DOWN
                    "up" -> UP
                    else -> throw IllegalArgumentException("Unknown command: $str")
                }
            }
        }
    }

    data class Movement(val command: Command, val steps: Int)

    private val input: List<Movement> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input2.txt")
            .readLines()
            .map {
                val split = it.split(" ")
                Movement(Command.fromString(split[0]), split[1].toInt())
            }

    fun part1(): Pair<Int, Int> {
        return input.fold(0 to 0)
        { coord, c  ->
            when (c.command) {
                Command.UP -> coord.first to coord.second - c.steps
                Command.DOWN -> coord.first to coord.second + c.steps
                Command.FORWARD -> coord.first + c.steps to coord.second
            }
        }
    }

    fun part2(): Triple<Long, Long, Long> {
        return input.fold(Triple(0L, 0L, 0L))
        { coord, c ->
            when (c.command) {
                Command.UP -> Triple(coord.first, coord.second, coord.third - c.steps)
                Command.DOWN -> Triple(coord.first, coord.second, coord.third + c.steps)
                Command.FORWARD -> Triple(coord.first + c.steps,coord.second + (coord.third * c.steps), coord.third)
            }
        }
    }

    fun solve() {
        val p1 = part1()
        val p2 = part2()
        println("Part 1: $p1 and the multiplication is ${p1.first * p1.second}")
        println("Part 2: $p2 and the multiplication is ${p2.first * p2.second}")
    }
}

fun main(args: Array<String>) {
    Day2().solve()
}
