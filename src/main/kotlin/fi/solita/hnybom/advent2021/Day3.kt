package fi.solita.hnybom.advent2021

import fi.solita.hnybom.advent2021.utils.Helpers
import java.io.File
import kotlin.math.pow

class Day3 {

    private val input: List<List<Long>> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input3.txt")
            .readLines()
            .map {
                    str -> str.toCharArray().map {
                        it.digitToInt().toLong()
                    }
            }


    private fun part1(): Long {
        val transposed = Helpers.transpose(input)
        val gamma = transposed.map { list ->
            if(list.count { it == 1L } > list.count { it == 0L }) 1L else 0L
        }
        val epsilon = gamma.map { if(it == 1L) 0L else 1L }

        val g = gamma.foldIndexed(0L, bin2dec(gamma.size))
        val e = epsilon.foldIndexed(0L, bin2dec(gamma.size))
        return g * e
    }

    private fun bin2dec(length: Int) = { index: Int, acc: Long, i: Long ->
        if (i == 1L) acc + 2.0.pow(length - index - 1).toLong()
        else acc
    }

    private fun part2(): Long {
        val mostCommonByIndex = calculateMostCommonByIndex(input)

        tailrec fun filterByIndex(
            listToFilter: List<List<Long>>,
            index: Int,
            mostCommonByIndexAtThisPoint: List<Long>,
            filter: (Long, Int, List<Long>) -> Boolean): List<Long> {
            val filtered = listToFilter.filter {
                filter(it[index], index, mostCommonByIndexAtThisPoint)
            }
            return if(filtered.size == 1) filtered.first()
            else if(index < 0) return emptyList()
            else filterByIndex(filtered, index + 1, calculateMostCommonByIndex(filtered), filter)
        }

        val oxygenBits = filterByIndex(input, 0, mostCommonByIndex) { it, index, mostCommonByIndexAtThisPoint ->
            val mostCommon = mostCommonByIndexAtThisPoint[index]
            if(mostCommon == -1L) it == 1L
            else it == mostCommon
        }

        val co2 = filterByIndex(input, 0, mostCommonByIndex) { it, index, mostCommonByIndexAtThisPoint ->
            val mostCommon = mostCommonByIndexAtThisPoint[index]
            if(mostCommon == -1L) it == 0L
            else it != mostCommon
        }

        val o = oxygenBits.foldIndexed(0L, bin2dec(oxygenBits.size))
        val c = co2.foldIndexed(0L, bin2dec(co2.size))
        return o * c

    }

    private fun calculateMostCommonByIndex(matrix: List<List<Long>>): List<Long> {
        val transposed = Helpers.transpose(matrix)
        val mostCommonByIndex = transposed.map { list ->
            val ones = list.count { it == 1L }
            val zeros = list.count { it == 0L }
            if (ones > zeros) 1L
            else if (ones < zeros) 0L
            else -1L
        }
        return mostCommonByIndex
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day3().solve()
}