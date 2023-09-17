class CountDownCounter(var count: Int) {
    @Synchronized
    fun decrement() {
        count--
    }
}