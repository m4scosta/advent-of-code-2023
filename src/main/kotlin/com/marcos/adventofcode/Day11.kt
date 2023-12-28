package com.marcos.adventofcode

import org.assertj.core.api.Assertions.assertThat
import java.io.InputStream
import kotlin.math.abs
import kotlin.math.min

internal class Day11 : Solution {
    override val inputFileNames = listOf(
        "/day11Input.txt",
        "/day11Input-example.txt"
    )

    override fun solve(input: InputStream) {
        val universe = input.bufferedReader().readLines().map { it.toCharArray().toList() }
        val expandedUniverse = expandUniverse(universe)
        val galaxies = findGalaxies(expandedUniverse)
        val distanceBetweenGalaxies = mutableMapOf<Pair<Galaxy, Galaxy>, Int>()

        for (galaxy in galaxies) {
            for (galaxy2 in galaxies) {
                if (galaxy == galaxy2 || distanceBetweenGalaxies.containsKey(Pair(galaxy2, galaxy))) {
                    continue
                }

                val distance = distanceBetweenCoordinates(galaxy, galaxy2)
                distanceBetweenGalaxies[Pair(galaxy, galaxy2)] = distance
            }
        }

        println(distanceBetweenGalaxies.values.sum())
    }


    private fun expandUniverse(universe: List<List<Char>>): List<List<Char>> {
        val expandedUniverse = mutableListOf<MutableList<Char>>()

        for (row in universe) {
            expandedUniverse.add(row.toMutableList())
            if (row.none { it == '#' }) {
                expandedUniverse.add(row.toMutableList())
            }
        }

        var column = 0
        while (column < expandedUniverse[0].size) {
            if (expandedUniverse.none { it[column] == '#' }) {
                for (row in expandedUniverse) {
                    row.add(column, '.')
                }
                column++
            }
            column++
        }

        return expandedUniverse
    }

    private fun findGalaxies(universe: List<List<Char>>): Set<Galaxy> {
        val galaxies = mutableSetOf<Galaxy>()

        for (x in universe.indices) {
            for (y in universe[x].indices) {
                val galaxyGalaxy = Galaxy(x, y)
                if (universe[x][y] == '#') {
                    galaxies.add(galaxyGalaxy)
                }
            }
        }

        return galaxies
    }

    private fun distanceBetweenCoordinates(galaxy1: Galaxy, galaxy2: Galaxy): Int {
        return if (galaxy1.x == galaxy2.x) {
            abs(galaxy1.y - galaxy2.y)
        } else if (galaxy1.y == galaxy2.y) {
            abs(galaxy1.x - galaxy2.x)
        } else {
            val rowsBetween = abs(galaxy1.x - galaxy2.x)
            val columnsBetween = abs(galaxy1.y - galaxy2.y)

            val linearSteps = abs(rowsBetween - columnsBetween)
            val diagonalSteps = min(rowsBetween, columnsBetween) * 2

            diagonalSteps + linearSteps
        }
    }

    private data class Galaxy(val x: Int, val y: Int)

    private fun testDistanceBetweenCoordinates() {
        // same line
        assertThat(distanceBetweenCoordinates(Galaxy(0, 0), Galaxy(3, 0))).isEqualTo(3)
        assertThat(distanceBetweenCoordinates(Galaxy(3, 0), Galaxy(0, 0))).isEqualTo(3)

        // same column
        assertThat(distanceBetweenCoordinates(Galaxy(0, 0), Galaxy(0, 3))).isEqualTo(3)
        assertThat(distanceBetweenCoordinates(Galaxy(0, 3), Galaxy(0, 0))).isEqualTo(3)

        // square
        assertThat(distanceBetweenCoordinates(Galaxy(0, 0), Galaxy(3, 3))).isEqualTo(6)
        assertThat(distanceBetweenCoordinates(Galaxy(3, 3), Galaxy(0, 0))).isEqualTo(6)

        // rectangular
        assertThat(distanceBetweenCoordinates(Galaxy(0, 0), Galaxy(3, 4))).isEqualTo(7)
        assertThat(distanceBetweenCoordinates(Galaxy(3, 4), Galaxy(0, 0))).isEqualTo(7)

        // examples
        assertThat(distanceBetweenCoordinates(Galaxy(6, 1), Galaxy(11, 5))).isEqualTo(9)
        assertThat(distanceBetweenCoordinates(Galaxy(11, 5), Galaxy(6, 1))).isEqualTo(9)
        assertThat(distanceBetweenCoordinates(Galaxy(0, 0), Galaxy(10, 5))).isEqualTo(15)
    }
}