package com.marcos.adventofcode

import java.io.InputStream

/**
 */
internal class Day9Part2 : Solution {
    override val inputFileNames = listOf(
        "/dayNineInput-example.txt",
        "/dayNineInput.txt",
    )

    override fun solve(input: InputStream) {
        input.bufferedReader().lines()
            .map { Helpers.parseList(it, itemParser = String::toLong) }
            .mapToLong { predictPreviousValue(it) }
            .sum()
            .also { println(it) }
    }

    private fun predictPreviousValue(numbers: List<Long>): Long {
        if (numbers.distinct().size == 1) {
            return numbers.first()
        }

        val nextLevel = numbers.zipWithNext().map { it.second - it.first }
        return numbers.first() - predictPreviousValue(nextLevel)
    }
}
