fun main() {
    fun part1(input: List<String>): Int {
        val cards = input.map { line ->
            val cardSplit = line.split(":")
            val cardId = cardSplit[0].replace("Card ", "").trim().toInt()
            val numberSplit = cardSplit[1].split("|")
            val winningNumbers = numberSplit[0].split(" ")
                .mapNotNull { num ->
                    num.trim().toIntOrNull()
                }
            val numbersPresent = numberSplit[1].split(" ")
                .mapNotNull { num ->
                    num.trim().toIntOrNull()
                }
            Card(
                cardId,
                winningNumbers,
                numbersPresent
            )
        }
        return cards.sumOf { it.calculatePoints().toInt() }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { line ->
            val cardSplit = line.split(":")
            val cardId = cardSplit[0].replace("Card ", "").trim().toInt()
            val numberSplit = cardSplit[1].split("|")
            val winningNumbers = numberSplit[0].split(" ")
                .mapNotNull { num ->
                    num.trim().toIntOrNull()
                }
            val numbersPresent = numberSplit[1].split(" ")
                .mapNotNull { num ->
                    num.trim().toIntOrNull()
                }
            Card(
                cardId,
                winningNumbers,
                numbersPresent
            )
        }.associate { card ->
            card.id to card
        }
        val sortedCards = cards.toSortedMap()
        sortedCards.forEach { cardId, card ->
            (1 .. card.amount).forEach{
                val newCardIds = card.calculateWonCardIds()
                newCardIds.forEach { incrementId ->
                    sortedCards[incrementId]?.let { cardToIncrement ->
                        cardToIncrement.amount++
                    }
                }
            }
        }
        return sortedCards.map { (_, card) -> card.amount }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}


data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val numbersPresent: List<Int>,
    var amount: Int = 1
) {
    fun calculatePoints(): Double {
        val pairs = numbersPresent.map { i ->
            if (winningNumbers.contains(i)) {
                1
            } else {
                0
            }
        }.sum()
        if (pairs == 0) {
            return 0.0
        }
        return Math.pow(2.0, pairs - 1.0)
    }

    fun calculateWonCardIds(): List<Int> {
        val pairs =  numbersPresent.map { i ->
            if (winningNumbers.contains(i)) {
                1
            } else {
                0
            }
        }.sum()
        return (1..pairs).map { it + id }
    }
}