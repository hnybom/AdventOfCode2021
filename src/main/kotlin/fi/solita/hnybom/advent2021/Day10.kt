package fi.solita.hnybom.advent2021

import java.io.File
import java.util.*

class Day10 {

    private val input: List<String> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input10.txt")
            .readLines()

    private val openingBrackets = listOf('(', '[', '{', '<')
    private val bracketMetadata = mapOf(
        '(' to Pair(')', 3),
        '[' to Pair(']', 57),
        '{' to Pair('}', 1197),
        '<' to Pair('>', 25137)
    )

    private val closingBracketsAddition = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    private val closingBrackets = bracketMetadata.values.associate { it.first to it.second }

    fun part1(): Int {

        val result = findCorruptLineScores()
        return result.sum()
    }

    private fun findCorruptLineScores() = input.map { s ->
        val stack = Stack<Char>()
        val values = s.map { c ->
            if (openingBrackets.contains(c)) {
                stack.add(c)
                0
            } else {
                val expecting = stack.pop()
                val lastOpen = bracketMetadata[expecting]
                if (lastOpen?.first != c) {
                    closingBrackets[c] ?: 0
                } else 0
            }
        }
        values.find { it != 0 } ?: 0
    }

    fun part2(): Long {
        val result = input.map { s ->
            val stack = Stack<Char>()
            // LOL
            run br@ {
                s.forEach { c ->
                    if (openingBrackets.contains(c)) {
                        stack.add(c)
                    } else {
                        val expecting = stack.pop()
                        val lastOpen = bracketMetadata[expecting]
                        if (lastOpen?.first != c) {
                            stack.clear()
                            return@br
                        }
                    }
                }
            }
            stack.reversed().fold(0L) { acc, c ->
                val add = closingBracketsAddition[bracketMetadata[c]?.first] ?: 0
                (acc * 5) + add
            }

        }

        val filtered = result.filter { it != 0L }
        return filtered.sorted()[filtered.size / 2]
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day10().solve()
}
