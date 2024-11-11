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
    val productName: Status<ProductName> =
        // Ask the user for the product name
        askWithErrorHandling("Enter product name : ")
            // Validate the product name using the value object
            .flatMap { ProductName.of(it) }

    val quantity: Status<Quantity> =
        // Ask the user for the quantity
        askWithErrorHandling("Enter quantity     : ")
            // Validate the quantity using the value object
            .flatMap { Quantity.of(it) }

    val item = Ok(curried(::ShoppingCartItem))
        .apply(productName)
        .apply(quantity)

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

private fun <A1, A2, R> curried(function: (A1, A2) -> R): (A1) -> (A2) -> R =
    { a1: A1 ->
        { a2: A2 ->
            function(a1, a2)
        }
    }

private fun <T, R> Status<(T) -> R>.apply(argument: Status<T>): Status<R> =
    when (this) {
        is Ok -> when (argument) {
            is Ok -> Ok(this.value(argument.value))
            is Err -> Err(argument.message)
        }

        is Err -> when (argument) {
            is Ok -> Err(message)
            is Err -> Err(message + "\n" + argument.message)
        }
    }

private fun <T, R> Status<T>.map(mapper: (T) -> R): Status<R> =
    when (this) {
        is Ok -> Ok(mapper(value))
        is Err -> Err(message)
    }

private fun <T, R> Status<T>.flatMap(mapper: (T) -> Status<R>): Status<R> =
    when (val m = map(mapper)) {
        is Ok -> m.value
        is Err -> Err(m.message)
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
