import java.math.BigInteger

fun main() {
    // write your code here
    val x = readln().toBigInteger()
    val y = readln().toBigInteger()
    println(myMax(x, y))
}


fun myMax(x: BigInteger, y: BigInteger): BigInteger {

    return (x + y + (x - y).abs()) / BigInteger.valueOf(2)
}