@SuppressWarnings("MagicNumber")
class BankAccount {
    var balance = 0L

    fun addMoney(action: String) {
        // synchronize adding money with synchronized()
        when (action) {
            "1" -> {
                addGold()
            }
            "2" -> {
                addSilver()
            }
            else ->  {
                addCopper()
            }
        }
    }
    fun addGold() {
        synchronized(this) {
            balance += 10000
        }
    }
    fun addSilver() {
        synchronized(this) {
            balance += 100
        }
    }
    fun addCopper() {
        synchronized(this) {
            balance += 1
        }
    }
}