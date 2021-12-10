package fi.solita.hnybom.advent2021

import java.io.File

typealias Coordinate = Pair<Int, Int>

class Day9 {

    data class Point(val x: Int, val y: Int, val z: Int)
    private val inputStr: List<String> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input9.txt")
            .readLines()

    private val points: Map<Coordinate, Point> = inputStr.flatMapIndexed { y, s ->
        s.mapIndexed { x, c ->
            Pair(x, y) to Point(x, y, c.toString().toInt())
        }
    }.toMap()

    private fun getAdjacent(p: Point): List<Point> {
        return listOfNotNull(
            points.getOrDefault(Coordinate(p.x - 1, p.y), null),
            points.getOrDefault(Coordinate(p.x + 1, p.y), null),
            points.getOrDefault(Coordinate(p.x, p.y + 1), null),
            points.getOrDefault(Coordinate(p.x, p.y - 1), null),
        )
    }

    fun part1(): Int {
        val lowPoints = getLowPoints()
        return lowPoints.sumOf { it.z + 1 }
    }

    private fun getLowPoints(): List<Point> {
        return points.values.filter { target ->
            getAdjacent(target).all { target.z < it.z }
        }
    }

    private tailrec fun getBasinPoints(p: Point): List<Point> {
        val higherNeighbours = getAdjacent(p).filter { it.z > p.z && it.z < 9 }
        return if(higherNeighbours.isEmpty()) {
            emptyList()
        } else higherNeighbours + higherNeighbours.flatMap { getBasinPoints(it) }
    }

    fun part2(): Int {
        val lowPoints = getLowPoints()

        val basins = lowPoints.map {
            getBasinPoints(it).distinct()
        }

        return basins.sortedByDescending { it.size }
            .take(3)
            .map { it.size + 1 } // add start point as well
            .reduce { acc, i -> acc * i }
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day9().solve()
}
