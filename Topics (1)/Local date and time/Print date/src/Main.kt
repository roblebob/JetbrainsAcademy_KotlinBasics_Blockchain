import kotlinx.datetime.*

fun printDate(date: String) {
    // Write your code here
    val localDate = date.toLocalDate()
    println("${localDate.dayOfWeek}, ${localDate.month} ${localDate.dayOfMonth}, ${localDate.year}")
}

fun main() {
    val date = readln()
    printDate(date)
}