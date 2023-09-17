class MessageNotifier(val message: String, val repetitions: Int) : Thread() { // implement the constructor
    override fun run() {
        // implement the method to print the message stored in a field
        repeat(repetitions) {
            println(message)
        }
    }
}