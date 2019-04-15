package consoleSplit

import java.io.File

fun main(args: Array<String>) {
    val arg = args.joinToString(separator = " ")
    if(!arg.matches(Regex("""^split (-d)? (-l|-c|-n) [1-9]+\d* (-o .+)? .+$""")))
        throw IllegalArgumentException("Incorrect input format")
    for (line in File(args.last()).readLines()) {

    }
}

