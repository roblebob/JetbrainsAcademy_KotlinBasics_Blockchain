package blockchain

import java.security.MessageDigest

val T_MIN = 100
val T_MAX = 1000
val N_MINERS = 5


val MESSAGES = listOf(
    "Hello! How are you?",
    "It's not fair!\nYou always will be first because it is your blockchain!",
    "I'm the best programmer in the world!",
    "Anyway, thank you for this amazing chat."
)


fun main() {


    val miners = mutableListOf<Miner>()
    for (i in 1..N_MINERS) {
        miners.add(Miner.getInstances(i))
    }
    miners.forEach {
        it.start()
    }
    miners.forEach { it.join() }
    println(Blockchain.validateAll())
}










