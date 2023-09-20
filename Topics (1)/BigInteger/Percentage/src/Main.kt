import java.math.BigInteger

fun main() {
    // write your code here
    val x = readln().toBigInteger()
    val y = readln().toBigInteger()
    println("${x * BigInteger.valueOf(100) / (x + y)}% ${y * BigInteger.valueOf(100) / (x + y)}%")
}