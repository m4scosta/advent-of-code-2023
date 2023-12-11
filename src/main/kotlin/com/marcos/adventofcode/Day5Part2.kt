package com.marcos.adventofcode

import java.io.InputStream
import kotlin.math.max
import kotlin.math.min

/**
 * --- Part Two ---
 * Everyone will starve if you only plant such a small number of seeds. Re-reading the almanac, it looks like the seeds:
 * line actually describes ranges of seed numbers.
 *
 * The values on the initial seeds: line come in pairs. Within each pair, the first value is the start of the range and
 * the second value is the length of the range. So, in the first line of the example above:
 *
 * seeds: 79 14 55 13
 *
 * This line describes two ranges of seed numbers to be planted in the garden. The first range starts with seed number
 * 79 and contains 14 values: 79, 80, ..., 91, 92. The second range starts with seed number 55 and contains 13 values:
 * 55, 56, ..., 66, 67.
 *
 * Now, rather than considering four seed numbers, you need to consider a total of 27 seed numbers.
 *
 * In the above example, the lowest location number can be obtained from seed number 82, which corresponds to soil 84,
 * fertilizer 84, water 84, light 77, temperature 45, humidity 46, and location 46. So, the lowest location number is 46.
 *
 * Consider all of the initial seed numbers listed in the ranges on the first line of the almanac. What is the lowest
 * location number that corresponds to any of the initial seed numbers?
 */
internal class Day5Part2 : Solution {
    override val inputFileNames = listOf(
        "/dayFiveInput-example.txt",
        "/dayFiveInput.txt",
    )

    override fun solve(input: InputStream) {
        val inputIterator = input.bufferedReader().lines().iterator()

        val seedRanges = parseSeedRanges(inputIterator.next())

        inputIterator.next() // skip blank line

        val categoryMapsPipeline = listOf(
            parseCategoryMap(inputIterator), // seed to soil
            parseCategoryMap(inputIterator), // soil to fertilizer
            parseCategoryMap(inputIterator), // fertilizer to water
            parseCategoryMap(inputIterator), // water to light
            parseCategoryMap(inputIterator), // light to temperature
            parseCategoryMap(inputIterator), // temperature to humidity
            parseCategoryMap(inputIterator), // humidity to location
        )

        seedRanges
            .minOf { seedRange ->
                categoryMapsPipeline.fold(listOf(seedRange)) { sourceRanges, categoryMap ->
                    sourceRanges.flatMap { getDestinationRanges(categoryMap, it) }
                }
                    .minOf { it.first }
            }
            .also { println(it) }
    }

    private fun getDestinationRanges(
        categoryMap: List<CategoryMapEntry>,
        sourceRange: LongRange
    ): List<LongRange> {
        val intersectingRanges = categoryMap.filter { sourceRange.hasIntersection(it.sourceRange) }

        if (intersectingRanges.isEmpty()) {
            return listOf(sourceRange)
        }

        var currRange = sourceRange
        val destinationRanges = mutableListOf<LongRange>()
        val intersectingRangesIterator = intersectingRanges.iterator()

        while (!currRange.isEmpty()) {
            if (!intersectingRangesIterator.hasNext()) {
                destinationRanges.add(currRange)
                break
            }

            val intersectingRange = intersectingRangesIterator.next()

            if (currRange.first < intersectingRange.sourceStart) {
                destinationRanges.add(LongRange(currRange.first, intersectingRange.sourceStart - 1))
            }

            destinationRanges.add(intersectingRange.getIntersectionDestinationRange(currRange))

            currRange = LongRange(intersectingRange.sourceRange.last + 1, currRange.last)
        }

        return destinationRanges
    }

    private fun parseSeedRanges(seedsLine: String): List<LongRange> {
        return Helpers.parseList(seedsLine.substring(7, seedsLine.length)) { it.toLong() }
            .withIndex()
            .groupBy(keySelector = { it.index / 2 }, valueTransform = { it.value })
            .values
            .map { LongRange(it.first(), it.first() + it[1]) }
    }

    private fun parseCategoryMap(inputIterator: Iterator<String>): List<CategoryMapEntry> {
        inputIterator.next() // skip map header
        val categoryMap = mutableListOf<CategoryMapEntry>()

        while (inputIterator.hasNext()) {
            val line = inputIterator.next()
            if (line.isBlank()) {
                break
            }

            val numbers: List<Long> = Helpers.parseList(line) { it.toLong() }
            categoryMap.add(
                CategoryMapEntry(
                    destinationStart = numbers[0],
                    sourceStart = numbers[1],
                    rangeSize = numbers[2],
                )
            )
        }

        return categoryMap.sortedBy { it.sourceStart }
    }

    private data class CategoryMapEntry(
        val destinationStart: Long,
        val sourceStart: Long,
        val rangeSize: Long,
    ) {
        val sourceRange = sourceStart..<sourceStart + rangeSize

        fun getIntersectionDestinationRange(otherRange: LongRange): LongRange {
            val intersection = otherRange.intersection(sourceRange)
            return LongRange(
                destinationStart + intersection.first - sourceStart,
                destinationStart + intersection.first - sourceStart + intersection.last - intersection.first,
            )
        }
    }
}

private fun LongRange.intersection(other: LongRange): LongRange {
    return LongRange(
        max(other.first, first),
        min(other.last, last),
    )
}

private fun LongRange.hasIntersection(other: LongRange): Boolean {
    return this.first in other || this.last in other || other.first in this || other.last in this
}