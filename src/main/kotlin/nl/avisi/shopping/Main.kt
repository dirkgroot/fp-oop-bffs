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
    // Ask the user for the product name
    val productNameInput: Status<String> =
        askWithErrorHandling("Enter product name : ")

    // Ask the user for the quantity
    val quantityInput: Status<String> =
        askWithErrorHandling("Enter quantity     : ")

    // Validate the product name using the value object
    val productName: Status<ProductName> =
        try {
            parse(productNameInput) { ProductName(it) }
        } catch (e: IllegalArgumentException) {
            Err(e.message!!)
        }

    // Validate the quantity using the value object
    val quantity: Status<Quantity> =
        try {
            parse(quantityInput) { Quantity.of(it) }
        } catch (e: IllegalArgumentException) {
            Err(e.message!!)
        }

    // Check if IO and validation succeeded and print the result
    if (productName is Ok && quantity is Ok) {
        val item = ShoppingCartItem(productName.value, quantity.value)

        println("SUCCESS!\n")
        println("Product name       : ${item.productName}")
        println("Quantity           : ${item.quantity}")
        println("Shopping cart item : $item")
    } else {
        println("FAILURE!\n")
        if (productName is Err) println(productName.message)
        if (quantity is Err) println(quantity.message)
    }
}

private fun <R> parse(status: Status<String>, parser: (String) -> R): Status<R> =
    when (status) {
        is Ok -> Ok(parser(status.value))
        is Err -> Err(status.message)
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
