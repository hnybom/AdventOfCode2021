package fi.solita.hnybom.advent2021

import java.io.File

class Day1 {

    private val input: List<Long> = File(
        "/home/henriny/work/own/AdventOfCode2021/src/main/resources/input1.txt").readLines().map { it.toLong() }

    fun part1(): Long {
        return input
            .zipWithNext()
            .sumOf { (a, b) -> if (a < b) 1L else 0L }
    }

    fun part2(): Long {
        return input
            .windowed(3, 1, false)
            .zipWithNext()
            .sumOf { (a, b) -> if (a.sum() < b.sum()) 1 else 0L }
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day1().solve()
}
