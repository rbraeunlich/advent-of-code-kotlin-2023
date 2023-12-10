fun main() {
    fun calculateLineDifferenrences(line: List<Long>): List<Long> {
        return line.windowed(2).map { (first, second) ->  second - first}
    }

    fun getLastLineDifference(line: List<Long>): Long {
        val diffList: List<Long> = calculateLineDifferenrences(line)
//        println(diffList)
        return if (diffList.all { it == 0L }) {
            diffList.last()
        } else {
            val lastLineDiff = getLastLineDifference(diffList)
            val newNumber = diffList.last() + lastLineDiff
//            println(newNumber)
            newNumber
        }
    }

    fun part1(input: List<String>): Long {
        val sum = input.map { line ->
            val longs = line.split(" ").map { it.toLong() }
            val lastLineDifference = getLastLineDifference(longs)
            longs.last() + lastLineDifference
        }.sum()
//        println(sum)
        return sum
    }

    fun getFirstLineDifference(line: List<Long>): Long {
        val diffList: List<Long> = calculateLineDifferenrences(line)
//        println(diffList)
        return if (diffList.all { it == 0L }) {
            diffList.first()
        } else {
            val firstLineDiff = getFirstLineDifference(diffList)
            val newNumber = diffList.first() - firstLineDiff
            println(newNumber)
            newNumber
        }
    }

    fun part2(input: List<String>): Long {
        val sum = input.map { line ->
            val longs = line.split(" ").map { it.toLong() }
            val lastLineDifference = getFirstLineDifference(longs)
            longs.first() - lastLineDifference
        }.sum()
//        println(sum)
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
