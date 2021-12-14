package fi.solita.hnybom.advent2021

import java.io.File

class Day14 {

    val fileLines = File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input14.txt")
        .readLines()

    private val tmpl: String = fileLines.first()

    private val instructions : Map<String, String> =
        fileLines.drop(2).map {
            val split = it.split("->")
            split[0]!!.trim() to split[1].trim()
        }.toMap()

    private fun step(template: String) : String {
        return template.windowed(2, 1, true).fold("") { acc, s ->
            instructions[s]?.let {
                acc + s[0] + it
            } ?: (acc + s)
        }
    }

    fun part1(): Int {

        val result = (0..9).fold(tmpl) { acc, i ->
            val s = step(acc)
            s
        }

        val mapped = result.associate { it to result.count { c -> it == c } }
        val max = mapped.maxOfOrNull { it.value }!!
        val min = mapped.minOfOrNull { it.value }!!
        return max - min
    }

    private fun step2(pairs: Map<String, Long>, characterCounts: MutableMap<String, Long>) : Pair<Map<String, Long>, MutableMap<String, Long>> {

        val s = pairs.flatMap { entry ->
            instructions[entry.key]?.let { key ->
                val pair1 = entry.key[0] + key
                val pair2 = key + entry.key[1]
                characterCounts[key] = characterCounts.getOrDefault(key, 0L) + entry.value
                listOf(
                    pair1 to entry.value,
                    pair2 to entry.value
                )
            } ?: listOf(entry.toPair())
        }

        val v = s.associate { p ->
            p.first to s.filter { it.first == p.first }.sumOf { it.second }
        }

        return Pair(v, characterCounts)
    }

    private fun part2(): Long {
        val pairs = tmpl.windowed(2, 1, false).associateWith { 1L }
        val charCounts = tmpl.associate { s -> s.toString() to tmpl.count { it == s }.toLong() }.toMutableMap()

        (0..39).fold(Pair(pairs, charCounts)) { acc, _ ->
            val s = step2(acc.first, acc.second)
            s
        }
        val max = charCounts.maxOfOrNull { it.value }!!
        val min = charCounts.minOfOrNull { it.value }!!
        return max - min
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day14().solve()
}
