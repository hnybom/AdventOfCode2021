package fi.solita.hnybom.advent2021

import java.io.File

class Day17 {

    data class Momentum(val position: Coordinate, val velocity: Coordinate, val isInTarget: Boolean = false)

    private val input: String =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input17.txt")
            .readLines()
            .first()

    private val targetRegex = ".*x=(-*\\d+..-*\\d+).*y=(-*\\d+..-*\\d+)".toRegex()

    private val targetXmin : Int
    private val targetXmax : Int
    private val targetYmin : Int
    private val targetYmax : Int

    init {
        val groupValues = targetRegex.find(input)?.groupValues
        val targetPairs = groupValues!!.drop(1).map { s ->
            val split = s.split("..")
            Pair(split[0], split[1])
        }

        targetXmin = targetPairs[0].first.toInt()
        targetXmax = targetPairs[0].second.toInt()
        targetYmin = targetPairs[1].first.toInt()
        targetYmax = targetPairs[1].second.toInt()
    }

    private fun isInTarget(x: Int, y: Int) : Boolean {
        return x in targetXmin..targetXmax && y in targetYmin..targetYmax
    }

    private fun step(m: Momentum) : Momentum {
        val nextPos = Coordinate(m.position.first + m.velocity.first, m.position.second + m.velocity.second)

        val velocityX =
            if(m.velocity.first > 0) m.velocity.first - 1
            else if(m.velocity.first < 0) m.velocity.first + 1
            else m.velocity.first

        val velocityY = m.velocity.second - 1

        val isInTarget = isInTarget(nextPos.first, nextPos.second)
        return Momentum(nextPos, Coordinate(velocityX, velocityY), isInTarget)
    }

    private fun simulate(m: Momentum, stop : (Momentum) -> Boolean) : List<Momentum> {
        val trajectory = ArrayList<Momentum>()
        var step = step(m)
        trajectory.add(step)
        while (!stop(step)) {
            step = step(step)
            trajectory.add(step)
        }
        return trajectory
    }

    fun part1(): Int {
        var maxY = Int.MIN_VALUE

        (0..1000).forEach { x ->
            (-100..100).forEach { y ->
                val trajectory = simulate(Momentum(Coordinate(0, 0), Coordinate(x, y))) { m ->
                            m.isInTarget
                            || targetYmin > m.position.second
                            || (m.velocity.first <= 0 && m.position.first < targetXmin)
                }
                if(trajectory.find { it.isInTarget } != null) {
                    trajectory.maxOf { it.position.second }?.let {
                        if(maxY < it) {
                            maxY = it
                        }
                    }
                }
            }
        }


        return maxY
    }

    fun part2(): Int {
        val hits = ArrayList<Coordinate>()

        (0..1000).forEach { x ->
            (-100..100).forEach { y ->
                val trajectory = simulate(Momentum(Coordinate(0, 0), Coordinate(x, y))) { m ->
                    m.isInTarget
                            || targetYmin > m.position.second
                            || (m.velocity.first <= 0 && m.position.first < targetXmin)
                }
                if(trajectory.find { it.isInTarget } != null) {
                    hits.add(Coordinate(x, y))
                }
            }
        }

        return hits.size
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day17().solve()
}
