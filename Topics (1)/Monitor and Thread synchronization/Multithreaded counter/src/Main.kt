class Counter {
    var count = 0

    @Synchronized
    fun inc() {
        count++
    }
}