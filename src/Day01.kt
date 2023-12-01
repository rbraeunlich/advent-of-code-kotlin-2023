fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { line ->
                "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt()
            }.sum()
    }

    val numberStrings = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    fun toInt(second: String): Int =
        when (second) {
            "one" -> 1
            "two" -> 2
            "three" -> 3
            "four" -> 4
            "five" -> 5
            "six" -> 6
            "seven" -> 7
            "eight" -> 8
            "nine" -> 9
            else -> 0
        }

    fun part2(input: List<String>): Int {
        return input
            .map { line ->
                val first: Int =
                    if (line.indexOfFirst { it.isDigit() } != -1 && line.indexOfFirst { it.isDigit() } < line.indexOfAny(
                            numberStrings
                        ) || line.indexOfAny(numberStrings) == -1) {
                        line.first { it.isDigit() }.digitToInt()
                    } else {
                        toInt(line.findAnyOf(numberStrings)!!.second)
                    }
                val second: Int =
                    if (line.indexOfLast { it.isDigit() } != -1 && line.indexOfLast { it.isDigit() } > line.lastIndexOfAny(
                            numberStrings
                        ) || line.indexOfAny(numberStrings) == -1) {
                        line.last { it.isDigit() }.digitToInt()
                    } else {
                        toInt(line.findLastAnyOf(numberStrings)!!.second)
                    }
                "$first$second".toInt()
            }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_part2test")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
