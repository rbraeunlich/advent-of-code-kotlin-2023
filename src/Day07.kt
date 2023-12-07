import java.util.Scanner

val cardValues = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()
val cardValuesPart2 = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed()

fun main() {

    fun part1(input: List<String>): Int {
        return input.map { line ->
            val (cards, bid) = line.split(" ")
            val cardList = cards.toList()
            CamelCard(cardList, bid.toInt())
        }.sorted()
            .mapIndexed { index, camelCard ->
                (index + 1) * camelCard.bid
            }.sum()
//        return input.size
        //each hand is followed by its bid amount.
        // Each hand wins an amount equal to its bid multiplied by its rank,
        // where the weakest hand gets rank 1, the second-weakest hand gets rank 2,
        // and so on up to the strongest hand.
    }

    fun part2(input: List<String>): Int {
         return input.map { line ->
            val (cards, bid) = line.split(" ")
            val cardList = cards.toList()
            CamelCardWithJoker(cardList, bid.toInt())
        }.sorted()
            .mapIndexed { index, camelCard ->
                (index + 1) * camelCard.bid
            }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)


    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

data class CamelCard(val hand: List<Char>, val bid: Int) : Comparable<CamelCard> {
    override fun compareTo(other: CamelCard): Int {
        if (this.getTypeAsNumber() > other.getTypeAsNumber()) {
            return 1
        } else if (this.getTypeAsNumber() < other.getTypeAsNumber()) {
            return -1
        } else {
            // compare card orders
            for ((index, card) in hand.withIndex()) {
                val ratingThis = cardValues.indexOf(card)
                val otherRating = cardValues.indexOf(other.hand[index])
                if (ratingThis > otherRating) {
                    return 1
                } else if (ratingThis < otherRating) {
                    return -1
                }
            }
            return 0
        }
    }

    fun getTypeAsNumber(): Int =
        if (this.isFiveOfAKind()) {
            7
        } else if (this.isFourOfAKind()) {
            6
        } else if (this.isFullHouse()) {
            5
        } else if (this.isThreeOfAKind()) {
            4
        } else if (this.isTwoPair()) {
            3
        } else if (this.isOnePair()) {
            2
        } else {
            // highCard
            1
        }

    private fun isOnePair(): Boolean {
        val groupBy = this.hand.groupBy { it }
        return groupBy.keys.size == 4 && groupBy.filterValues { it.size == 2 }.isNotEmpty()
    }

    private fun isTwoPair(): Boolean {
        val groupBy = this.hand.groupBy { it }
        return groupBy.keys.size == 3 && groupBy.filterValues { it.size == 2 }.isNotEmpty()
    }

    private fun isThreeOfAKind(): Boolean {
        val groupBy = this.hand.groupBy { it }
        return groupBy.keys.size == 3 && groupBy.filterValues { it.size == 3 }.isNotEmpty()
    }

    private fun isFullHouse(): Boolean {
        val groupBy = this.hand.groupBy { it }
        return groupBy.keys.size == 2 && groupBy.filterValues { it.size < 4 }.isNotEmpty()
    }

    private fun isFourOfAKind(): Boolean {
        return this.hand.groupBy { it }.filterValues { it.size == 4 }.isNotEmpty()
    }

    private fun isFiveOfAKind(): Boolean {
        return this.hand.toSet().size == 1
    }
}

data class CamelCardWithJoker(
    val hand: List<Char>,
    val bid: Int
) : Comparable<CamelCardWithJoker> {

    val jokerRating: Int

    init {
        jokerRating = calculateJokerRating()
//        kotlin.io.println("Hand: $hand JokerRating: $jokerRating")
    }

    private fun calculateJokerRating(): Int {
        // create Joker mutations of this card
        return (cardValuesPart2).mapNotNull { c ->
            if (c == 'J') {
                null
            } else {
                val jokerHand = hand.joinToString(separator = "").replace('J', c).toList()
                CamelCard(jokerHand, this.bid)
            }
        }.maxOrNull()
            ?.getTypeAsNumber()
            ?: this.getTypeAsNumber()
    }

    override fun compareTo(other: CamelCardWithJoker): Int {
        if (this.jokerRating > other.jokerRating) {
            return 1
        } else if (this.jokerRating < other.jokerRating) {
            return -1
        } else {
            // compare card orders
            for ((index, card) in hand.withIndex()) {
                val ratingThis = cardValuesPart2.indexOf(card)
                val otherRating = cardValuesPart2.indexOf(other.hand[index])
                if (ratingThis > otherRating) {
                    return 1
                } else if (ratingThis < otherRating) {
                    return -1
                }
            }
            return 0
        }
    }
    fun getTypeAsNumber(): Int =
        if (this.isFiveOfAKind()) {
            7
        } else if (this.isFourOfAKind()) {
            6
        } else if (this.isFullHouse()) {
            5
        } else if (this.isThreeOfAKind()) {
            4
        } else if (this.isTwoPair()) {
            3
        } else if (this.isOnePair()) {
            2
        } else {
            // highCard
            1
        }

    private fun isOnePair(): Boolean {
        val groupBy = this.hand.groupBy { it }
        return groupBy.keys.size == 4 && groupBy.filterValues { it.size == 2 }.isNotEmpty()
    }

    private fun isTwoPair(): Boolean {
        val groupBy = this.hand.groupBy { it }
        return groupBy.keys.size == 3 && groupBy.filterValues { it.size == 2 }.isNotEmpty()
    }

    private fun isThreeOfAKind(): Boolean {
        val groupBy = this.hand.groupBy { it }
        return groupBy.keys.size == 3 && groupBy.filterValues { it.size == 3 }.isNotEmpty()
    }

    private fun isFullHouse(): Boolean {
        val groupBy = this.hand.groupBy { it }
        return groupBy.keys.size == 2 && groupBy.filterValues { it.size < 4 }.isNotEmpty()
    }

    private fun isFourOfAKind(): Boolean {
        return this.hand.groupBy { it }.filterValues { it.size == 4 }.isNotEmpty()
    }

    private fun isFiveOfAKind(): Boolean {
        return this.hand.toSet().size == 1
    }
}