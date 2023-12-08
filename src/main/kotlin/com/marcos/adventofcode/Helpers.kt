package com.marcos.adventofcode

object Helpers {
    fun <T> parseList(str: String, delimiter: String = " ", itemParser: (String) -> T): List<T> {
        return str
            .trim()
            .split(delimiter)
            .filter(String::isNotBlank)
            .map(itemParser)
    }
}