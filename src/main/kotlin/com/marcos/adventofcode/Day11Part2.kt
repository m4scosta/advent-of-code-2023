package com.marcos.adventofcode

import java.io.InputStream
import kotlin.math.abs
import kotlin.math.min

private const val EXPANSION_MULTIPLIER = 1000000

internal class Day11Part2 : Solution {
    override val inputFileNames = listOf(
        "/day11Input.txt",
        "/day11Input-example.txt"
    )

    override fun solve(input: InputStream) {
        val universe = input.bufferedReader().readLines().map { it.toCharArray().toList() }
        val galaxies = findGalaxies(universe)
        val distanceBetweenGalaxies = calculateDistanceBetweenAllGalaxies(galaxies)
        println(distanceBetweenGalaxies.values.sum())
    }

    private fun findGalaxies(universe: List<List<Char>>): Set<Galaxy> {
        val emptyRowsIndexes = universe.mapIndexed { index, row -> Pair(index, row) }
            .filter { row -> row.second.none { it == '#' } }
            .map { it.first }
        val emptyColumnsIndexes = universe.indices
            .map { index -> Pair(index, universe.map { it[index] }) }
            .filter { column -> column.second.none { it == '#' } }
            .map { it.first }
        val galaxies = mutableSetOf<Galaxy>()

        for (x in universe.indices) {
            for (y in universe[x].indices) {
                if (universe[x][y] == '#') {
                    val emptyRowsBeforeX = emptyRowsIndexes.filter { it < x }.size
                    val emptyColumnsBeforeY = emptyColumnsIndexes.filter { it < y }.size

                    galaxies.add(
                        Galaxy(
                            x.toLong() - emptyRowsBeforeX + emptyRowsBeforeX * EXPANSION_MULTIPLIER,
                            y.toLong() - emptyColumnsBeforeY + emptyColumnsBeforeY * EXPANSION_MULTIPLIER
                        )
                    )
                }
            }
        }

        return galaxies
    }

    private fun calculateDistanceBetweenAllGalaxies(
        galaxies: Iterable<Galaxy>,
    ): Map<Pair<Galaxy, Galaxy>, Long> {
        val distances = mutableMapOf<Pair<Galaxy, Galaxy>, Long>()

        for (galaxy in galaxies) {
            for (galaxy2 in galaxies) {
                if (galaxy == galaxy2 || distances.containsKey(Pair(galaxy2, galaxy))) {
                    continue
                }

                val distance = distanceBetweenGalaxies(galaxy, galaxy2)
                distances[Pair(galaxy, galaxy2)] = distance
            }
        }

        return distances
    }

    private fun distanceBetweenGalaxies(galaxy1: Galaxy, galaxy2: Galaxy): Long {
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

    private data class Galaxy(val x: Long, val y: Long)
}