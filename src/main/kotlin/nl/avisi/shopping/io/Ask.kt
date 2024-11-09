package nl.avisi.shopping.io

/**
 * The only function in our IO "library".
 * Pretend this is a library function that we cannot change.
 */
fun ask(question: String): String {
    print(question)
    val input = readln()
    if (!input.matches("[0-9a-zA-Z\\s]*".toRegex()))
        throw IllegalStateException("Invalid character!")
    return input
}
