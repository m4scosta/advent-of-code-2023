package com.marcos.adventofcode

import java.io.InputStream

/**
 */
internal class Day8 : Solution {
    override val inputFileNames = listOf(
        "/dayEightInput-example.txt",
        "/dayEightInput-example2.txt",
        "/dayEightInput.txt",
    )

    private val elementLinkRegex = "([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)".toRegex()

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

        var currentElement = "AAA"
        var directionIndex = 0
        var steps = 0
        while (currentElement != "ZZZ") {
            steps++
            val direction = directions[directionIndex]
            currentElement = if (direction == 'L') {
                elementGraph[currentElement]!!.left
            } else {
                elementGraph[currentElement]!!.right
            }
            directionIndex = (directionIndex + 1) % directions.length
        }

        println(steps)
    }

    private data class LRLink(
        val left: String,
        val right: String,
    )
}
