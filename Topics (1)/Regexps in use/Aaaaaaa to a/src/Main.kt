fun main() {
    val text = readln()
    // write your code here
    text.replace(Regex("[Aa]+"), "a").also(::println)
}