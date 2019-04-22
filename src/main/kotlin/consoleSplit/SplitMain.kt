package consoleSplit

import java.io.File

enum class TypeOfWork {
    L, C, N
}

fun isCorrectInput(arg: String) {
    if(!((arg.matches(Regex("""^split( -d)?( (-l|-c|-n) [1-9]+\d*)?( -o .+)? .+$""")))||
                (arg.matches(Regex("""^split( -d)?( -o .+)?( (-l|-c|-n) [1-9]+\d*)? .+$""")))||
                (arg.matches(Regex("""^split( (-l|-c|-n) [1-9]+\d*)?( -o .+)?( -d)? .+$""")))||
                (arg.matches(Regex("""^split( (-l|-c|-n) [1-9]+\d*)?( -d)?( -o .+)? .+$""")))||
                (arg.matches(Regex("""^split( -o .+)?( -d)?( (-l|-c|-n) [1-9]+\d*)? .+$""")))||
                (arg.matches(Regex("""^split( -o .+)?( (-l|-c|-n) [1-9]+\d*)?( -d)? .+$""")))))
        throw IllegalArgumentException("Incorrect input format")
}

fun shouldEnum(arg: String): Boolean = arg.contains(Regex("""-d"""))

fun inpFile(args: Array<String>): String = args.last()

fun outFile(args: Array<String>, fInput: String): String =
    if(args.contains("-o"))
        if(args[args.indexOf("-o") + 1] == "-")
            fInput
        else
            args[args.indexOf("-o") + 1]
    else
        "x"

fun typeOfWork(args: Array<String>): TypeOfWork =
    when {
        args.contains("-l") -> TypeOfWork.L
        args.contains("-c") -> TypeOfWork.C
        args.contains("-n") -> TypeOfWork.N
        else -> TypeOfWork.L
    }

fun unitsOfMeasurement(args: Array<String>): Int = when {
    args.contains("-l") -> args[args.indexOf("-l") + 1].toInt()
    args.contains("-c") -> args[args.indexOf("-c") + 1].toInt()
    args.contains("-n") -> args[args.indexOf("-n") + 1].toInt()
    else -> 100
}

fun nextFile(baseName: String, shouldEnum: Boolean, i: Int): String {
    return if(shouldEnum) {
        baseName + (i + 1).toString()
    }
    else {
        if (i > 676)
            throw java.lang.IllegalArgumentException("Incorrect output configuration")
        val fLetter = (97 + i / 26).toChar()
        val sLetter = (97 + i % 26).toChar()
        baseName + fLetter + sLetter
    }
}

fun lWork(fInput: String, fBaseOutput: String, mLines: Int, enNumer: Boolean) {
    var i = 0
    var j = 0
    var fOutput = nextFile(fBaseOutput, enNumer, i)
    var writer = File(fOutput).bufferedWriter()
    for (line in File(fInput).readLines()) {
        if(j==mLines) {
            writer.close()
            i++
            fOutput = nextFile(fBaseOutput, enNumer, i)
            writer = File(fOutput).bufferedWriter()
            j = 0
        }
        writer.write(line)
        writer.newLine()
        j++
    }
    writer.close()
}

fun cWork(fInput: String, fBaseOutput: String, mChars: Int, enNumer: Boolean) {
    var i = 0
    var j = 0
    var fOutput = nextFile(fBaseOutput, enNumer, i)
    var writer = File(fOutput).bufferedWriter()
    val r = File(fInput).reader()
    var c = r.read()
    while (c != -1) {
        if(j==mChars) {
            writer.close()
            i++
            fOutput = nextFile(fBaseOutput, enNumer, i)
            writer = File(fOutput).bufferedWriter()
            j = 0
        }
        writer.write(c)
        c = r.read()
        if(!c.toChar().toString().matches(Regex("""\r""")))
            j++
    }
    writer.close()
}


fun main(args: Array<String>) {

    val arg = args.joinToString(separator = " ")

    isCorrectInput(arg)

    val fInput = inpFile(args)

    val enNumer = shouldEnum(arg)

    // Default type of work
    val toW = typeOfWork(args)

    // Default units of measurement
    val uoM = unitsOfMeasurement(args)

    val fBaseOutput = outFile(args, fInput)

    /*
    println(enNumer)
    println(toW)
    println(uoM)
    println(fBaseOutput)
    println(fInput)
    */

    if (!File(fInput).exists())
        throw IllegalArgumentException("Input file does not exist")

    when {
        toW == TypeOfWork.L -> lWork(fInput, fBaseOutput, uoM, enNumer)
        toW == TypeOfWork.C -> cWork(fInput, fBaseOutput, uoM, enNumer)
    }
}

