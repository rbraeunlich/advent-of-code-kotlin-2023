import kotlin.math.abs

fun main() {
    fun expandUniverse(universe: MutableList<MutableList<Char>>) {
        val rowsToExpand = mutableListOf<Int>()
        // row expansion
        for (i in 0..<universe.size) {
            val row = universe[i]
            if (row.all { it == '.' }) {
                // expand row
                rowsToExpand.add(i)
            }
        }
        rowsToExpand.forEachIndexed { index, row ->
            universe.add(row + index, MutableList(universe[0].size) { '.' })
        }
        val columnsToExpand = mutableListOf<Int>()
        // column expansion
        for (column in 0..<universe[0].size) {
            var c = ""
            for (row in 0..<universe.size) {
                c += universe[row][column]
            }
            if (c.all { it == '.' }) {
                // expand column
                columnsToExpand.add(column)
            }
        }
        columnsToExpand.forEachIndexed { index, column ->
            for (row in 0..<universe.size) {
                universe[row].add(column + index, '.')
            }
        }
        universe.forEach {
            it.joinToString(separator = "").println()
        }
    }

    fun part1(input: List<String>): Int {
        val universe = input.map { line ->
            line.toCharArray().toMutableList()
        }.toMutableList()
        expandUniverse(universe)
        val galaxies = universe.mapIndexed { row, rowLine ->
            rowLine.mapIndexed { column, char ->
                if (char == '#') {
                    Universe(row to column)
                } else {
                    null
                }
            }
        }.flatten().filterNotNull()
        var distanceSum = 0
        for (i in galaxies.indices) {
            for (j in galaxies.indices) {
                if (j <= i) {
                    continue
                } else {
                    val currentGalaxy = galaxies[i]
                    val distanceGalaxy = galaxies[j]
                    val distance =
                        abs(currentGalaxy.position.first - distanceGalaxy.position.first) + abs(currentGalaxy.position.second - distanceGalaxy.position.second)
                    distanceSum += distance
                }
            }
        }
        return distanceSum
    }

    fun expandUniverseMillons(universe: MutableList<MutableList<Char>>): Pair<MutableList<Int>, MutableList<Int>> {
        val rowsToExpand = mutableListOf<Int>()
        // row expansion
        for (i in 0..<universe.size) {
            val row = universe[i]
            if (row.all { it == '.' }) {
                // expand row
                rowsToExpand.add(i)
            }
        }
//        rowsToExpand.forEachIndexed { index, row ->
//            repeat(1000000) { i ->
//                universe.add(row + (index * 1000000) + i, MutableList(universe[0].size) { '.' })
//            }
//        }
        val columnsToExpand = mutableListOf<Int>()
        // column expansion
        for (column in 0..<universe[0].size) {
            var c = ""
            for (row in 0..<universe.size) {
                c += universe[row][column]
            }
            if (c.all { it == '.' }) {
                // expand column
                columnsToExpand.add(column)
            }
        }
//        columnsToExpand.forEachIndexed { index, column ->
//            repeat(1000000) { i ->
//                for (row in 0..<universe.size) {
//                    universe[row].add(column + (index * 1000000) + i, '.')
//                }
//            }
//        }
        return rowsToExpand to columnsToExpand
    }

    fun part2(input: List<String>): Long {
        val universe = input.map { line ->
            line.toCharArray().toMutableList()
        }.toMutableList()
        val (rowsToExpand, columnsToExpand) = expandUniverseMillons(universe)
        val galaxies = universe.mapIndexed { row, rowLine ->
            rowLine.mapIndexed { column, char ->
                if (char == '#') {
                    val additionalRows = rowsToExpand.count { row > it } * (1000000-1)
                    val additionalColumns = columnsToExpand.count { column > it } * (1000000-1)
                    Universe(row + additionalRows to column + additionalColumns)
                } else {
                    null
                }
            }
        }.flatten().filterNotNull()
        var distanceSum = 0L
        for (i in galaxies.indices) {
            for (j in galaxies.indices) {
                if (j <= i) {
                    continue
                } else {
                    val currentGalaxy = galaxies[i]
                    val distanceGalaxy = galaxies[j]
                    val distance =
                        abs(currentGalaxy.position.first - distanceGalaxy.position.first) + abs(currentGalaxy.position.second - distanceGalaxy.position.second)
                    distanceSum += distance
                }
            }
        }
        return distanceSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)
//    check(part2(testInput) == 1030L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

data class Universe(
    /**
     * Row to column, NOT x to y
     */
    val position: Pair<Int, Int>
)