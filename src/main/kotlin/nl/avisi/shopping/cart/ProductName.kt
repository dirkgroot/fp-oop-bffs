package nl.avisi.shopping.cart

import nl.avisi.shopping.Err
import nl.avisi.shopping.Ok
import nl.avisi.shopping.Status

data class ProductName private constructor(val value: String) {
    companion object {
        fun of(value: String): Status<ProductName> =
            when {
                value.isBlank() -> Err("Product name must not be blank")
                value.contains('\n') -> Err("Product name must not contain newlines")
                else -> Ok(ProductName(value))
            }
    }

    override fun toString() = value
}
