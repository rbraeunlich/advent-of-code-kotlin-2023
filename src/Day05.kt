fun main() {
    fun part1(input: List<String>): Long {
        // destination range start, the source range start, and the range length
        val initialSeeds = mutableListOf<Long>()
        val seedToSoilMap = GardeningMap()
        val soilToFertilizerMap = GardeningMap()
        val fertilizerToWaterMap = GardeningMap()
        val waterToLightMap = GardeningMap()
        val lightToTemperatureMap = GardeningMap()
        val temperatureToHumidityMap = GardeningMap()
        val humidityToLocationMap = GardeningMap()

        var currentMap: GardeningMap? = null
        input.forEach { line ->
            if (line.startsWith("seeds")) {
                line.replace("seeds: ", "").split(" ")
                    .forEach { seed -> initialSeeds.add(seed.toLong()) }
            } else if (line.isBlank()) {
                // nothing
            } else if (line.startsWith("seed-to-soil map")) {
                currentMap = seedToSoilMap

            } else if (line.startsWith("soil-to-fertilizer map")) {
                currentMap = soilToFertilizerMap

            } else if (line.startsWith("fertilizer-to-water map")) {
                currentMap = fertilizerToWaterMap

            } else if (line.startsWith("water-to-light map")) {
                currentMap = waterToLightMap

            } else if (line.startsWith("light-to-temperature map")) {
                currentMap = lightToTemperatureMap

            } else if (line.startsWith("temperature-to-humidity map")) {
                currentMap = temperatureToHumidityMap

            } else if (line.startsWith("humidity-to-location map")) {
                currentMap = humidityToLocationMap
            } else {
                // read the range line
                val (destination, source, length) = line.split(" ")
                val newRange = GardeningRange(destination.toLong(), source.toLong(), length.toLong())
                currentMap!!.ranges.add(newRange)
            }
        }
//        println(seedToSoilMap)
//        println(soilToFertilizerMap)
//        println(fertilizerToWaterMap)
//        println(waterToLightMap)
//        println(lightToTemperatureMap)
//        println(temperatureToHumidityMap)
//        println(humidityToLocationMap)

        return initialSeeds.map { seed ->
            val soilDestination = seedToSoilMap.getDestination(seed)
            val fertilizerDestination = soilToFertilizerMap.getDestination(soilDestination)
            val waterDestination = fertilizerToWaterMap.getDestination(fertilizerDestination)
            val lightDestination = waterToLightMap.getDestination(waterDestination)
            val temperatureDestination = lightToTemperatureMap.getDestination(lightDestination)
            val humidityDestination = temperatureToHumidityMap.getDestination(temperatureDestination)
            val destination = humidityToLocationMap.getDestination(humidityDestination)
            destination
        }.min()
    }

    fun part2(input: List<String>): Long {
        // destination range start, the source range start, and the range length
        val initialSeeds = mutableListOf<Pair<Long, Long>>()
        val seedToSoilMap = GardeningMap()
        val soilToFertilizerMap = GardeningMap()
        val fertilizerToWaterMap = GardeningMap()
        val waterToLightMap = GardeningMap()
        val lightToTemperatureMap = GardeningMap()
        val temperatureToHumidityMap = GardeningMap()
        val humidityToLocationMap = GardeningMap()

        var currentMap: GardeningMap? = null
        input.forEach { line ->
            if (line.startsWith("seeds")) {
                line.replace("seeds: ", "")
                    .split(" ")
                    .chunked(2)
                    .forEach { (seedStart, range) ->
                            initialSeeds.add(seedStart.toLong() to range.toLong())
                    }
            } else if (line.isBlank()) {
                // nothing
            } else if (line.startsWith("seed-to-soil map")) {
                currentMap = seedToSoilMap

            } else if (line.startsWith("soil-to-fertilizer map")) {
                currentMap = soilToFertilizerMap

            } else if (line.startsWith("fertilizer-to-water map")) {
                currentMap = fertilizerToWaterMap

            } else if (line.startsWith("water-to-light map")) {
                currentMap = waterToLightMap

            } else if (line.startsWith("light-to-temperature map")) {
                currentMap = lightToTemperatureMap

            } else if (line.startsWith("temperature-to-humidity map")) {
                currentMap = temperatureToHumidityMap

            } else if (line.startsWith("humidity-to-location map")) {
                currentMap = humidityToLocationMap
            } else {
                // read the range line
                val (destination, source, length) = line.split(" ")
                val newRange = GardeningRange(destination.toLong(), source.toLong(), length.toLong())
                currentMap!!.ranges.add(newRange)
            }
        }
//        println(seedToSoilMap)
//        println(soilToFertilizerMap)
//        println(fertilizerToWaterMap)
//        println(waterToLightMap)
//        println(lightToTemperatureMap)
//        println(temperatureToHumidityMap)
//        println(humidityToLocationMap)

        return initialSeeds.minOf { (seedStart, seedRange) ->
            (0 ..< seedRange).minOf { seedOffset ->
                val seed = seedStart + seedOffset
                val soilDestination = seedToSoilMap.getDestination(seed)
                val fertilizerDestination = soilToFertilizerMap.getDestination(soilDestination)
                val waterDestination = fertilizerToWaterMap.getDestination(fertilizerDestination)
                val lightDestination = waterToLightMap.getDestination(waterDestination)
                val temperatureDestination = lightToTemperatureMap.getDestination(lightDestination)
                val humidityDestination = temperatureToHumidityMap.getDestination(temperatureDestination)
                val destination = humidityToLocationMap.getDestination(humidityDestination)
                destination
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

data class GardeningMap(val ranges: MutableList<GardeningRange> = mutableListOf()) {

    fun getDestination(seed: Long): Long {
        val matchingRange = ranges.firstOrNull { range ->
            // is within range
            range.sourceRangeStart <= seed && (range.sourceRangeStart + range.rangeLength - 1) >= seed
        }
        return if (matchingRange == null) {
            seed
        } else {
            val offset = seed - matchingRange.sourceRangeStart
            matchingRange.destinationRangeStart + offset
        }
    }
}

data class GardeningRange(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long)
