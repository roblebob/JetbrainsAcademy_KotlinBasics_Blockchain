// finish it!
fun <T: Comparable<T>> getBigger(a: T, b: T): T {
    return if (a > b) a else b
}