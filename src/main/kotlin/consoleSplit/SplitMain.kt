package consoleSplit

import java.io.File
import java.lang.StringBuilder

enum class TypeOfWork {
    L, C, N
}

/*fun isCorrectInput1(arg: String) {
    if(!((arg.matches(Regex("""^split( -d)?( (-l|-c|-n) [1-9]+\d*)?( -o .+)? .+$""")))||
                (arg.matches(Regex("""^split( -d)?( -o .+)?( (-l|-c|-n) [1-9]+\d*)? .+$""")))||
                (arg.matches(Regex("""^split( (-l|-c|-n) [1-9]+\d*)?( -o .+)?( -d)? .+$""")))||
                (arg.matches(Regex("""^split( (-l|-c|-n) [1-9]+\d*)?( -d)?( -o .+)? .+$""")))||
                (arg.matches(Regex("""^split( -o .+)?( -d)?( (-l|-c|-n) [1-9]+\d*)? .+$""")))||
                (arg.matches(Regex("""^split( -o .+)?( (-l|-c|-n) [1-9]+\d*)?( -d)? .+$""")))))
        throw IllegalArgumentException("Incorrect input format")
}*/
data class Parser(val args: Array<String>) {

    var sNumer = false
    var workToDo = TypeOfWork.L
    var unitsOfMeasurement = 100
    var outputFile = "x"
    var inpFile = "Nope"

    init {

        if(args.isEmpty())
            throw IllegalArgumentException("Please enter command")
        inpFile = args.last()
        var amountOfValidElements = 1

        val dCount = args.count { it == "-d" }
        if (dCount > 1)
            throw IllegalArgumentException("Odd amount of -d flag")
        amountOfValidElements += dCount
        if (dCount == 1)
            sNumer = true

        val lCount = args.count { it == "-l" }
        val cCount = args.count { it == "-c" }
        val nCount = args.count { it == "-n" }
        val wCount = lCount + cCount + nCount
        if (wCount > 1)
            throw IllegalArgumentException("Odd amount of -l/-c/-n flag")
        when {
            cCount == 1 -> workToDo = TypeOfWork.C
            nCount == 1 -> workToDo = TypeOfWork.N
        }
        amountOfValidElements += wCount

        when {
            lCount == 1 -> {
                if (!args.getOrElse(args.indexOf("-l") + 1) { "olo" }.matches(Regex("""[1-9]+\d*""")))
                    throw IllegalArgumentException("Incorrect number of lines given")
                unitsOfMeasurement = args[args.indexOf("-l") + 1].toInt()
            }
            cCount == 1 -> {
                if (!args.getOrElse(args.indexOf("-c") + 1) { "olo" }.matches(Regex("""[1-9]+\d*""")))
                    throw IllegalArgumentException("Incorrect number of symbols given")
                unitsOfMeasurement = args[args.indexOf("-c") + 1].toInt()
            }
            nCount == 1 -> {
                if (!args.getOrElse(args.indexOf("-n") + 1) { "olo" }.matches(Regex("""[1-9]+\d*""")))
                    throw IllegalArgumentException("Incorrect number of files given")
                unitsOfMeasurement = args[args.indexOf("-n") + 1].toInt()
            }
        }
        amountOfValidElements++

        val oCount = args.count { it == "-o" }
        if (oCount > 1)
            throw IllegalArgumentException("Odd amount of -o flag")
        amountOfValidElements += oCount
        if (oCount == 1) {
            if (args.indexOf("-o") + 1 == args.size)
                throw IllegalArgumentException("Output file not given")
            outputFile = args[args.indexOf("-o") + 1]
            if (outputFile == "-")
                outputFile = inpFile
            amountOfValidElements++
        }

        if (amountOfValidElements != args.size)
            throw IllegalArgumentException("Incorrect command")
    }
}

/*fun shouldEnum(arg: String): Boolean = arg.contains(Regex("""-d"""))

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
}*/

fun nextFile(baseName: String, shouldEnum: Boolean, i: Int): String {
    return if(shouldEnum) {
        baseName + (i + 1).toString()
    }
    else {
        if(i > 676)
            throw java.lang.IllegalArgumentException("Incorrect output configuration")
        val fLetter = 'a' + i / 26
        val sLetter = 'a' + i % 26
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
        //if(!c.toChar().toString().matches(Regex("""\r""")))
            j++
    }
    writer.close()
}

fun nWork(fInput: String, fBaseOutput: String, mFiles: Int, enNumer: Boolean) {
    val inpStr = StringBuilder()
    val r = File(fInput).reader()
    var c = r.read()
    while (c != -1) {
        inpStr.append(c.toChar())
        c = r.read()
    }
    r.close()
    //println(inpStr.toString())

    val outputSize = inpStr.toString().length / mFiles

    //println(inpStr.length)
    //println (outputSize)

    /*var i = 0
    var j = 0
    var fOutput = nextFile(fBaseOutput, enNumer, i)
    var writer = File(fOutput).bufferedWriter()
    for (charS in inpStr.toString()) {
        writer.write(charS.toInt())
        j++
        if(j > outputSize) {
            writer.close()
            i++
            fOutput = nextFile(fBaseOutput, enNumer, i)
            writer = File(fOutput).bufferedWriter()
            j = 1
        }
    }
    */

    if(mFiles < inpStr.toString().length)
        cWork(fInput, fBaseOutput, outputSize, enNumer)
    else {
        cWork(fInput, fBaseOutput, 1, enNumer)
        //val i = mFiles - inpStr.toString().length
        var fOutput: String

        for(k in inpStr.toString().length until mFiles) {
            fOutput = nextFile(fBaseOutput, enNumer, k)
            val writer = File(fOutput).bufferedWriter()
            writer.close()
        }
    }
}


fun main(args: Array<String>) {

    val parser = Parser(args)

    val fInput = parser.inpFile

    val enNumer = parser.sNumer

    // Default type of work
    val toW = parser.workToDo

    // Default units of measurement
    val uoM = parser.unitsOfMeasurement

    val fBaseOutput = parser.outputFile

    /*
    println(enNumer)
    println(toW)
    println(uoM)
    println(fBaseOutput)
    println(fInput)
    */

    if (!File(fInput).exists())
        throw IllegalArgumentException("Input file does not exist")

    when (toW) {
        TypeOfWork.L -> lWork(fInput, fBaseOutput, uoM, enNumer)
        TypeOfWork.C -> cWork(fInput, fBaseOutput, uoM, enNumer)
        TypeOfWork.N -> nWork(fInput, fBaseOutput, uoM, enNumer)
    }
}

