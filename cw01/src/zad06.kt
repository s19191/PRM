fun main() {
    val priceList = mapOf<String, Double>(
        "Banan" to 1.20,
        "Masło" to 4.99,
        "Chleb" to 4.50,
        "Herbata" to 6.00
    )

    val salesPriceList = priceList.mapValues { it.value * 0.20 }

    priceList.forEach { (product, price) ->
        println("%s - %.2f".format(product, price))
    }
    println()

    salesPriceList.forEach { (product, price) ->
        println("%s - %.2f".format(product, price))
    }
    println()

    fun printPrice(product: String) {
        val price: Double? = priceList[product]
        println("Cena produktu $product: ${price?.let { "%.2f zł".format(it) } ?: "Brak na stanie"}")
    }
    printPrice("Drożdże")
    printPrice("Masło")
}