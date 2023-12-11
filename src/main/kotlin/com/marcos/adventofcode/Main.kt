package com.marcos.adventofcode

import java.io.InputStream

class Main {
    companion  object {
        @JvmStatic
        fun main(args: Array<String>) {
            val solution = DayFivePart2()
            for (inputFileName in solution.inputFileNames) {
                println("Solving $inputFileName")
                solution.solve(readInputFile(inputFileName))
                println()
            }
        }

        private fun readInputFile(fileName: String): InputStream {
            return this::class.java.getResourceAsStream(fileName) ?: error("File not found: $fileName")
        }
    }
}