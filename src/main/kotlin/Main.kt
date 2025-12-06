import kotlin.random.Random
/* fun main() {
    println("ИГРА КАМЕНЬ-НОЖНИЦЫ-БУМАГА")
    println("Ходы:")
    println("1 - Камень")
    println("2 - Ножницы")
    println("3 - Бумага")

    var continuePlaying = true

    while (continuePlaying) {
        playRound()

        println("\nХотите сыграть еще раз? (Да/Нет)")
        val input = readln()
        if (input == "Нет") {
            println("Спасибо за игру")
            continuePlaying = false
        } else if (input == "Да") {
            continue
        } else {
            println("Ошибка, введите правильный вариант ответа")
        }
    }
}

fun playRound() {
    var playerChoice: Int
    var computerChoice: Int
    var isDraw: Boolean

    do {
        playerChoice = getPlayerChoice()

        computerChoice = (1 until 4).random()

        println("\nВаш выбор: ${getChoiceName(playerChoice)}")
        println("Выбор ПК: ${getChoiceName(computerChoice)}")

        isDraw = playerChoice == computerChoice

        if (isDraw) {
            println("Ничья! Играем еще")
        }

    } while (isDraw)

    val winner = determineWinner(playerChoice, computerChoice)

    when (winner) {
        "player" -> println("Вы победили! ${getWinReason(playerChoice, computerChoice)}")
        "computer" -> println("Победил компьютер! ${getWinReason(computerChoice, playerChoice)}")
    }
}

fun getPlayerChoice(): Int {
    while (true) {
        println("\nСделайте ваш выбор (1-3):")
        print("1 - Камень, 2 - Ножницы, 3 - Бумага: ")
        val choice = readln().toIntOrNull() ?: 0
        if (choice in 1..3) {
            return choice
        } else {
            println("Неверно! Введите 1, 2 или 3")
        }
    }
}

fun getChoiceName(choice: Int): String {
    return when (choice) {
        1 -> "Камень"
        2 -> "Ножницы"
        3 -> "Бумага"
        else -> "Неизвестно"
    }
}

fun determineWinner(playerChoice: Int, computerChoice: Int): String {
    return when {
        playerChoice == 1 && computerChoice == 2 -> "player"
        playerChoice == 2 && computerChoice == 3 -> "player"
        playerChoice == 3 && computerChoice == 1 -> "player"
        else -> "computer"
    }
}

fun getWinReason(winnerChoice: Int, loserChoice: Int): String {
    return when {
        winnerChoice == 1 && loserChoice == 2 -> "Камень ломает ножницы"
        winnerChoice == 2 && loserChoice == 3 -> "Ножницы режут бумагу"
        winnerChoice == 3 && loserChoice == 1 -> "Бумага оборачивает камень"
        else -> ""
    }
}
import kotlin.random.Random

fun main() {

    print("Введите сообщение для шифрования: ")
    val input = readln().trim()

    print("Введите вспомогательный символ (для нечётной длины, например, 'Я'): ")
    var filler = readln().trim().uppercase().firstOrNull() ?: 'Я'

    val alphabet = "АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
    if (filler !in alphabet) {
        println("Символ '$filler' не в алфавите. Меняю на 'Я'")
        filler = 'Я'
    }

    var tip: Boolean? = null
    while (tip == null) {
        print("Использовать типовую таблицу? (да/нет): ")
        val rawAnswer = readln().trim().lowercase()

        when (rawAnswer) {
            "да" -> tip = true
            "нет" -> tip = false
            else -> {
                println("Ошибка")
            }
        }
    }

    val cleaned = cleanMessage(input, alphabet)
    var message = cleaned
    if (message.length % 2 != 0) {
        message += filler
        println("Длина сообщения нечётная → добавлен символ '$filler'")
    }

    val pairs = mutableListOf<Pair<Char, Char>>()
    for (i in 0 until message.length step 2) {
        pairs.add(Pair(message[i], message[i + 1]))
    }

    val cipherTable: Map<Pair<Char, Char>, String>
    if (tip) {
        cipherTable = StandardTable(alphabet)
        println("Используется типовая таблица")
    } else {
        cipherTable = RandomTable(alphabet)
        println("Сгенерирована случайная таблица")
    }

    val encrypt = mutableListOf<String>()
    for (pair in pairs) {
        val code = cipherTable[pair]
        if (code == null) {
            println("Ошибка: нет кода для пары $pair")
            return
        }
        encrypt.add(code)
    }

    println("\n")
    println("Исходное сообщение:")
    val pairsStr = pairs.joinToString(" ") { "${it.first}${it.second}" }
    println(pairsStr)

    println("\nЗашифрованное сообщение:")
    val numbersStr = encrypt.joinToString(" ")
    println(numbersStr)



    println("\nРасшифровка:")
    val decryptedC = mutableListOf<Char>()
    for (code in encrypt) {
        val pair = cipherTable.entries.firstOrNull { it.value == code }?.key
        if (pair != null) {
            decryptedC.add(pair.first)
            decryptedC.add(pair.second)
        } else {
            decryptedC.add('?')
            decryptedC.add('?')
        }
    }
    var decrypted = decryptedC.joinToString("")
    if (decrypted.lastOrNull() == filler && cleaned.length % 2 != 0) {
        decrypted = decrypted.dropLast(1)
    }
    println("→ $decrypted")
}

fun cleanMessage(text: String, alphabet: String): String {
    return text.uppercase()
        .replace('Ё', 'Е')
        .replace('Й', 'И')
        .filter { it in alphabet }
}

fun StandardTable(alphabet: String): Map<Pair<Char, Char>, String> {
    val table = mutableMapOf<Pair<Char, Char>, String>()
    var counter = 1
    for (row in alphabet) {
        for (col in alphabet) {
            val code = "%03d".format(counter)
            table[Pair(row, col)] = code
            counter++
        }
    }
    return table
}

fun RandomTable(alphabet: String): Map<Pair<Char, Char>, String> {
    val total = alphabet.length * alphabet.length // 961
    val numbers = (1..total).shuffled(Random(42)) 
    val table = mutableMapOf<Pair<Char, Char>, String>()
    var index = 0
    for (row in alphabet) {
        for (col in alphabet) {
            val code = "%03d".format(numbers[index])
            table[Pair(row, col)] = code
            index++
        }
    }
    return table
}*/


const val alphabet= "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЭЮЯ"
fun main(){
    print("Введите исходное сообщение ")
    val mess = readln().uppercase()

    print("Введите ключ ")
    val key = readln().uppercase()
    if(key.isEmpty()){
        println("Ключ не может быть пустым")
        return
    }

    print("Использовать типовую таблицу? (y/n) ")
    val standart = readln().lowercase()

    val useStandart = when(standart) {
        "y", "yes", "д", "да" -> true
        "n", "no", "н", "нет" -> false
        else -> {
            println("Неизвестный ответ. Введите (да/нет)")
            true
        }
    }

}