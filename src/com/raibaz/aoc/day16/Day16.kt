package com.raibaz.aoc.day16

import readInput

enum class PacketType(private val type: Int? = null) {
    LITERAL(4),
    OPERATOR_PLUS(0),
    OPERATOR_PRODUCT(1),
    OPERATOR_MIN(2),
    OPERATOR_MAX(3),
    OPERATOR_GT(5),
    OPERATOR_LT(6),
    OPERATOR_EQ(7);

    companion object {
        fun byType(type: Int) = values().find { it.type == type } ?: error("Unsupported packet type $type")
    }

}

enum class PacketLengthType(private val type: Int) {
    LENGTH(0),
    COUNT(1);

    companion object {
        fun byType(type: Int) = values().find { it.type == type } ?: error("Unsupported")
    }

}

const val headerLength = 3 + 3 + 1

data class Packet(
    val version: Int,
    val type: PacketType,
    val lengthType: PacketLengthType,
    val payload: String,
    val subPackets: List<Packet> = listOf()
) {
    fun sumVersions(): Long = version + subPackets.sumOf { it.sumVersions() }
    fun packetLength(): Int {
        return if (type == PacketType.LITERAL) {
            headerLength - 1 + payload.length
        } else if (lengthType == PacketLengthType.LENGTH) {
            headerLength + 15 + subPackets.sumOf { it.packetLength() }
        } else {
            headerLength + 11 + subPackets.sumOf { it.packetLength() }
        }
    }
}

fun main() {
    // runPart1("Day16_example1", 16)
    // runPart1("Day16_example2", 12)
    // runPart1("Day16_example3", 23)
    // runPart1("Day16_example4", 31)
    // runPart1("Day16", null)

    runPart2("C200B40A82", 3)
    runPart2("04005AC33890", 54)
    runPart2("880086C3E88112", 7)
    runPart2("CE00C43D881120", 9)
    runPart2("D8005AC2A8F0", 1)
    runPart2("F600BC2D8F", 0)
    runPart2("9C005AC2F8F0", 0)
    runPart2("9C0141080250320F1802104A08", 1)

    val input = readInput("com/raibaz/aoc/day16/Day16").first()
    runPart2(input, null)
}

fun runPart1(fileName: String, expectedValue: Long?) {
    val input = readInput("com/raibaz/aoc/day16/$fileName").first().hexToBinaryString()
    val packet = input.parsePacket()
    val sum = packet.sumVersions()
    if (expectedValue == null || sum == expectedValue) {
        println("Ok, sum = $sum")
    } else {
        error("Error: sum = $sum, expected value = $expectedValue")
    }
}

fun runPart2(input: String, expectedValue: Long?) {
    val packet = input.hexToBinaryString().parsePacket()
    val parsed = packet.parse()
    if (expectedValue == null || parsed == expectedValue) {
        println("Ok, sum = $parsed")
    } else {
        error("Error: sum = $parsed, expected value = $expectedValue")
    }
}

fun String.parsePacket(): Packet {
    val version = take(3).binaryStringToInt()
    val packetType = PacketType.byType(drop(3).take(3).binaryStringToInt())
    val packetLengthType = PacketLengthType.byType(drop(6).take(1).toInt())

    val payload = when(packetType) {
        PacketType.LITERAL -> drop(headerLength - 1).computeLiteralPayload()
        else -> ""
    }
    when (packetType) {
        PacketType.LITERAL -> println("version = $version (${take(3)}), packet type = $packetType, payload = ${payload.parseLiteral()} (${payload})")
        else -> println("version = $version (${take(3)}), packet type = $packetType, length type = $packetLengthType")
    }
    val subPackets = when(packetType) {
        PacketType.LITERAL -> listOf()
        else -> parseSubpackets(packetLengthType)
    }


    return Packet(version, packetType, packetLengthType, payload, subPackets)
}

fun String.computeLiteralPayload(): String {
    val buf = StringBuilder()
    val payloadWithOnes = windowed(5, 5).takeWhile { it.first() == '1' }.joinToString("")

    val lastBits = drop(payloadWithOnes.length).take(5)
    val paddingSize = 5 - lastBits.length
    buf.append(payloadWithOnes)
    buf.append(lastBits)
    (0 until paddingSize).forEach { _ ->
        buf.append(0)
    }
    return buf.toString()
}

fun String.parseSubpackets(packetLengthType: PacketLengthType): List<Packet> {
    val subpacketsWithLength = drop(headerLength)
    return if (packetLengthType == PacketLengthType.LENGTH) {
        val subpacketLength = subpacketsWithLength.take(15).binaryStringToInt()
        val subPackets = subpacketsWithLength.drop(15)
        var bitsToParse = subpacketLength
        val ret = mutableListOf<Packet>()
        var lastLength = 0
        while(bitsToParse > headerLength) {
            val nextpacket = subPackets.drop(lastLength).take(subpacketLength).parsePacket()
            val length = nextpacket.packetLength()
            bitsToParse -= length
            lastLength += length
            ret.add(nextpacket)
        }
        ret
    } else {
        val subpacketCount = subpacketsWithLength.take(11).binaryStringToInt()
        println("Count = $subpacketCount")
        val subPackets = subpacketsWithLength.drop(11)
        var parsedBytes = 0
        (1 .. subpacketCount).map {
            val packet = subPackets.drop(parsedBytes).parsePacket()
            parsedBytes += packet.packetLength()
            packet
        }
    }
}

fun Packet.parse(): Long {
    return when (type) {
        PacketType.LITERAL -> payload.parseLiteral()
        PacketType.OPERATOR_PLUS -> subPackets.sumOf { it.parse() }
        PacketType.OPERATOR_PRODUCT -> subPackets.fold(1) { acc, packet -> acc * packet.parse() }
        PacketType.OPERATOR_MIN -> subPackets.minOf { it.parse() }
        PacketType.OPERATOR_MAX -> subPackets.maxOf { it.parse() }
        PacketType.OPERATOR_GT -> if(subPackets[0].parse() > subPackets[1].parse()) 1 else 0
        PacketType.OPERATOR_LT -> if(subPackets[0].parse() < subPackets[1].parse()) 1 else 0
        PacketType.OPERATOR_EQ -> if(subPackets[0].parse() == subPackets[1].parse()) 1 else 0

    }
}

fun String.parseLiteral(): Long = windowed(5, 5).map { it.drop(1) }.joinToString("").binaryStringToLong()

fun String.hexToBinaryString() = map {
    when(it) {
        '0' -> "0000"
        '1' -> "0001"
        '2' -> "0010"
        '3' -> "0011"
        '4' -> "0100"
        '5' -> "0101"
        '6' -> "0110"
        '7' -> "0111"
        '8' -> "1000"
        '9' -> "1001"
        'A' -> "1010"
        'B' -> "1011"
        'C' -> "1100"
        'D' -> "1101"
        'E' -> "1110"
        'F' -> "1111"
        else -> error("Unknown character $it")
    }
}.joinToString("")

fun String.binaryStringToInt() = toInt(2)

fun String.binaryStringToLong() =
    this.fold(0L) { acc, ch ->
        when (ch) {
            '1' -> acc * 2L + 1L
            else -> acc * 2L
        }
    }