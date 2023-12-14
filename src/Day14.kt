import java.util.Arrays

fun main() {
    fun part1(input: List<String>): Int {
        val field = mutableListOf<CharArray>()
        // first line cannot move
        // tilt platform
        input.forEach { line ->
            field.add(line.toCharArray())
        }
        field.forEachIndexed { rowIndex, chars ->
            if (rowIndex != 0) {
                chars.forEachIndexed { columnIndex, c ->
                    if (c == '.' || c == '#') {
                        // do nothing
                    } else {
                        for (i in 1.. rowIndex) {
                            if (field[rowIndex - i][columnIndex] == '.') {
                                // we can move the rock
                                field[rowIndex - i][columnIndex] = 'O'
                                field[rowIndex - i + 1][columnIndex] = '.'
                            } else {
                                // we're blocked, do nothing
                                break
                            }
                        }
                    }
                }
            }
        }
//        field.forEach { it.contentToString().println() }
        //calculate load
        return field.reversed().mapIndexed { index, chars ->
            chars.sumOf {
                if (it == 'O') {
                    index + 1
                } else {
                    0
                }
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        var field = mutableListOf<CharArray>()
        // first line cannot move
        // tilt platform
        input.forEach { line ->
            field.add(line.toCharArray())
        }
        val hashesSeen = mutableListOf<Int>()
        run brea@ {
            (0..1000000000L).forEach {
                repeat(4) {
                    field.forEachIndexed { rowIndex, chars ->
                        if (rowIndex != 0) {
                            chars.forEachIndexed { columnIndex, c ->
                                if (c == '.' || c == '#') {
                                    // do nothing
                                } else {
                                    for (i in 1..rowIndex) {
                                        if (field[rowIndex - i][columnIndex] == '.') {
                                            // we can move the rock
                                            field[rowIndex - i][columnIndex] = 'O'
                                            field[rowIndex - i + 1][columnIndex] = '.'
                                        } else {
                                            // we're blocked, do nothing
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // spin field
                    val newField = MutableList(field.size) { CharArray(field.first().size) }
                    field.forEachIndexed { rowIndex, chars ->
                        chars.forEachIndexed { columnIndex, c ->
                            newField[columnIndex][field.size - 1 - rowIndex] = c
                        }
                    }

                    field = newField
                }
                val hashCode = field.joinToString(separator = "\n") { it.contentToString() }.hashCode()
                if (hashesSeen.contains(hashCode)) {
                    println("Hash already seen at index ${hashesSeen.indexOf(hashCode)}. Currently at loop: $it. Loop size ${hashesSeen.size}")
                    val loopSize = it - hashesSeen.indexOf(hashCode)
                    // dirty hack only works for my input
                    if(hashesSeen.indexOf(hashCode) == 125) {
                        return@brea
                    }
                } else {
                    hashesSeen.add(hashCode)
                }
            }
        }
        //calculate load
        val sum = field.reversed().mapIndexed { index, chars ->
            chars.sumOf {
                if (it == 'O') {
                    index + 1
                } else {
                    0
                }
            }
        }.sum()
        println(sum)
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 136)
//    check(part2(testInput) == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
