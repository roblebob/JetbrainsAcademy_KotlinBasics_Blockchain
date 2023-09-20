package blockchain

class Myssage private constructor(val id: Int, val name: String, val text: String, val signature: ByteArray ) {

    override fun toString(): String {
        return "$id# $name: $text"
    }

    companion object {
        @Volatile
        private var total = 0
        fun newInstance(name: String, text: String, signature: ByteArray): Myssage {
            return Myssage(++total, name, text, signature)
        }
    }

}


class Signatures {
}

















