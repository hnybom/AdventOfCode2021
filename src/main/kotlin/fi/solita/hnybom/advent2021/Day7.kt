package fi.solita.hnybom.advent2021

import java.io.File
import kotlin.math.abs

class Day7 {

    private val input: List<Long> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input7.txt")
            .readLines()
            .first()
            .split(",")
            .map { it.toLong() }
            .sorted()

    fun part1(): Long {
        val median = input[input.size / 2]
        return input.sumOf { abs(it - median) }
    }

    fun part2(): Long {
        val min = input.first()
        val max = input.last()
        val allValues = (min..max).map { range ->
            range to input.sumOf {
                val diff = abs(it - range)
                diff * 0.5 * (diff + 1)
            }
        }
        val ans = allValues.minByOrNull { it.second }
        return ans?.second?.toLong() ?: 0L
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day7().solve()
}
