import kotlin.random.Random

fun main() {
rollDice()
}

fun randomNumberGenerator(): Int {
    val theChosenOne = Random.nextInt(6)
    return when {
        theChosenOne >= 7 -> 6
        theChosenOne <= 0 -> 1
        else -> theChosenOne
    }
}

fun rollDice() {
    val output =  mutableListOf<Int>()
    for (num in 1..5) { output.add(randomNumberGenerator()) }

    val allRolls = listOf(
        Pair("Ones", sameNumberScore(1, output)),
        Pair("Twos", sameNumberScore(2, output)),
        Pair("Threes", sameNumberScore(3, output)),
        Pair("Fours", sameNumberScore(4, output)),
        Pair("Fives", sameNumberScore(5, output)),
        Pair("Sixes", sameNumberScore(6, output)),
        Pair("Three of a Kind", ofAKindScore(3, output, true)),
        Pair("Four of a Kind", ofAKindScore(4, output, true)),
        Pair("Full House", fullHouseScore(output)),
        Pair("Chance", chanceScore(output)),
        Pair("Small Straight", straightScore(true, output)),
        Pair("Large Straight", straightScore(false, output)),
        Pair("Yahtzee", yahtzeeScore(output)),
    )

    println("Dice Roll: $output")
    println("=== All Choices: ===")
    for (rollSet in allRolls) {
        println("${rollSet.first}: ${rollSet.second}")
    }
    println("=== Choices with Points: ===")
    for (rollSet in allRolls) {
        if (rollSet.second != 0) {
            println("${rollSet.first}: ${rollSet.second}")
        } else {
            continue
        }
    }
    println("Best Option: ${bestOption(allRolls)}")
}

fun bestOption(input: List<Pair<String, Int>>): Pair<String, Int> {
    val dummyList = input.sortedBy { it.second }
    return dummyList.last()
}

fun straightScore(small: Boolean, input: MutableList<Int>): Int {
    val sortedList = input.sorted()
    var previousNumber = 0
    var successCounter = 0

    for (numba in sortedList) {
    if (previousNumber - numba == -1 || (numba == 2 && previousNumber == 0)) {
        previousNumber = numba
        successCounter++
    } else {
        previousNumber = numba
        continue
    }
    }

    return when {
        (!small && successCounter >= 5) -> 40
        (small && successCounter >= 4) -> 30
        else -> 0
    }

}

fun yahtzeeScore(input: MutableList<Int>): Int {
    return if (
        ofAKindScore(5, input) > 0
    ) { 50 } else { 0 }
}

fun fullHouseScore(input: MutableList<Int>): Int {
    return if (
        (ofAKindScore(2, input) > 0) && (ofAKindScore(3, input) > 0)
    ) { 25 } else { 0 }
}

fun ofAKindScore(
    allowedKinds: Int,
    input: MutableList<Int>,
    finalCalculation: Boolean = false
): Int {
    var total = 0
    fun updateTotal() {total += chanceScore(input)}
    for (numba in input) {
        if (finalCalculation) {
            if (input.count { it == numba } >= allowedKinds) {
                updateTotal()
                break
            }
        } else {
            if (input.count { it == numba } == allowedKinds) {
                updateTotal()
                break
            }
        }
    }
    return total
}

fun chanceScore(input: MutableList<Int>): Int {
    var total = 0
    for (numba in input) { total += numba }
    return total
}

fun sameNumberScore(
    numberCategory: Int,
    input: MutableList<Int>
): Int {
    var total = 0
    for (number in input) {
        if (number == numberCategory) {
            total += number
        } else {
            continue
        }
    }
    return total
}