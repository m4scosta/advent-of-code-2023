package com.marcos.adventofcode

import java.io.InputStream

/**
 */
internal class Day7 : Solution {
    override val inputFileNames = listOf(
        "/daySevenInput-example.txt",
        "/daySevenInput.txt",
    )

    override fun solve(input: InputStream) {
        input.bufferedReader().lines()
            .toList()
            .map { parseHandAndBid(it) }
            .sorted()
            .mapIndexed { index, handAndBid -> handAndBid.bid * (index + 1) }
            .sum()
            .also(::println)
    }

    private fun parseHandAndBid(line: String): HandAndBid {
        val parts = line.split(" ")
        return HandAndBid(
            hand = Hand(parts[0]),
            bid = parts[1].toLong(),
        )
    }

    private data class HandAndBid(
        val hand: Hand,
        val bid: Long,
    ) : Comparable<HandAndBid> {
        override fun compareTo(other: HandAndBid): Int {
            return hand.compareTo(other.hand)
        }
    }

    private data class Hand(
        val cards: String,
    ) : Comparable<Hand> {
        val type = HandType.fromHandCards(cards)

        override fun compareTo(other: Hand): Int {
            if (type != other.type) {
                return type.strength - other.type.strength
            }

            for (i in cards.indices) {
                val thisCard = cards[i]
                val otherCard = other.cards[i]

                if (thisCard != otherCard) {
                    return getCardStrength(thisCard) - getCardStrength(otherCard)
                }
            }

            return 0
        }

        private fun getCardStrength(card: Char): Int = when (card) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            else -> card - '0'
        }
    }

    private enum class HandType(val strength: Int) {
        HIGH_CARD(0),
        ONE_PAIR(1),
        TWO_PAIR(2),
        THREE_OF_A_KIND(3),
        FULL_HOUSE(4),
        FOUR_OF_A_KIND(5),
        FIVE_OF_A_KIND(6);

        companion object {
            fun fromHandCards(handCards: String): HandType {
                require(handCards.length == 5) { "Incorrect hand cards size" }

                val cardCounts = handCards.groupingBy { it }.eachCount()
                val minCount = cardCounts.values.min()
                val maxCount = cardCounts.values.max()

                return when {
                    cardCounts.size == 1 -> FIVE_OF_A_KIND
                    cardCounts.size == 2 && minCount == 1 -> FOUR_OF_A_KIND
                    cardCounts.size == 2 && minCount == 2 -> FULL_HOUSE
                    cardCounts.size == 3 && maxCount == 3 -> THREE_OF_A_KIND
                    cardCounts.size == 3 && maxCount == 2 -> TWO_PAIR
                    cardCounts.size == 4 && maxCount == 2 -> ONE_PAIR
                    else -> HIGH_CARD
                }
            }
        }
    }
}
