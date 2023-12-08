package com.marcos.adventofcode

import java.io.InputStream

class Main {
    companion  object {
        @JvmStatic
        fun main(args: Array<String>) {
            val solution = DayFive()
            val input = readInputFile(solution.inputFileName)
            solution.solve(input)
        }

        private fun readInputFile(fileName: String): InputStream {
            return this::class.java.getResourceAsStream(fileName) ?: error("File not found: $fileName")
        }
    }
}