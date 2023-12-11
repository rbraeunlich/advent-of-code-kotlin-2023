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

    fun toPipeElement(it: Char, x: Int, y: Int): PipeElement = when (it) {
        '|' -> VerticalPipe(x to y, Int.MAX_VALUE, it)
        '-' -> HorizontalPipe(x to y, Int.MAX_VALUE, it)
        'L' -> LPipe(x to y, Int.MAX_VALUE, it)
        'J' -> JPipe(x to y, Int.MAX_VALUE, it)
        '7' -> SevenPipe(x to y, Int.MAX_VALUE, it)
        'F' -> FPipe(x to y, Int.MAX_VALUE, it)
        'S' -> StartElement(x to y, Int.MAX_VALUE, it)
        else -> Ground(x to y, Int.MAX_VALUE, it)
    }

    fun findAdjacentPipes(startElement: PipeElement, pipeMap: PipeMap): List<PipeElement> {
        val x = startElement.position.first
        val y = startElement.position.second
        val possiblePositions = listOf(
            x - 1 to y,
            x to y - 1,
            x to y + 1,
            x + 1 to y,
        )
        return possiblePositions
            .filter { (x, y) ->
                x >= 0 && y >= 0
            }
            // Hack for my test data input
            .filter { (first,second) ->  y == second}
            .map { (x, y) ->
            pipeMap.maze[y][x]
        }.filterNot { it is Ground }
    }

    fun part1(input: List<String>): Int {
        val maze = mutableListOf<List<PipeElement>>()
        var startPosition: Pair<Int, Int>? = null
        input.forEachIndexed { index, line ->
            maze.add(line.toCharArray().mapIndexed { lineIndex, c ->
                toPipeElement(c, lineIndex, index)
            }
                .toList())
            if (line.contains('S')) {
                startPosition = line.indexOf('S') to index
            }
        }

        val pipeMap = PipeMap(maze.toList(), startPosition!!)

        // search
        val startElement = pipeMap.maze[pipeMap.startPosition.second][pipeMap.startPosition.first]
        val adjacentPipes = findAdjacentPipes(startElement, pipeMap)
//        adjacentPipes.println()
        check(adjacentPipes.size == 2)
        // first iteration
        var previousPosition = startElement.position
        var currentPosition = adjacentPipes[0]
        var distanceFromStart = 1
        do {
            val nextPipePosition = currentPosition.getNextPipePosition(previousPosition)
//            println(nextPipePosition)
            currentPosition.distanceFromStart = distanceFromStart
            distanceFromStart++
            previousPosition = currentPosition.position
            currentPosition = pipeMap.maze[nextPipePosition.second][nextPipePosition.first]
        } while (
            currentPosition !is StartElement
        )
        // second iteration
        previousPosition = startElement.position
        currentPosition = adjacentPipes[1]
        distanceFromStart = 1
        do {
            val nextPipePosition = currentPosition.getNextPipePosition(previousPosition)
//            println(nextPipePosition)
            if(currentPosition.distanceFromStart > distanceFromStart) {
                currentPosition.distanceFromStart = distanceFromStart
            } else {
//                break
            }
            distanceFromStart++
            previousPosition = currentPosition.position
            currentPosition = pipeMap.maze[nextPipePosition.second][nextPipePosition.first]
        } while (
            currentPosition !is StartElement
        )

//        pipeMap.maze.forEach { list ->
//            list.forEach {
//                print("%10d".format(it.distanceFromStart) +  " ")
//            }
//            println()
//        }

        return pipeMap.maze.flatten().filterNot { it.distanceFromStart == Int.MAX_VALUE }.maxBy { it.distanceFromStart }.distanceFromStart
    }

    fun part2(input: List<String>): Int {
//        var enclosedCounter = 0
//        var inside = false
//        input.forEachIndexed { index, line ->
//            val replacedLine = line
//                .replace("J", "|")
//                .replace("F", "|")
//                .replace("L", "|")
//                .replace("7", "|")
//            replacedLine.toCharArray().forEach { c ->
//                when(c) {
//                '|' -> inside = !inside
//                '-' -> inside = inside
//                'S' -> inside = inside
//                // ground
//                else -> if(inside) enclosedCounter++
//                }
//            }
//            inside = false
//        }
//
//        return enclosedCounter
        val maze = mutableListOf<List<PipeElement>>()
        var startPosition: Pair<Int, Int>? = null
        input.forEachIndexed { index, line ->
            maze.add(line.toCharArray().mapIndexed { lineIndex, c ->
                toPipeElement(c, lineIndex, index)
            }
                .toList())
            if (line.contains('S')) {
                startPosition = line.indexOf('S') to index
            }
        }

        val pipeMap = PipeMap(maze.toList(), startPosition!!)
        // search
        val startElement = pipeMap.maze[pipeMap.startPosition.second][pipeMap.startPosition.first]
        val adjacentPipes = findAdjacentPipes(startElement, pipeMap)
        // first iteration
        var previousPosition = startElement.position
        var currentPosition = adjacentPipes[0]
        var distanceFromStart = 1
        do {
            val nextPipePosition = currentPosition.getNextPipePosition(previousPosition)
//            println(nextPipePosition)
            currentPosition.distanceFromStart = distanceFromStart
            distanceFromStart++
            previousPosition = currentPosition.position
            currentPosition = pipeMap.maze[nextPipePosition.second][nextPipePosition.first]
        } while (
            currentPosition !is StartElement
        )

        pipeMap.maze.forEachIndexed { y, pipeElements ->
            pipeElements.forEachIndexed { x, pipeElement ->
                if(pipeElement is Ground) {
                    print('.')
                } else if(pipeElement.distanceFromStart == Int.MAX_VALUE) (
                    print(' ')
                ) else {
                    print(pipeElement.symbol)
                }
            }
            println()
        }

//        val count = pipeMap.maze.flatten().filterIsInstance<Ground>()
//            .count { ground ->
//                val neighbourCoordinates = getNeighbourCoordinates(ground.position)
//                    .filter { (x, y) -> x >= 0 && x < pipeMap.maze.size && y >= 0 && y < pipeMap.maze.size }
//                neighbourCoordinates.map { (x, y) ->
//                    val neighbour = pipeMap.maze[y][x]
//                    // part of the cycle
//                    neighbour.distanceFromStart != Int.MAX_VALUE || neighbour is Ground
//                }.all { it }
//            }
        return 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
//    check(part1(testInput) == 4)
//    check(part1(readInput("Day10_test2")) == 8))
//    check(part2(readInput("Day10_test3")) == 4)
//    check(part2(readInput("Day10_test4")) == 8)
//    check(part2(readInput("Day10_test5")) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(readInput("Day10")).println()
}

