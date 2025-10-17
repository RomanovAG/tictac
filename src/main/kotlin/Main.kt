import kotlin.math.abs
import kotlin.math.sqrt

enum class GameState {
    GAME_NOT_FINISHED,
    DRAW,
    X_WINS,
    O_WINS,
    IMPOSSIBLE
}

fun checkState(arr: CharArray, gridDim: Int = 3): GameState {
    val xCount = arr.count { it == 'X' }
    val oCount = arr.count { it == 'O' }
    val emptyCount = arr.count { it == '_' }

    if (abs(xCount - oCount) > 1) {
        return GameState.IMPOSSIBLE
    }

    val xWins = isWinning(arr, 'X', gridDim)
    val oWins = isWinning(arr, 'O', gridDim)

    if (xWins && oWins) {
        return GameState.IMPOSSIBLE
    }

    if (xWins) return GameState.X_WINS
    if (oWins) return GameState.O_WINS

    return if (emptyCount == 0) GameState.DRAW else GameState.GAME_NOT_FINISHED
}

fun isWinning(arr: CharArray, ch: Char, gridSize: Int = 3): Boolean {
    for (i in 0 .. gridSize-1) {
        var wins = true
        for (j in 0 .. gridSize-1) {
            if (arr[i * gridSize + j] != ch) {
                wins = false
                break
            }
        }
        if (wins)
            return true
    }

    for (j in 0 .. gridSize-1) {
        var wins = true
        for (i in 0 .. gridSize-1) {
            if (arr[i * gridSize + j] != ch) {
                wins = false
                break
            }
        }
        if (wins)
            return true
    }

    var wins = true
    for (i in 0 .. gridSize-1) {
        if (arr[i * gridSize + i] != ch) {
            wins = false
            break
        }
    }
    if (wins)
        return true

    wins = true
    for (i in 0 .. gridSize-1) {
        if (arr[i * gridSize + (gridSize - 1 - i)] != ch) {
            wins = false
            break
        }
    }
    return wins
}

fun printState(state: GameState) {
    when (state) {
        GameState.GAME_NOT_FINISHED -> println("Game not finished")
        GameState.DRAW -> println("Draw")
        GameState.X_WINS -> println("X wins")
        GameState.O_WINS -> println("O wins")
        GameState.IMPOSSIBLE -> println("Impossible")
    }
}

fun printBoard(arr: CharArray, gridDim: Int = 3) {
    print("---------")
    for (i in arr.indices) {
        if (i % gridDim == 0) {
            print("\n| ")
        }
        print("${arr[i]} ")
        if ((i+1) % gridDim == 0) {
            print("|")
        }
    }
    println("\n---------")
}

fun getCoords(input: String): Pair<Int, Int>? {
    val inp = input.trim().split(" ")
    if (inp.size != 2)
        return null

    val row = inp[0].toInt()
    val col = inp[1].toInt()
    return Pair(row, col)
}

fun isValidCoords(coords: Pair<Int, Int>, gridDim: Int): Boolean {
    val (row, col) = coords
    return row in 1..gridDim && col in 1..gridDim
}

fun getID(coords: Pair<Int, Int>, gridDim: Int): Int {
    val (row, col) = coords
    return (row - 1) * gridDim + (col - 1)
}

fun makeMove(arr: CharArray, coords: Pair<Int, Int>, ch: Char, gridDim: Int = 3): Boolean {
    val index = getID(coords, gridDim)
    if (arr[index] == '_') {
        arr[index] = ch
        return true
    }
    return false
}

fun main() {
    println("Enter init state > ")
    val arr = readLine()!!.trim().toCharArray()
    val gridDim = sqrt(arr.size.toDouble()).toInt()
    printBoard(arr, gridDim)

    var ch = 'X'
    var state = checkState(arr, gridDim)
    while (state == GameState.GAME_NOT_FINISHED) {
        var validMove = false
        var coords: Pair<Int, Int>? = null

        while (!validMove) {
            print("Enter the coordinates: ")
            val userInput = readLine()!!

            coords = getCoords(userInput)
            if (coords == null) {
                println("You should enter numbers!")
                continue
            }

            if (!isValidCoords(coords, gridDim)) {
                println("Coordinates should be from 1 to $gridDim!")
                continue
            }

            val cellIndex = getID(coords, gridDim)
            if (arr[cellIndex] != '_') {
                println("This cell is occupied! Choose another one!")
                continue
            }

            validMove = true
        }

        makeMove(arr, coords!!, ch, gridDim)
        printBoard(arr, gridDim)
        state = checkState(arr, gridDim)
        ch = if (ch == 'X') 'O' else 'X'
    }
    printState(state)
}