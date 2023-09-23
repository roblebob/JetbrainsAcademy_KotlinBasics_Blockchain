package blockchain

const val T_MIN = 100
const val T_MAX = 1000
const val N_MINERS = 9
const val N_BLOCKS = 15
const val KEY_LENGTH = 1024


fun main() {


    val miners = mutableListOf<Miner>()
    for (i in 1..N_MINERS) {
        miners.add(Miner.newInstance())
    }
    miners.forEach {
        it.start()
    }
    miners.forEach { it.join() }
    println(Blockchain.validateAll())
}










