package nl.avisi.shopping.cart

data class Quantity(val value: Int) {
    init {
        if (value < 1)
            throw IllegalArgumentException("Quantity must be 1 or more")
    }

    companion object {
        fun of(string: String): Quantity {
            val value = string.toIntOrNull()
                ?: throw IllegalArgumentException("Quantity must be numeric")
            return Quantity(value)
        }
    }

    override fun toString() = value.toString()
}
