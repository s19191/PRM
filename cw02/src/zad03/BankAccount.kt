package zad03;

class BankAccount(startBalance: Double) {
    private var _balance: Double = startBalance
    val balance: Double
        get() = _balance

    constructor() : this(0.0)
    fun withdraw(amount: Double) {
        if (_balance >= amount) {
            _balance -= amount
        } else {
            throw UnsupportedOperationException("Not enough founds")
        }
    }

    fun payInto(amount: Double) {
        _balance += amount
    }
}