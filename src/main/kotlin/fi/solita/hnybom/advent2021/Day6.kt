package fi.solita.hnybom.advent2021

import java.io.File

typealias ScalableSea = MutableMap<Int, Long>

class Day6 {

    data class Sea(val fish: MutableList<Fish>)
    data class Fish(var counter: Int)

    private fun getSea() =
        Sea(
            File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input6.txt")
                .readLines()
                .first()
                .split(",")
                .map { Fish(it.toInt()) }
                .toMutableList()
        )

    private fun getScalableSea(): ScalableSea {
        val sea = mutableMapOf<Int, Long>()
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input6.txt")
            .readLines()
            .first()
            .split(",")
            .map { it.toInt() }
            .forEach {
                sea[it] = sea.getOrDefault(it, 0L)  + 1
            }
        return sea
    }

    fun part1(): Int {
        val localSea = getSea()
        (0..79).forEach {
            val toAdd = localSea.fish.fold(0L) { acc, fish ->
                if(fish.counter == 0) {
                    fish.counter = 6
                    acc + 1
                } else {
                    fish.counter--
                    acc
                }
            }

            localSea.fish.addAll((1..toAdd).map { Fish(8) })
        }
        return localSea.fish.size
    }

    fun part2(): Long {
        val localSea = getScalableSea()
        (0..255).forEach { _ ->
            val toAdd = localSea.getOrDefault(0, 0L)

            (0..7).forEach {
                val countsToTransfer = localSea.getOrDefault(it + 1, 0L)
                localSea[it] = countsToTransfer
            }

            if(toAdd != 0L) {
                localSea[6] = localSea.getOrDefault(6, 0L) + toAdd
                localSea[8] = toAdd
            } else localSea[8] = 0

        }
        return localSea.values.sum()
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day6().solve()
}
