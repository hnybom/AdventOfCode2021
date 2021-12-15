package fi.solita.hnybom.advent2021

import java.io.File

class Day15 {

    data class Node(val x: Int, val y: Int, val risk: Int, val paths: MutableList<Node>) {
        override fun equals(other: Any?) : Boolean {
            if(other == null) return false
            if(other !is Node) return false
            return EssentialData(this) == EssentialData(other)
        }
        override fun hashCode() = EssentialData(this).hashCode()
        override fun toString() = EssentialData(this).toString()
    }

    private data class EssentialData(val x: Int, val y: Int, val risk: Int) {
        constructor(node: Node) : this(x = node.x, y = node.y, risk = node.risk)
    }

    private fun getAdjacent(p: Node, map: Map<Coordinate, Node>): List<Node> {
        return listOfNotNull(
            map.getOrDefault(Coordinate(p.x - 1, p.y), null),
            map.getOrDefault(Coordinate(p.x + 1, p.y), null),
            map.getOrDefault(Coordinate(p.x, p.y + 1), null),
            map.getOrDefault(Coordinate(p.x, p.y - 1), null)
        )
    }

    private val map: Map<Coordinate, Node> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input15.txt")
            .readLines()
            .flatMapIndexed { y, s -> s.mapIndexed { x, risk ->
                Coordinate(x, y) to Node(x, y, risk.toString().toInt(), mutableListOf())
            } }.toMap()

    private val expandedMap : Map<Coordinate, Node>

    init  {
        map.values.forEach {
            it.paths.addAll(getAdjacent(it, map))
        }

        val timesWidth = map.values.maxOf { it.x } + 1
        val timesHeight = map.values.maxOf { it.y } + 1

         val expand = (0..4).flatMap { h ->
            (0..4).flatMap { v ->
                map.map {
                    val newRisk = it.value.risk + h + v
                    val risk = if(newRisk > 9) (newRisk % 10) +1 else newRisk
                    val x = it.key.first + h * timesWidth
                    val y = it.key.second + v * timesHeight
                    Pair(
                        Coordinate(x, y),
                        Node(x, y, risk, mutableListOf())
                    )
                }
            }
        }

        expandedMap = expand.toMap()

        expandedMap.values.forEach {
            it.paths.addAll(getAdjacent(it, expandedMap))
        }
    }

    fun part1(): Long {
        val start = map[Coordinate(0, 0)]!!
        val end = map[Coordinate(map.values.maxOf { it.x }, map.values.maxOf { it.y })]!!
        return dijkstra(map, start, end)
    }

    fun part2(): Long {
        val start = expandedMap[Coordinate(0, 0)]!!
        val end = expandedMap[Coordinate(expandedMap.values.maxOf { it.x }, expandedMap.values.maxOf { it.y })]!!
        return dijkstra(expandedMap, start, end)
    }

    private fun dijkstra(map: Map<Coordinate, Node>, start: Node, end : Node) : Long {
        val costs = map.values.associateWith { Long.MAX_VALUE }.toMutableMap()
        val route = map.values.associateWith<Node, Node?> { null }.toMutableMap()

        costs[start] = 0
        val q = mutableListOf(start)

        while (q.isNotEmpty()) {
            val u = q.minByOrNull { costs[it]!! }!!
            q.remove(u)
            u.paths.forEach { v ->
                val alt = costs[u]!! + v.risk
                if (alt < costs[v]!!) {
                    costs[v] = alt
                    route[v] = u
                    if (v == end) {
                        return alt
                    }
                    q.add(v)
                }
            }
        }

        return 0L
    }

    fun solve() {
        //println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day15().solve()
}
