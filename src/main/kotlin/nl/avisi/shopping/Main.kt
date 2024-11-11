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
    val productNameInput: Status<String> =
        askWithErrorHandling("Enter product name : ")

    // Ask the user for the quantity
    val quantityInput: Status<String> =
        askWithErrorHandling("Enter quantity     : ")

    // Validate the product name using the value object
    val productName: ProductName? =
        try {
            when (productNameInput) {
                is Ok -> ProductName(productNameInput.value)
                is Err -> {
                    errors.add(productNameInput.message)
                    null
                }
            }
        } catch (e: IllegalArgumentException) {
            errors.add(e.message!!)
            null
        }

    // Validate the quantity using the value object
    val quantity: Quantity? =
        try {
            when (quantityInput) {
                is Ok -> Quantity.of(quantityInput.value)
                is Err -> {
                    errors.add(quantityInput.message)
                    null
                }
            }
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

private fun askWithErrorHandling(question: String): Status<String> =
    try {
        Ok(ask(question))
    } catch (e: IllegalStateException) {
        Err(e.message!!)
    }

sealed interface Status<T>
data class Ok<T>(val value: T) : Status<T>
data class Err<T>(val message: String) : Status<T>
