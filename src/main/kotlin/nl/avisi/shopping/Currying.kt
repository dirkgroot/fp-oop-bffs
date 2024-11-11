package nl.avisi.shopping

fun <A1, A2, R> curried(function: (A1, A2) -> R): (A1) -> (A2) -> R =
    { a1: A1 ->
        { a2: A2 ->
            function(a1, a2)
        }
    }
