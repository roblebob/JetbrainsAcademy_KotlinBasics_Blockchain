fun findSerialNumberForGame(listGames: List<String>) {

    val name = readln()

    for (game in listGames) {
        val args = game.split("@".toRegex())
        if (name == args[0]) {
            println("Code for Xbox - ${args[1]}, for PC - ${args[2]}")
        }
    }
}