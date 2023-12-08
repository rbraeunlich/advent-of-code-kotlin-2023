fun main() {
    fun part1(input: List<String>): Int {
        val directionsString = input.first()
        val desertNodes = input.drop(2).map { nodeString ->
            // JKT = (KFV, CFQ)
            val (nodeId, left, right) = nodeString.replace("(", "").replace(")", "").split("=", ",")
            Node(nodeId.trim(), left.trim(), right.trim())
        }
        var goalReached = false
        var nextDirection = 0
        var stepsTaken = 1
        val desertNodesLookup = desertNodes.associate { it.id to it}
        var currentNode = desertNodesLookup["AAA"]!!
        do {
            val direction = directionsString[nextDirection]
            nextDirection++
            if(nextDirection >= directionsString.length) {
                nextDirection = 0
            }
            val targetNode = when(direction) {
                'L' -> currentNode.left
                else -> currentNode.right
            }
//            println(currentNode)
//            println(targetNode)
            if(targetNode == "ZZZ") {
                goalReached = true
            } else {
                currentNode = desertNodesLookup[targetNode]!!
                stepsTaken++
            }
        } while (!goalReached)
        return stepsTaken
    }

    fun part2(input: List<String>): Long {
        val directionsString = input.first()
        val desertNodes = input.drop(2).map { nodeString ->
            // JKT = (KFV, CFQ)
            val (nodeId, left, right) = nodeString.replace("(", "").replace(")", "").split("=", ",")
            Node(nodeId.trim(), left.trim(), right.trim())
        }
        val desertNodesLookup = desertNodes.associate { it.id to it}
        var currentNodes = desertNodesLookup.filter { (k, _) -> k.endsWith("A") }.toList().map { (_, v) -> v }
        val nodesWithLenghts = currentNodes.associateWith {
            var currentNode = it
            var stepsTaken = 1
            var goalReached = false
            var nextDirection = 0
            do {
                val direction = directionsString[nextDirection]
                nextDirection++
                if (nextDirection >= directionsString.length) {
                    nextDirection = 0
                }
                val targetNode = when (direction) {
                    'L' -> currentNode.left
                    else -> currentNode.right
                }
//            println(currentNode)
//            println(targetNode)
                if (targetNode.endsWith("Z")) {
                    goalReached = true
                } else {
                    currentNode = desertNodesLookup[targetNode]!!
                    stepsTaken++
                }
            } while (!goalReached)
            stepsTaken
        }
        return findLCMOfListOfNumbers(nodesWithLenghts.values.toList())
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val testInput2 = readInput("Day08_test2")
    check(part1(testInput) == 2)
    check(part1(testInput2) == 6)
    check(part2(readInput("Day08_test3")) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

data class Node(val id: String, val left: String, val right: String)

fun findLCMOfListOfNumbers(numbers: List<Int>): Long {
    var result = numbers[0].toLong()
    for (i in 1 until numbers.size) {
        result = org.apache.commons.math3.util.ArithmeticUtils.lcm(result, numbers[i].toLong())
    }
    return result
}