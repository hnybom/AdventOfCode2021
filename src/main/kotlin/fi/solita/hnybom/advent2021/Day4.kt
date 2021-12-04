package fi.solita.hnybom.advent2021

import fi.solita.hnybom.advent2021.utils.Helpers
import java.io.File

class Day4 {

    data class BoardNumber(var marked: Boolean, val number: Int)
    data class Board(val numbers: MutableList<List<BoardNumber>>) {

        fun isWinning() =
            numbers.map(winningRow()).any { it } || Helpers.transpose(numbers).map(winningRow()).any { it }

        fun calculateScore(number: Int): Int {
            val totalUnmarked =
                numbers.flatten().filter { !it.marked }.fold(0) { acc, boardNumber -> acc + boardNumber.number  }
            return totalUnmarked * number
        }

        private fun winningRow(): (List<BoardNumber>) -> Boolean = { row -> row.all { it.marked } }

        fun mark(number: Int) =
            numbers.forEach { row ->
                row.forEach {
                    if (it.number == number) it.marked = true
                }
            }
    }

    private val numbers: List<Int> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input4.txt")
            .readLines().first().split(",").map { it.toInt() }

    private val boards: List<Board>

    init {
        val allBoards = File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input4.txt").readLines().drop(2)

        boards = allBoards.fold(arrayListOf(Board(ArrayList()))) { boards, line ->
            if(line.isEmpty()) {
                boards.add(Board(ArrayList()))
            }
            else {
                val currentBoard = boards.last()
                val boardNumbers = line
                    .split(" ")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { BoardNumber(false, it.toInt()) }
                currentBoard.numbers.add(boardNumbers)
            }
            boards
        }
    }

    fun part1() {
        numbers.takeWhile { number ->
            boards.forEach {
                it.mark(number)
            }

            val winningBoard = boards.find { it.isWinning() }
            winningBoard?.let {
                println("Winning number " + it.calculateScore(number))
            }
            winningBoard == null
        }

    }

    fun part2() {
        val winningPerRound = numbers.mapIndexedNotNull { index,  number ->
            val winningThisRound = boards.mapNotNull {
                val isWinningBefore = it.isWinning()
                if(!isWinningBefore) {
                    it.mark(number)
                }

                if(it.isWinning() && !isWinningBefore) {
                    it
                } else {
                    null
                }
            }
            number to winningThisRound
        }

        val lastToWin = winningPerRound.findLast { it.second.isNotEmpty() }
        println("Last to win: " + lastToWin?.second?.first()?.calculateScore(lastToWin.first))
    }

    fun solve() {
        part1()
        part2()
    }
}

fun main(args: Array<String>) {
    Day4().solve()
}
