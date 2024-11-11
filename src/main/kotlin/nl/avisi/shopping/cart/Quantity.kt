package nl.avisi.shopping.cart

import nl.avisi.shopping.Err
import nl.avisi.shopping.Ok
import nl.avisi.shopping.Status

data class Quantity(val value: Int) {
    companion object {
        fun of(string: String): Status<Quantity> {
            val value = string.toIntOrNull()
            return when {
                value == null -> Err("Quantity must be numeric")
                value < 1 -> Err("Quantity must be 1 or more")
                else -> Ok(Quantity(value))
            }
        }
    }

    override fun toString() = value.toString()
}
