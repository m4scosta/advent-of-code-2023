package com.marcos.adventofcode

import java.io.InputStream

internal interface Solution {
    val inputFileName: String

    fun solve(input: InputStream)
}