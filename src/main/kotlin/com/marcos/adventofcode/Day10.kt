package com.marcos.adventofcode

import java.io.InputStream
import java.util.Stack

internal class Day10 : Solution {
    override val inputFileNames = listOf(
        "/dayTenInput.txt",
        "/dayTenInput-example.txt",
        "/dayTenInput-example2.txt",
    )

    override fun solve(input: InputStream) {
        var start: Tile? = null
        val tilesGrid = input.bufferedReader().lines().toList().mapIndexed { x, line ->
            line.toCharArray().mapIndexed { y, char ->
                val tile = Tile(coordinate = Coordinate(x, y), type = TileType.fromChar(char))
                if (tile.type == TileType.START) {
                    start = tile
                }
                tile
            }
        }
        val loopSize = getLoopSize(tilesGrid, start!!)
        println(loopSize / 2)
    }

    private fun getLoopSize(
        tilesGrid: List<List<Tile>>,
        startTile: Tile,
    ): Int {
        val visitedTiles = mutableSetOf(startTile)
        val stack = Stack<Tile>()

        var loopSize = 0
        val tilesConnectedToStart = startTile.connectedCoordinates
            .mapNotNull { tilesGrid.getTile(it) }
            .filter { startTile.coordinate in it.connectedCoordinates }
        stack.addAll(tilesConnectedToStart)

        while (stack.isNotEmpty()) {
            val currentTile = stack.pop()

            if (currentTile.type == TileType.GROUND) {
                continue
            }

            visitedTiles.add(currentTile)
            loopSize += 1

            val connectedTiles = currentTile.connectedCoordinates.mapNotNull { tilesGrid.getTile(it) }
                .filter { it !in visitedTiles }

            stack.addAll(connectedTiles)
        }


        return loopSize
    }

    private data class Tile(
        val type: TileType,
        val coordinate: Coordinate,
    ) {
        val connectedCoordinates = type.connectedCoordinates.map {
            Coordinate(coordinate.x + it.x, coordinate.y + it.y)
        }
    }

    /**
     * | is a vertical pipe connecting north and south.
     * - is a horizontal pipe connecting east and west.
     * L is a 90-degree bend connecting north and east.
     * J is a 90-degree bend connecting north and west.
     * 7 is a 90-degree bend connecting south and west.
     * F is a 90-degree bend connecting south and east.
     * . is ground; there is no pipe in this tile.
     * S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape
     * the pipe has.
     */
    private enum class TileType(val char: Char, val connectedCoordinates: List<Coordinate>) {
        VERTICAL_PIPE('|', listOf(Coordinate(-1, 0), Coordinate(1, 0))),
        HORIZONTAL_PIPE('-', listOf(Coordinate(0, -1), Coordinate(0, 1))),
        NORTH_EAST_BEND('L', listOf(Coordinate(-1, 0), Coordinate(0, 1))),
        NORTH_WEST_BEND('J', listOf(Coordinate(-1, 0), Coordinate(0, -1))),
        SOUTH_WEST_BEND('7', listOf(Coordinate(1, 0), Coordinate(0, -1))),
        SOUTH_EAST_BEND('F', listOf(Coordinate(1, 0), Coordinate(0, 1))),
        START('S', listOf(Coordinate(1, 0), Coordinate(-1, 0), Coordinate(0, 1), Coordinate(0, -1))),
        GROUND('.', emptyList());

        companion object {
            fun fromChar(char: Char): TileType {
                return entries.first { it.char == char }
            }
        }
    }

    private data class Coordinate(
        val x: Int,
        val y: Int,
    )

    private fun List<List<Tile>>.getTile(coordinate: Coordinate): Tile? {
        if (!isWithinBounds(coordinate)) {
            return null
        }

        return this[coordinate.x][coordinate.y]
    }

    private fun List<List<Tile>>.isWithinBounds(coordinate: Coordinate): Boolean {
        return coordinate.x in indices && coordinate.y in first().indices
    }
}
