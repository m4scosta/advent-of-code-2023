package com.marcos.adventofcode

object Helpers {
    fun <T> parseList(str: String, delimiter: String = " ", itemParser: (String) -> T): List<T> {
        return str
            .trim()
            .split(delimiter)
            .filter(String::isNotBlank)
            .map(itemParser)
    }

    fun List<Int>.product(): Int {
        return this.reduce { acc, n -> acc * n }
    }

    fun List<Int>.productOrDefault(default: () -> Int): Int {
        return this.reduceOrNull { acc, n -> acc * n } ?: default()
    }
}