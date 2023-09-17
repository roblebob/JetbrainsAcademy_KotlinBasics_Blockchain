class RandomClass<T, S, U>(var value1: T, var value2: S, var value3: U) {
    fun getFirstValue(): T {
        return value1
    }
    fun getSecondValue(): S {
        return value2
    }
    fun getThirdValue(): U {
        return value3
    }
}