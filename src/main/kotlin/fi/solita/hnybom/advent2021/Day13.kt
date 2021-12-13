package fi.solita.hnybom.advent2021

import java.io.File

class Day13 {

    private val lines = File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input13.txt")
        .readLines()

    private val coordinates: List<Coordinate> =
            lines
            .takeWhile { it.isNotEmpty() }
            .map {
                val s = it.split(",")
                Coordinate(s[0].toInt(), s[1].toInt())
            }

    enum class FOLD {
        X, Y
    }
    data class Fold(val fold: FOLD, val place: Int)

    private val folding = lines.dropWhile { it.isNotEmpty() }
        .drop(1)
        .map {
            val s = it.split("=")
            Fold(FOLD.valueOf(s[0].last().toString().uppercase()), s[1].toInt())
        }

    private fun foldPaper(coord: Coordinate, fold: Fold): Coordinate? {
        val max = fold.place * 2
        return when(fold.fold) {
            FOLD.X -> {
                if(coord.first == fold.place) null
                else if (coord.first < fold.place) coord
                else Coordinate(max - coord.first, coord.second)
            }

            FOLD.Y -> {
                if(coord.second == fold.place) null
                else if (coord.second < fold.place) coord
                else Coordinate(coord.first, max - coord.second)
            }
        }
    }

    fun part1(): Int {
        val result = folding.take(1).flatMap { fold ->
            coordinates.mapNotNull {
                foldPaper(it, fold)
            }
        }
        return result.distinct().count()
    }

    fun part2(): Long {
        val result = folding.fold(coordinates) { acc, fold ->
            acc.mapNotNull {
                foldPaper(it, fold)
            }
        }
        val rd = result.distinct()
        val maxX = rd.maxOf { it.first }
        val maxY = rd.maxOf { it.second }
        val map = rd.associateWith { it }

        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val coord = Coordinate(x, y)
                if(map.containsKey(coord)) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }

        return 0L
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day13().solve()
}
