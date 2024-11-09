package nl.avisi.shopping.cart

data class ProductName(val value: String) {
    init {
        if (value.isBlank())
            throw IllegalArgumentException("Product name must not be blank")
        if (value.contains('\n'))
            throw IllegalArgumentException("Product name must not contain newlines")
    }

    override fun toString() = value
}
