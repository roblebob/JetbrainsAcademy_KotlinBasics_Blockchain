fun printIfDaemon(thread: Thread) {
    // implement logic
    println("${if (thread.isDaemon) " " else "not "}daemon")
}