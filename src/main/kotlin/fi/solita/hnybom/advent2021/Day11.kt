package fi.solita.hnybom.advent2021

import java.io.File

class Day11 {

    data class Octopus(val x: Int, val y: Int, var power: Int, var hasFlashed: Boolean = false)


    private fun getMap() =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input11.txt")
            .readLines()
            .flatMapIndexed {
                    x, str -> str.toCharArray().mapIndexed { y, c -> Pair(x, y) to Octopus(x,y,c.digitToInt()) } }.toMap()

    private fun getAdjacent(p: Octopus, map: Map<Coordinate, Octopus>): List<Octopus> {
        return listOfNotNull(
            map.getOrDefault(Coordinate(p.x - 1, p.y), null),
            map.getOrDefault(Coordinate(p.x + 1, p.y), null),
            map.getOrDefault(Coordinate(p.x, p.y + 1), null),
            map.getOrDefault(Coordinate(p.x, p.y - 1), null),
            map.getOrDefault(Coordinate(p.x + 1, p.y + 1), null),
            map.getOrDefault(Coordinate(p.x - 1, p.y - 1), null),
            map.getOrDefault(Coordinate(p.x + 1, p.y - 1), null),
            map.getOrDefault(Coordinate(p.x - 1, p.y + 1), null),
        )
    }

    private fun flash(ocs : Collection<Octopus>, map: Map<Coordinate, Octopus>) : Long {
        return ocs.fold(0L) { acc, octopus ->
            if(octopus.power > 9 && !octopus.hasFlashed) {
                octopus.hasFlashed = true
                val adj = getAdjacent(octopus, map)
                adj.forEach { it.power++ }
                acc + 1 + flash(adj, map)
            } else acc
        }
    }

    fun part1(): Long {
        val map = getMap()
        val octopuses = map.values
        val result = (0..99).fold(0L) { acc, _ ->
            val flashedThisRound = turn(octopuses, map)
            acc + flashedThisRound
        }

        return result
    }

    private fun turn(octopuses: Collection<Octopus>, map: Map<Coordinate, Octopus>): Long {
        octopuses.forEach {
            it.power++
        }
        val flashedThisRound = flash(octopuses, map)
        octopuses.forEach {
            if (it.hasFlashed) {
                it.power = 0
                it.hasFlashed = false
            }
        }
        return flashedThisRound
    }

    fun part2(): Long {
        val map = getMap()
        val octopuses = map.values
        var round  = 0L
        while(true) {
            round++
            val flashedThisRound = turn(map.values, map)
            if(flashedThisRound == octopuses.size.toLong()) break
        }
        return round
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day11().solve()
}
