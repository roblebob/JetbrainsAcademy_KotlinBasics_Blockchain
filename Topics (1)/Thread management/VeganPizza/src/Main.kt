// Fix this function
fun cookVeganPizza() {
    val base = Base()
    val tomatoes = Tomatoes()
    val tofu = Tofu()
    val bake = Bake()
    val stepOfCook: MutableList<Thread> = mutableListOf()
    stepOfCook.add(base)
    stepOfCook.add(tomatoes)
    stepOfCook.add(tofu)
    stepOfCook.add(bake)
    for (step in stepOfCook) {
        step.start()
        step.join()
    }
}

// Do not change the code below

class Base : Thread() {
    override fun run() {
        println("cook base")
    }
}

class Tomatoes : Thread() {
    override fun run() {
        for (i in 3 downTo 1) {
            println("slice tomatoes $i")
        }
    }
}

class Tofu : Thread() {
    override fun run() {
        println("fry tofu")
    }
}

class Bake : Thread() {
    override fun run() {
        for (i in 4 downTo 0) {
            if (i == 0) {
                println("Your pizza is ready!")
                break
            }
            println("to bake...$i min")
        }
    }
}