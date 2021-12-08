package fi.solita.hnybom.advent2021

import java.io.File

class Day8 {

    data class Display(val connections: List<String>, val numbers: List<String>)


    private val input: List<Display> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input8.txt")
            .readLines()
            .map {
                val splitted = it.split("|")
                Display(
                    splitted[0].trim().split(" ").toList(),
                    splitted[1].trim().split(" ").toList()
                )
            }

    fun part1(): Long {
        return input.flatMap { it.numbers }
                .fold(0L) { acc, list ->
                    when(list.length) {
                        2 -> acc + 1 // 1
                        3 -> acc + 1 // 7
                        4 -> acc + 1 // 4
                        7 -> acc + 1 // 8
                        else -> acc
                    }
                }
    }

    fun part2(): Long {
        return input.fold(0L) { acc, display ->
            val numbers = display.numbers
            val connections = display.connections
            val oneChars = connections.find { it.length == 2 }!!
            val threeChars = connections.find { it.length == 5 && containsAll(it, oneChars) }!!
            val fourChars = connections.find { it.length == 4 }!!
            val sevenChars = connections.find { it.length == 3 }!!
            val eightChars = connections.find { it.length == 7 }!!
            val nineChars = connections.find { it.length == 6 && containsAll(it, fourChars) }!!
            val zeroChars = connections.find { it.length == 6 && containsAll(it, oneChars) && it != nineChars }!!
            val sixChars = connections.find { it.length == 6 && it != zeroChars && it != nineChars }!!
            val sixAndNineForFive = union(sixChars, nineChars)
            val fiveChars = connections.find { it.length == 5 && containsAll(it, sixAndNineForFive) }!!
            val twoChars = connections.find { it.length == 5 && it != threeChars && it != fiveChars }!!

            val result = numbers.map {
                when {
                    containsAllWithSameLength(it, zeroChars) -> "0"
                    containsAllWithSameLength(it, oneChars) -> "1"
                    containsAllWithSameLength(it, twoChars) -> "2"
                    containsAllWithSameLength(it, threeChars) -> "3"
                    containsAllWithSameLength(it, fourChars) -> "4"
                    containsAllWithSameLength(it, fiveChars) -> "5"
                    containsAllWithSameLength(it, sixChars) -> "6"
                    containsAllWithSameLength(it, sevenChars) -> "7"
                    containsAllWithSameLength(it, eightChars) -> "8"
                    containsAllWithSameLength(it, nineChars) -> "9"
                    else -> throw IllegalStateException("No match for $it")
                }
            }.joinToString("").toLong()
            acc + result
        }
    }

    private fun containsAllWithSameLength(str: String, chars: String) : Boolean {
        return if(str.length == chars.length) {
            containsAll(str, chars)
        } else false
    }

    private fun containsAll(str: String, chars: String) : Boolean {
        chars.forEach { c ->
            if (!str.contains(c)) {
                return false
            }
        }
        return true
    }

    private fun union(a: String, b: String): String {
        return a.toCharArray().intersect(b.toCharArray().toSet()).joinToString("")
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day8().solve()
}
