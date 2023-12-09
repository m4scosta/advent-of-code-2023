package com.marcos.adventofcode

import java.io.InputStream

internal class DayOnePart2 : Solution {
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
        val numberBuilder = StringBuilder()
        var lastChars = ""
        var firstNumber: Char? = null
        var lastNumber: Char? = null

        for (char in input) {
            if (lastChars.length == 5) {
                lastChars = lastChars.substring(1)
            }

            lastChars += char

            val digit = if (char.isDigit()) {
                lastChars = ""
                char
            } else if (lastChars.isDigit()) {
                lastChars.toDigit()
            } else {
                null
            }

            if (digit != null) {
                if (firstNumber == null) {
                    firstNumber = digit
                    numberBuilder.append(digit)
                }
                lastNumber = digit
            }
        }

        val number = numberBuilder
            .append(lastNumber)
            .toString()
            .toInt()

        return number
    }

    private fun String.isDigit(): Boolean {
        return toDigit() != null
    }

    private fun String.toDigit(): Char? = when (this) {
        "one" -> '1'
        "two" -> '2'
        "three" -> '3'
        "four" -> '4'
        "five" -> '5'
        "six" -> '6'
        "seven" -> '7'
        "eight" -> '8'
        "nine" -> '9'
        else -> {
            if (length > 3) {
                substring(1).toDigit() ?: substring(0, length - 1).toDigit()
            } else {
                null
            }
        }
    }
}