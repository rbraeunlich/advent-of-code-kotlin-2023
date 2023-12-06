fun main() {
    fun part1(input: List<String>): Int {
        //starting speed of zero millimeters per millisecond.
        // For each whole millisecond you spend at the beginning of the race holding down the button,
        // the boat's speed increases by one millimeter per millisecond.
        val times = input.first().replace("Time:", "")
            .split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val distances = input.get(1).replace("Distance:", "")
            .split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val races = times.zip(distances)

        return races.map { (time, record) ->
            var winningCount = 0
            (1..<time).forEach { pressTime ->
                val remainingTime = time - pressTime
                val distanceTravelled = remainingTime * pressTime
                if (distanceTravelled > record) {
                    winningCount++
                }
            }
            winningCount
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        //starting speed of zero millimeters per millisecond.
        // For each whole millisecond you spend at the beginning of the race holding down the button,
        // the boat's speed increases by one millimeter per millisecond.
        val time = input.first().replace("Time:", "")
            .split(" ").filter { it.isNotBlank() }.joinToString(separator = "").toLong()
        val distance = input.get(1).replace("Distance:", "")
            .split(" ").filter { it.isNotBlank() }.joinToString(separator = "").toLong()

        var winningCount = 0L
        (1..<time).forEach { pressTime ->
            val remainingTime = time - pressTime
            val distanceTravelled = remainingTime * pressTime
            if (distanceTravelled > distance) {
                winningCount++
            }
        }
        return winningCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
