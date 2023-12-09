package com.marcos.adventofcode

import java.io.InputStream

internal interface Solution {
    val inputFileNames: List<String>

    fun solve(input: InputStream)
}