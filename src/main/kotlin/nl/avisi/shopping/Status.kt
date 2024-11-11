package nl.avisi.shopping

sealed interface Status<T>
data class Ok<T>(val value: T) : Status<T>
data class Err<T>(val message: String) : Status<T>

fun <T, R> Status<T>.flatMap(mapper: (T) -> Status<R>): Status<R> =
    when (val m = map(mapper)) {
        is Ok -> m.value
        is Err -> Err(m.message)
    }

fun <T, R> Status<T>.map(mapper: (T) -> R): Status<R> =
    when (this) {
        is Ok -> Ok(mapper(value))
        is Err -> Err(message)
    }

fun <T, R> Status<(T) -> R>.apply(argument: Status<T>): Status<R> =
    when (this) {
        is Ok -> when (argument) {
            is Ok -> Ok(value(argument.value))
            is Err -> Err(argument.message)
        }

        is Err -> when (argument) {
            is Ok -> Err(message)
            is Err -> Err(message + "\n" + argument.message)
        }
    }
