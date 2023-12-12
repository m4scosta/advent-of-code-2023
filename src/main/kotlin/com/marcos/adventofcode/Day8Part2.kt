package com.marcos.adventofcode

import java.io.InputStream

/**
 */
internal class Day8Part2 : Solution {
    override val inputFileNames = listOf(
        "/dayEightInput-example3.txt",
        "/dayEightInput.txt",
    )

    private val elementLinkRegex = "([A-Z0-9]{3}) = \\(([A-Z0-9]{3}), ([A-Z0-9]{3})\\)".toRegex()

    override fun solve(input: InputStream) {
        val inputLines = input.bufferedReader().lines().iterator()

        val directions = inputLines.next()
        inputLines.next() // skip blank line

        val elementGraph = mutableMapOf<String, LRLink>()

        while (inputLines.hasNext()) {
            val line = inputLines.next()
            val elements = elementLinkRegex.matchEntire(line)!!.groupValues
            elementGraph[elements[1]] = LRLink(elements[2], elements[3])
        }

        var currentElements = elementGraph.keys.filter { it[2] == 'A' }

        currentElements
            .map { getStepsToReachZ(it, elementGraph, directions) }
            .toList()
            .also { println(it) }
            .let { calculateLeastCommonMultiple(it) }
            .also { println(it) }
    }

    private fun getStepsToReachZ(
        sourceElement: String,
        elementGraph: Map<String, LRLink>,
        directions: String,
    ): Long {
        var directionIndex = 0
        var steps = 0L
        var currentElement = sourceElement

        while (currentElement[2] != 'Z') {
            steps++
            val direction = directions[directionIndex]
            currentElement = if (direction == 'L') {
                elementGraph[currentElement]!!.left
            } else {
                elementGraph[currentElement]!!.right
            }
            directionIndex = (directionIndex + 1) % directions.length
        }

        return steps
    }

    private fun calculateLeastCommonMultiple(a: Long, b: Long): Long {
        fun calcLCM(x: Long, y: Long): Long {
            var a = x
            var b = y
            while (b != 0L) {
                val temp = b
                b = a % b
                a = temp
            }
            return a
        }

        return if (a == 0L || b == 0L) 0 else (a * b) / calcLCM(a, b)
    }

    // Função para calcular o MMC de múltiplos números
    private fun calculateLeastCommonMultiple(numeros: List<Long>): Long {
        if (numeros.isEmpty()) return 0
        var lcm = numeros[0]
        for (i in 1..<numeros.size) {
            lcm = calculateLeastCommonMultiple(lcm, numeros[i])
        }
        return lcm
    }


    private data class LRLink(
        val left: String,
        val right: String,
    )
}
