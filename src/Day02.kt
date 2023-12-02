fun main() {
    // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    //only 12 red cubes, 13 green cubes, and 14 blue cubes
    fun part1(input: List<String>): Int {
        val finalGames = input.map { line ->
            val split = line.split(":")
            val gamePart = split[0]
            val id = gamePart.replace("Game ", "").toInt()

            val game = Game(id, mutableListOf())
            val games = split[1]
                .split(";")
                .forEach { gameLine ->
                    val colours = gameLine.split(",")
                    val cubeResult = CubeResult(0, 0, 0)
                    colours.forEach {
                        if (it.contains("blue")) {
                            val blue = it.replace("blue", "").trim().toInt()
                            cubeResult.blue = blue
                        } else if (it.contains("red")) {
                            val red = it.replace("red", "").trim().toInt()
                            cubeResult.red = red
                        } else {
                            val green = it.replace("green", "").trim().toInt()
                            cubeResult.green = green
                        }
                    }
                    game.cubes.add(cubeResult)
                }
            game
        }

        return finalGames.filter { it.isPossible() }.map { it.id }.sum()
    }

    fun part2(input: List<String>): Int {
        val finalGames = input.map { line ->
            val split = line.split(":")
            val gamePart = split[0]
            val id = gamePart.replace("Game ", "").toInt()

            val game = Game(id, mutableListOf())
            val games = split[1]
                .split(";")
                .forEach { gameLine ->
                    val colours = gameLine.split(",")
                    val cubeResult = CubeResult(0, 0, 0)
                    colours.forEach {
                        if (it.contains("blue")) {
                            val blue = it.replace("blue", "").trim().toInt()
                            cubeResult.blue = blue
                        } else if (it.contains("red")) {
                            val red = it.replace("red", "").trim().toInt()
                            cubeResult.red = red
                        } else {
                            val green = it.replace("green", "").trim().toInt()
                            cubeResult.green = green
                        }
                    }
                    game.cubes.add(cubeResult)
                }
            game
        }

        return finalGames.map { it.getMinCubes() }.map { it.first * it.second * it.third }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)


    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class Game(val id: Int, val cubes: MutableList<CubeResult>) {
    fun isPossible() = cubes.all { it.isPossible() }
    fun getMinCubes(): Triple<Int, Int, Int> {
        val red = cubes.map { it.red }.max()
        val green = cubes.map { it.green }.max()
        val blue = cubes.map { it.blue }.max()
        return Triple(red, green, blue)
    }
}

data class CubeResult(var red: Int, var green: Int, var blue: Int) {

    fun isPossible() = red <= 12 && green <= 13 && blue <= 14
}