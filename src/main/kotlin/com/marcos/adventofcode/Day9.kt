package com.marcos.adventofcode

import java.io.InputStream

/**
 */
internal class Day9 : Solution {
    override val inputFileNames = listOf(
        "/dayNineInput-example.txt",
        "/dayNineInput.txt",
    )

    override fun solve(input: InputStream) {
        input.bufferedReader().lines()
            .map { Helpers.parseList(it, itemParser = String::toLong) }
            .mapToLong { predictNextValue(it) }
            .sum()
            .also { println(it) }
    }

    private fun predictNextValue(numbers: List<Long>): Long {
        if (numbers.distinct().size == 1) {
            return numbers.first()
        }

        val nextLevel = numbers.zipWithNext().map { it.second - it.first }
        return numbers.last() + predictNextValue(nextLevel)
    }
}
