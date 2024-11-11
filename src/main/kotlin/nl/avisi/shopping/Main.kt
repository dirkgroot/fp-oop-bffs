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
    val item = Ok(curried(::ShoppingCartItem))
        .apply(askWithErrorHandling("Enter product name : ").flatMap { ProductName.of(it) })
        .apply(askWithErrorHandling("Enter quantity     : ").flatMap { Quantity.of(it) })

    // Check if IO and validation succeeded and print the result
    if (item is Ok) {
        println("SUCCESS!\n")
        println("Product name       : ${item.value.productName}")
        println("Quantity           : ${item.value.quantity}")
        println("Shopping cart item : ${item.value}")
    } else if (item is Err) {
        println("FAILURE!\n")
        println(item.message)
    }
}

private fun askWithErrorHandling(question: String): Status<String> =
    try {
        Ok(ask(question))
    } catch (e: IllegalStateException) {
        Err(e.message!!)
    }
