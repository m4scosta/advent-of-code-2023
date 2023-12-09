package com.marcos.adventofcode

import java.io.InputStream

/**
 */
internal class DaySix : Solution {
    override val inputFileNames = listOf(
        "/daySixInput.txt",
        "/daySixInput-part2.txt",
    )

    override fun solve(input: InputStream) {
        val parsedLines = input.bufferedReader().readLines()
            .map { line -> line.split(":").last() }
            .map { line -> Helpers.parseList(line, itemParser = String::toLong) }

        println(parsedLines)

        val races = parsedLines.first().zip(parsedLines.last())

        val productOfWaysToBeatRecord = races.map { (duration, currentRecord) ->
            (0..duration)
                .filter { speed ->
                    val remainingTime = duration - speed
                    val distance = remainingTime * speed
                    distance > currentRecord
                }.size
        }.reduce { acc, curr -> acc * curr }

        println(productOfWaysToBeatRecord)
    }
}
