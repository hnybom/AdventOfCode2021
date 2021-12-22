package fi.solita.hnybom.advent2021

import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

class Day18 {

    data class NumberPair(
        var leaf: Long?,
        var parent: NumberPair?,
        var left: NumberPair?,
        var right: NumberPair?) {

        fun isLeaf() = leaf != null

        override fun equals(other: Any?): Boolean {
            return this === other // just compare references
        }

        override fun hashCode() = EssentialData(this).hashCode()
        override fun toString() : String {
            return if(isLeaf()) leaf.toString()
            else "[" + left?.toString() + "," + right?.toString() + "]"
        }
    }

    private data class EssentialData(val leaf: Long?) {
        constructor(number: NumberPair) : this(leaf = number.leaf)
    }

    enum class OP {
     EXP, SPLT
    }

    private fun initInput(): List<NumberPair> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input18.txt")
            .readLines().map { parse(it, null) }

    private fun parse(str: String, parent: NumberPair?): NumberPair {
        val c = str.first()
        val rest = str.drop(1)
        return if(c == '[') {
            val left = parse(rest, null)
            var openCount = 0
            var rightLoc = rest.dropWhile {
                if(it == '[') openCount++
                else if(it == ']') openCount--
                openCount != 0 || it != ','
            }.drop(1)
            val right = parse(rightLoc, parent)
            val current = NumberPair(null, parent, left, right)
            left.parent = current
            right.parent = current
            current
        } else if(c.isDigit()) {
            NumberPair(c.toString().toLong(), parent, null, null)
        } else {
            throw IllegalArgumentException("Unexpected char: $c")
        }
    }

    private fun findNextNumberLeft(number: NumberPair?, lastLeft: NumberPair): NumberPair? {
        return if(number == null) null
        else if(number.left?.isLeaf() == true) number.left
        else if(number.left == lastLeft) findNextNumberLeft(number.parent, number)
        else findRightLeaf(number.left!!)
    }

    private fun findNextNumberRight(number: NumberPair?, lastRight: NumberPair): NumberPair? {
        return if(number == null) null
        else if(number.right?.isLeaf() == true) number.right
        else if(number.right == lastRight) findNextNumberRight(number.parent, number)
        else findLeftLeaf(number.right!!)
    }

    private tailrec fun findLeftLeaf(number: NumberPair): NumberPair {
        return if(number.left?.isLeaf() == true) number.left!!
        else findLeftLeaf(number.left!!)
    }

    private tailrec fun findRightLeaf(number: NumberPair): NumberPair {
        return if(number.right?.isLeaf() == true) number.right!!
        else findRightLeaf(number.right!!)
    }

    private fun explode(number: NumberPair) {
        val left = number.left!!
        val nextLeft = findNextNumberLeft(number.parent, number)
        if(nextLeft != null) {
            nextLeft.leaf = left.leaf!! + nextLeft.leaf!!
        } else {
            number.parent!!.left = NumberPair(0, number.parent, null, null)
        }

        val right = number.right!!
        val nextRight = findNextNumberRight(number.parent, number)
        if(nextRight != null) {
            nextRight.leaf = right.leaf!! + nextRight.leaf!!
        } else {
            number.parent!!.right = NumberPair(0, number.parent, null, null)
        }

        if(number.parent?.left == number) {
            number.parent!!.left = NumberPair(0, number.parent, null, null)
        }

        if(number.parent?.right == number) {
            number.parent!!.right = NumberPair(0, number.parent, null, null)
        }

        number.parent = null
    }

    fun split(number: NumberPair) {
        val newLeft = floor(number.leaf!! / 2.0).toLong()
        val newRight = ceil(number.leaf!! / 2.0).toLong()
        number.leaf = null
        number.left = NumberPair(newLeft, number, null, null)
        number.right = NumberPair(newRight, number, null, null)
    }

    private fun process(number: NumberPair) {

        fun findFirstActionablePair(number: NumberPair?, depth: Int): Pair<NumberPair, OP>? {
            fun findExplodingPair(number: NumberPair?, depth: Int): Pair<NumberPair, OP>? {
                if(number == null) return null
                if(depth == 5 && number.leaf == null) return number to OP.EXP
                findExplodingPair(number.left, depth + 1)?.let { return it }
                findExplodingPair(number.right, depth + 1)?.let { return it }
                return null
            }
            fun findSplitPair(number: NumberPair?, depth: Int): Pair<NumberPair, OP>? {
                if(number == null) return null
                if((number.leaf ?: 0) >= 10) return number to OP.SPLT
                findSplitPair(number.left, depth + 1)?.let { return it }
                findSplitPair(number.right, depth + 1)?.let { return it }
                return null
            }

            findExplodingPair(number, depth)?.let { return it }
            findSplitPair(number, depth)?.let { return it }
            return null
        }

        tailrec fun step(number: NumberPair) {
            val firstProcessable = findFirstActionablePair(number, 1) ?: return
            when(firstProcessable.second) {
                OP.EXP -> explode(firstProcessable.first)
                OP.SPLT -> split(firstProcessable.first)
            }
            step(number)
        }
        step(number)
    }

    private fun magnitude(current: NumberPair): Long {
        if(current.leaf != null) return current.leaf!!
        val magnitudeLeft = magnitude(current.left!!)
        val magnitudeRight = magnitude(current.right!!)
        return magnitudeLeft * 3 + magnitudeRight * 2
    }

    fun part1(): Long {

        val r = initInput().reduce { acc, numberPair ->
            val root = NumberPair(
                leaf = null,
                parent = null,
                left = acc,
                right = numberPair)
            acc.parent = root
            numberPair.parent = root
            process(root)
            root
        }
        return magnitude(r)
    }

    fun part2(): Long {
        val initInput = initInput()
        val range = initInput.size - 1

        val sums = (0..range).flatMapIndexed { i, _ ->
            (0..range).mapIndexedNotNull { j, _ ->
                if(i == j) null
                else {
                    val newValues = initInput()
                    val left = newValues[i]
                    val right = newValues[j]
                    val add = NumberPair(
                        leaf = null,
                        parent = null,
                        left = left,
                        right = right
                    )
                    left.parent = add
                    right.parent = add
                    process(add)
                    magnitude(add)
                }
            }
        }
        return sums.maxOf { it }
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day18().solve()
}