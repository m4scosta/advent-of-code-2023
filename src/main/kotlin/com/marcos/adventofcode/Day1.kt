package com.marcos.adventofcode

import java.io.InputStream

internal class Day1 : Solution {
    override val inputFileNames = listOf(
        "/dayOneInput.txt",
    )

    override fun solve(input: InputStream) {
        val result = input.bufferedReader().lines()
            .mapToInt { findFirstAndLastDigitsInInputString(it) }
            .sum()

        println(result)
    }

    private fun findFirstAndLastDigitsInInputString(input: String): Int {
        return StringBuilder()
            .append(input.first { it.isDigit() })
            .append(input.last { it.isDigit() })
            .toString()
            .toInt()
    }
}