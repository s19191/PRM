package zad03

fun main() {
    val acc = BankAccount(100.0)

    println(acc.balance)
//    acc.balance = -1.0
    acc.withdraw(50.0)
    acc.payInto(50.0)
    acc.withdraw(100.0)
//    acc.withdraw(100.0)
    println(acc.balance)
}