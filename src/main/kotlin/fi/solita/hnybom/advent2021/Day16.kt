package fi.solita.hnybom.advent2021

import java.io.File

fun binToInt(bin: List<Int>) =
    bin.joinToString("").toInt(2)

fun binToLong(bin: List<Int>) =
    bin.joinToString("").toLong(2)

class Day16 {

    private val input: List<Char> =
        File("/home/henriny/work/own/AdventOfCode2021/src/main/resources/input16.txt")
            .readLines()
            .flatMap { it.toCharArray().toList() }

    private val bin: List<Int> =
        input.flatMap { char ->
            val bin = Integer.toBinaryString(Integer.parseInt(char.toString(), 16)).map { it.toString().toInt() }
            val padding = (0..(3 - bin.size)).map { 0 }
            padding + bin
        }

    enum class PACKET_TYPE {
        OPERATION_BITS, OPERATION_COUNTS, LITERAL
    }

    data class Packet(
        val version: Int,
        val type: Int,
        val subPackets: List<List<Int>>,
        val childPackets : List<Packet>,
        val packetType: PACKET_TYPE) {

        fun bitLength() : Int {
            return getHeaderLength() + subPackets.sumOf { it.size } + childPackets.sumOf { it.bitLength() }
        }

        private fun getHeaderLength() = when(packetType) {
            PACKET_TYPE.OPERATION_BITS -> 22
            PACKET_TYPE.OPERATION_COUNTS -> 18
            PACKET_TYPE.LITERAL -> 6
        }

        fun sumVersions() : Long {
            return version + childPackets.sumOf { it.sumVersions() }
        }

        fun getLiterals() = binToLong(subPackets.map { it.drop(1) }.flatten())
    }

    private fun decode(bin: List<Int>) : Packet? {
        val versionBin = bin.take(3)
        val typeBin = bin.drop(3).take(3)

        return if(bin.all { it == 0 } || bin.isEmpty()) {
            null
        } else if(typeBin == listOf(1, 0, 0)) {
            return handleLiteral(bin, versionBin, typeBin)
        } else {
            val lengthType = bin.drop(6).take(1).first()
            val binsAfterLength = bin.drop(7)
            if (lengthType == 0) {
                handleBitLengthOperation(binsAfterLength, versionBin, typeBin)
            } else {
                handleCountBasedOperation(binsAfterLength, versionBin, typeBin)
            }
        }
    }

    private fun handleLiteral(
        bin: List<Int>,
        versionBin: List<Int>,
        typeBin: List<Int>
    ): Packet {
        val subs = getLiteralSubPackets(bin.drop(6))
        return Packet(
            binToInt(versionBin),
            binToInt(typeBin),
            subs, emptyList(), PACKET_TYPE.LITERAL
        )
    }

    private fun getLiteralSubPackets(bin: List<Int>) : List<List<Int>> {
        val subPacketBin = bin.take(5)
        return if(subPacketBin.first() == 0) {
            listOf(subPacketBin)
        } else {
            listOf(subPacketBin) + getLiteralSubPackets(bin.drop(5))
        }
    }

    private fun handleCountBasedOperation(
        binsAfterLength: List<Int>,
        versionBin: List<Int>,
        typeBin: List<Int>
    ): Packet {
        val subPacketCount = binToInt(binsAfterLength.take(11))

        val childPackets = (1..subPacketCount).fold(emptyList<Packet>()) { acc, _ ->
            val toDrop = acc.sumOf { it.bitLength() } + 11
            val subPacketBins = binsAfterLength.drop(toDrop)
            val c = decode(subPacketBins)
            if (c == null) acc
            else acc + c
        }

        return Packet(
            binToInt(versionBin),
            binToInt(typeBin),
            emptyList(),
            childPackets,
            PACKET_TYPE.OPERATION_COUNTS
        )
    }

    private fun handleBitLengthOperation(
        binsAfterLength: List<Int>,
        versionBin: List<Int>,
        typeBin: List<Int>
    ): Packet {
        val subPacketBits = binToInt(binsAfterLength.take(15))
        val childBins = binsAfterLength.drop(15).take(subPacketBits)
        val children = getChildPackets(childBins)
        return Packet(
            binToInt(versionBin),
            binToInt(typeBin),
            emptyList(),
            children,
            PACKET_TYPE.OPERATION_BITS
        )
    }

    private fun getChildPackets(childBins: List<Int>): List<Packet> {
        val child = decode(childBins)
        return if (child == null) {
            emptyList()
        } else {
            val still = childBins.drop(child.bitLength())
            listOf(child) + getChildPackets(still)
        }
    }

    fun part1(): Long {
        val p = decode(bin)
        return p?.sumVersions() ?: 0
    }

    fun part2(): Long {
        val p = decode(bin)
        return calculate(p!!)
    }

    private fun calculate(p: Packet) : Long {
        return when(p.type) {
            0 -> p.childPackets.sumOf { calculate(it) }
            1 -> p.childPackets.map { calculate(it) }.reduce{ acc, i -> acc * i }
            2 -> p.childPackets.map { calculate(it) }.minOf { it }
            3 -> p.childPackets.map { calculate(it) }.maxOf { it }
            4 -> p.getLiterals().toLong()
            5 -> if(calculate(p.childPackets[0]) > calculate(p.childPackets[1])) 1 else 0
            6 -> if(calculate(p.childPackets[0]) < calculate(p.childPackets[1])) 1 else 0
            7 -> if(calculate(p.childPackets[0]) == calculate(p.childPackets[1])) 1 else 0
            else -> throw IllegalStateException("Unknown type ${p.type}")
        }
    }

    fun solve() {
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main(args: Array<String>) {
    Day16().solve()
}
