object Math {
    fun abs(value: Int): Int {
        return if (value < 0) -value else value
    }
    fun abs(value: Double): Double {
        return if (value < 0) -value else value
    }
}