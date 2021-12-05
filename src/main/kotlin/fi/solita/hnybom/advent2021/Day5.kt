package fi.solita.hnybom.advent2021

import java.io.File

typealias Point = Pair<Int, Int>

class Day5 {

    data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {

        fun occupies1(): List<Point> {
            if(x1 == x2) {
                return (y1.coerceAtMost(y2)..y1.coerceAtLeast(y2)).map { Point(x1, it) }
            }
            if(y1 == y2) {
                return (x1.coerceAtMost(x2)..x1.coerceAtLeast(x2)).map { Point(it, y1) }
            }
            return emptyList()
        }

        fun occupies2(): List<Point> {
            val straightLines = occupies1()
            if(x1 != x2 && y1 != y2) {
                fun move(from: Int, to: Int) : List<Int> {
                    return if(from < to) {
                        (from..to).toList()
                    } else {
                        (to..from).toList().reversed()
                    }
                }
                val xs = move(x1, x2)
                val ys = move(y1, y2)
                return xs.zip(ys).map { Point(it.first, it.second) } + straightLines
            }

            return straightLines

        }
    }

    private val input: List<Line> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input5.txt")
            .readLines()
            .map { row ->
                val coords = row.split("->").map { it.trim() }
                Line(
                    coords[0].split(",")[0].trim().toInt(),
                    coords[0].split(",")[1].trim().toInt(),
                    coords[1].split(",")[0].trim().toInt(),
                    coords[1].split(",")[1].trim().toInt()
                )
            }

    fun part1(): Int {
        return solvePoints {
            it.occupies1()
        }

    }

    fun part2(): Int {
        return solvePoints {
            it.occupies2()
        }
    }

    private fun solvePoints(occupies:  (Line) -> List<Point>): Int {
        val map = input.fold(emptyMap<Point, Int>()) { acc, line ->
            val occupies = occupies(line)
            val mapped = occupies.fold(emptyMap<Point, Int>()) { acc2, point ->
                val newValue = acc.getOrDefault(point, 0) + 1
                acc2.plus(point to newValue)
            }
            acc + mapped
        }

        return map.values.count { it >= 2 }
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day5().solve()
}
