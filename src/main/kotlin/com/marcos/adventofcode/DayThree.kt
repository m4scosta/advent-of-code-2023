package com.marcos.adventofcode

import java.io.InputStream

/**
 * --- Day 3: Gear Ratios ---
 * You and the Elf eventually reach a gondola lift station; he says the gondola lift will take you up to the water
 * source, but this is as far as he can bring you. You go inside.
 *
 * It doesn't take long to find the gondolas, but there seems to be a problem: they're not moving.
 *
 * "Aaah!"
 *
 * You turn around to see a slightly-greasy Elf with a wrench and a look of surprise. "Sorry, I wasn't expecting anyone!
 * The gondola lift isn't working right now; it'll still be a while before I can fix it." You offer to help.
 *
 * The engineer explains that an engine part seems to be missing from the engine, but nobody can figure out which one.
 * If you can add up all the part numbers in the engine schematic, it should be easy to work out which part is missing.
 *
 * The engine schematic (your puzzle input) consists of a visual representation of the engine. There are lots of numbers
 * and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a
 * "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)
 *
 * Here is an example engine schematic:
 *
 * 467..114..
 * ...*......
 * ..35..633.
 * ......#...
 * 617*......
 * .....+.58.
 * ..592.....
 * ......755.
 * ...$.*....
 * .664.598..
 *
 * In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58
 * (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.
 *
 * Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine
 * schematic?
 */
internal class DayThree : Solution {
    override val inputFileName = "/dayThreeInput.txt"

    override fun solve(input: InputStream) {
        val engineSchematic = input.bufferedReader().lines()
            .map { it.toCharArray() }
            .toList()

        var partNumbersSum = 0

        for (x in engineSchematic.indices) {
            val row = engineSchematic[x]

            var y = 0
            while (y < row.size) {
                val currentNumber = StringBuilder()
                var adjacentToASymbol = false

                while (engineSchematic.isWithinBounds(x, y) && engineSchematic[x][y].isDigit()) {
                    currentNumber.append(engineSchematic[x][y])
                    adjacentToASymbol = adjacentToASymbol || isAdjacentToASymbol(engineSchematic, x, y)
                    y += 1
                }

                if (currentNumber.isNotBlank() && adjacentToASymbol) {
                    partNumbersSum += currentNumber.toString().toInt()
                }

                y += 1
            }
        }

        println(partNumbersSum)
    }

    private fun isAdjacentToASymbol(engineSchematic: List<CharArray>, x: Int, y: Int): Boolean {
        return getAdjacentCoordinates(x, y)
            .filter { adj -> engineSchematic.isWithinBounds(adj.first, adj.second) }
            .map { (x, y) -> engineSchematic[x][y] }
            .any { !it.isDigit() && it != '.' }
    }

    private fun getAdjacentCoordinates(x: Int, y: Int): Set<Pair<Int, Int>> {
        return setOf(
            x - 1 to y,
            x - 1 to y - 1,
            x - 1 to y + 1,
            x + 1 to y,
            x + 1 to y - 1,
            x + 1 to y + 1,
            x to y - 1,
            x to y + 1,
        )
    }
}

private fun List<CharArray>.isWithinBounds(x: Int, y: Int): Boolean {
    return x >= 0 && y >= 0 && x < this.size && y < this.first().size
}