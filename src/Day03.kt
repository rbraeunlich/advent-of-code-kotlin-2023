fun main() {

    fun getNeighbourCoordinates(pair: Pair<Int, Int>): List<Pair<Int, Int>> {
        val x = pair.first
        val y = pair.second
        return listOf(
            x - 1 to y - 1,
            x - 1 to y,
            x - 1 to y + 1,
            x to y - 1,
            x to y + 1,
            x + 1 to y - 1,
            x + 1 to y,
            x + 1 to y + 1,
        )
    }

    fun isNextToSymbol(part: Part, symbols: List<Symbol>): Boolean {
        val positions = (part.startPosition.first..part.endPosition.first).map { it to part.startPosition.second }
        return positions.any { (x, y) ->
            val neighbours = getNeighbourCoordinates(x to y)
            symbols.any { symbol ->
                neighbours.contains(symbol.position)
            }
        }
    }

    fun part1(input: List<String>): Int {
        val plan = EnginePlan(
            mutableListOf()
        )
        input.forEachIndexed { lineIndex, line ->
            val lineWithSpaces = line.replace(".", " ")
            var currentNumber: Part? = null
            lineWithSpaces.mapIndexed { index, c ->
                if (c.isDigit() && currentNumber == null) {
                    currentNumber = Part("$c".toInt(), index to lineIndex, index to lineIndex)
                } else if (c.isDigit() && currentNumber != null) {
                    currentNumber!!.value = "${currentNumber!!.value}$c".toInt()
                    currentNumber!!.endPosition = index to lineIndex
                } else if (c.isWhitespace()) {
                    if (currentNumber != null) {
                        plan.parts.add(currentNumber!!)
                        currentNumber = null
                    } else {

                    }
                } else {
                    plan.parts.add(Symbol(index to lineIndex))
                    if (currentNumber != null) {
                        plan.parts.add(currentNumber!!)
                        currentNumber = null
                    }
                }
            }
            if (currentNumber != null) {
                plan.parts.add(currentNumber!!)
                currentNumber = null
            }

        }

        val partsOnly = plan.parts.filterIsInstance<Part>()
        val symbols = plan.parts.filterIsInstance<Symbol>()

        val partsNextToSymbol = partsOnly.filter { isNextToSymbol(it, symbols) }
        return partsNextToSymbol.sumOf { it.value }
    }

    fun hasTwoNumberNeighbours(asterisk: Symbol, partsOnly: List<Part>): Int {
        val neighbourCoordinates = getNeighbourCoordinates(asterisk.position)
        val neighbourParts = partsOnly.filter { part ->
            val positions = (part.startPosition.first..part.endPosition.first).map { it to part.startPosition.second }
            positions.any { neighbourCoordinates.contains(it) }
        }
        return if (neighbourParts.size == 2) {
            neighbourParts[0].value * neighbourParts[1].value
        } else {
            0
        }
    }

    fun part2(input: List<String>): Int {
        val plan = EnginePlan(
            mutableListOf()
        )
        input.forEachIndexed { lineIndex, line ->
            val lineWithSpaces = line.replace(".", " ")
            var currentNumber: Part? = null
            lineWithSpaces.mapIndexed { index, c ->
                if (c.isDigit() && currentNumber == null) {
                    currentNumber = Part("$c".toInt(), index to lineIndex, index to lineIndex)
                } else if (c.isDigit() && currentNumber != null) {
                    currentNumber!!.value = "${currentNumber!!.value}$c".toInt()
                    currentNumber!!.endPosition = index to lineIndex
                } else if (c.isWhitespace()) {
                    if (currentNumber != null) {
                        plan.parts.add(currentNumber!!)
                        currentNumber = null
                    } else {

                    }
                } else {
                    if (c == '*') {
                        plan.parts.add(Symbol(index to lineIndex))
                    }
                    if (currentNumber != null) {
                        plan.parts.add(currentNumber!!)
                        currentNumber = null
                    }
                }
            }
            if (currentNumber != null) {
                plan.parts.add(currentNumber!!)
                currentNumber = null
            }

        }

        val partsOnly = plan.parts.filterIsInstance<Part>()
        val asterisksOnly = plan.parts.filterIsInstance<Symbol>()

        return asterisksOnly.sumOf { asterisk ->
            hasTwoNumberNeighbours(asterisk, partsOnly)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

data class EnginePlan(val parts: MutableList<MapItem>)

sealed interface MapItem
data class Part(var value: Int, val startPosition: Pair<Int, Int>, var endPosition: Pair<Int, Int>) : MapItem
data class Symbol(val position: Pair<Int, Int>) : MapItem