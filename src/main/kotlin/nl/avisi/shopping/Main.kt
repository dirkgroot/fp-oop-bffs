package nl.avisi.shopping

import nl.avisi.shopping.cart.ProductName
import nl.avisi.shopping.cart.Quantity
import nl.avisi.shopping.cart.ShoppingCartItem
import nl.avisi.shopping.io.ask

/**
 * This program constructs a shopping cart item, using a product name
 * and quantity that are provided by the user.
 *
 * The product name must be a non-blank string with no line feeds.
 * The quantity must be a whole number that is higher than zero.
 *
 * The program does not halt when an error occurs. Instead, all errors
 * that have occurred during the program are shown at the end.
 *
 * Two types of error can occur:
 *
 * - IO error:
 *   When the user enters an illegal character. Only letters, digits
 *   and spaces are supported by our IO "library".
 *   See [ask].
 * - Validation error:
 *   When the user enters an invalid product name or quantity.
 */
fun main() {
    val errors = mutableListOf<String>()

    // Ask the user for the product name
    val productNameInput: String? = askWithErrorHandling(errors, "Enter product name : ")

    // Ask the user for the quantity
    val quantityInput: String? = askWithErrorHandling(errors, "Enter quantity     : ")

    // Validate the product name using the value object
    val productName: ProductName? =
        try {
            if (productNameInput != null) ProductName(productNameInput)
            else null
        } catch (e: IllegalArgumentException) {
            errors.add(e.message!!)
            null
        }

    // Validate the quantity using the value object
    val quantity: Quantity? =
        try {
            if (quantityInput != null) Quantity.of(quantityInput)
            else null
        } catch (e: IllegalArgumentException) {
            errors.add(e.message!!)
            null
        }

    // Check if IO and validation succeeded and print the result
    if (productName != null && quantity != null) {
        val item = ShoppingCartItem(productName, quantity)

        println("SUCCESS!\n")
        println("Product name       : ${item.productName}")
        println("Quantity           : ${item.quantity}")
        println("Shopping cart item : $item")
    } else {
        println("FAILURE!\n")
        println(errors.joinToString("\n"))
    }
}

private fun askWithErrorHandling(errors: MutableList<String>, question: String): String? =
    try {
        ask(question)
    } catch (e: IllegalStateException) {
        errors.add(e.message!!)
        null
    }