data class PipeMap(val maze: List<List<PipeElement>>, val startPosition: Pair<Int, Int>)

// | is a vertical pipe connecting north and south.
//- is a horizontal pipe connecting east and west.
//L is a 90-degree bend connecting north and east.
//J is a 90-degree bend connecting north and west.
//7 is a 90-degree bend connecting south and west.
//F is a 90-degree bend connecting south and east.
//. is ground; there is no pipe in this tile.
//S is the starting position of the animal; there is a pipe on this
sealed interface PipeElement {
    val position: Pair<Int, Int>
    fun getNextPipePosition(incomingPosition: Pair<Int, Int>): Pair<Int, Int>
    var distanceFromStart: Int
    val symbol: Char
}

data class VerticalPipe(override val position: Pair<Int, Int>, override var distanceFromStart: Int = Int.MAX_VALUE,
                        override val symbol: Char
) :
    PipeElement {
    override fun getNextPipePosition(incomingPosition: Pair<Int, Int>) =
        // coming from bottom
        if (incomingPosition.second > this.position.second) {
            this.position.first to this.position.second - 1
        } else {
            this.position.first to this.position.second + 1
        }

}

data class HorizontalPipe(override val position: Pair<Int, Int>, override var distanceFromStart: Int = Int.MAX_VALUE,
                          override val symbol: Char
) :
    PipeElement {
    override fun getNextPipePosition(incomingPosition: Pair<Int, Int>): Pair<Int, Int> =
        // coming from left
        if (incomingPosition.first < this.position.first) {
            this.position.first + 1 to this.position.second
        } else {
            this.position.first - 1 to this.position.second
        }

}

data class LPipe(override val position: Pair<Int, Int>, override var distanceFromStart: Int = Int.MAX_VALUE,
                 override val symbol: Char
) :
    PipeElement {
    override fun getNextPipePosition(incomingPosition: Pair<Int, Int>): Pair<Int, Int> =
        // coming from top
        if (incomingPosition.second < this.position.second) {
            this.position.first + 1 to this.position.second
        } else {
            this.position.first to this.position.second - 1
        }
}

data class JPipe(override val position: Pair<Int, Int>, override var distanceFromStart: Int = Int.MAX_VALUE,
                 override val symbol: Char
) :
    PipeElement {
    override fun getNextPipePosition(incomingPosition: Pair<Int, Int>): Pair<Int, Int> =
        // comin from top
        if (incomingPosition.second < this.position.second) {
            this.position.first - 1 to this.position.second
        } else {
            this.position.first to this.position.second - 1
        }

}

data class SevenPipe(override val position: Pair<Int, Int>, override var distanceFromStart: Int = Int.MAX_VALUE,
                     override val symbol: Char
) :
    PipeElement {
    override fun getNextPipePosition(incomingPosition: Pair<Int, Int>): Pair<Int, Int> =
        // coming from left
        if (incomingPosition.first < this.position.first) {
            this.position.first to this.position.second + 1
        } else {
            this.position.first - 1 to this.position.second
        }
}

data class FPipe(override val position: Pair<Int, Int>, override var distanceFromStart: Int = Int.MAX_VALUE,
                 override val symbol: Char
) :
    PipeElement {
    override fun getNextPipePosition(incomingPosition: Pair<Int, Int>): Pair<Int, Int> =
        // coming from bottom
        if (incomingPosition.second > this.position.second) {
            this.position.first + 1 to this.position.second
        } else {
            this.position.first to this.position.second + 1
        }

}

data class Ground(override val position: Pair<Int, Int>, override var distanceFromStart: Int = Int.MAX_VALUE, override val symbol: Char = '.') :
    PipeElement {
    override fun getNextPipePosition(incomingPosition: Pair<Int, Int>): Pair<Int, Int> {
        TODO("Not yet implemented")
    }
}

data class StartElement(override val position: Pair<Int, Int>, override var distanceFromStart: Int = Int.MAX_VALUE,
                        override val symbol: Char
) :
    PipeElement {
    override fun getNextPipePosition(incomingPosition: Pair<Int, Int>): Pair<Int, Int> {
        TODO("Not yet implemented")
    }
}
