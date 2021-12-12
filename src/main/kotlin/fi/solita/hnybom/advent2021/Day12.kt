package fi.solita.hnybom.advent2021

import java.io.File

val pathname = "/home/henriny/work/own/AdventOfCode2021/src/main/resources/input12.txt"

class Day12 {

    data class Cave(val name: String, val small: Boolean, val paths: MutableList<Cave>)

    private val caves: Map<String, Cave> =
        File(pathname)
            .readLines()
            .flatMap { s ->
                s.split("-").map {
                    it to Cave(it, Character.isLowerCase(it[0]), ArrayList())
                }
            }.distinct()
            .toMap()

    init {
        File(pathname)
            .readLines()
            .map {
                val p = it.split("-")
                caves[p[0]]!!.paths.add(caves[p[1]]!!)
                caves[p[1]]!!.paths.add(caves[p[0]]!!)
            }
    }

    private val start = caves["start"]
    private val end = caves["end"]

    fun part1(): Long {
        fun travel(cave: Cave, visited: List<Cave>) : Long {
            if (cave == end) return 1
            if (cave.small && visited.contains(cave)) return 0
            return cave.paths.sumOf { travel(it, visited + cave) }
        }
        return travel(start!!, emptyList())
    }

    fun part2(): Long {
        fun travel(cave: Cave, visited: List<Cave>, smallUsed: Boolean) : Long {
            if (cave == start && visited.isNotEmpty()) return 0
            if (cave == end) return 1
            if (cave.small && visited.contains(cave) && smallUsed) return 0
            return cave.paths.sumOf { travel(it, visited + cave, (visited.contains(cave) && cave.small) || smallUsed) }
        }
        return travel(start!!, emptyList(), false)
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day12().solve()
}
