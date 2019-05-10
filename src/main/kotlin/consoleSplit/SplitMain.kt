package consoleSplit

import java.io.File
import kotlin.math.ceil

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

    var shouldEnumerate = false
    var workToDo = TypeOfWork.L
    var unitsOfMeasurement = 100
    var outputFile = "x"
    var inputFile = "Nope"

    init {

        if (args.isEmpty())
            throw IllegalArgumentException("Please enter command")
        inputFile = args.last()
        var amountOfValidElements = 1

        val dCount = args.count { it == "-d" }
        if (dCount > 1)
            throw IllegalArgumentException("Odd amount of -d flag")
        amountOfValidElements += dCount
        if (dCount == 1)
            shouldEnumerate = true

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
                outputFile = inputFile
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

fun nextFile(baseName: String, shouldEnumerate: Boolean, i: Int): String {
    return if (shouldEnumerate) {
        baseName + (i + 1).toString()
    } else {
        if (i > 675)
            throw java.lang.IllegalArgumentException("Incorrect output configuration")
        val firstLetter = 'a' + i / 26
        val secondLetter = 'a' + i % 26
        baseName + firstLetter + secondLetter
    }
}

fun lWork(inputFile: String, baseOutputFile: String, maxLines: Int, shouldEnumerate: Boolean) {
    var i = 0
    var j = 0
    var outputFile = nextFile(baseOutputFile, shouldEnumerate, i)
    var writer = File(outputFile).bufferedWriter()
    val lines = File(inputFile).readLines()
    for (line in lines) {
        if (j == maxLines) {
            writer.close()
            i++
            outputFile = nextFile(baseOutputFile, shouldEnumerate, i)
            writer = File(outputFile).bufferedWriter()
            j = 0
        }
        writer.write(line)
        if (line != lines.last())
            writer.newLine()
        j++
    }
    writer.close()
}

fun cWork(inputFile: String, baseOutputFile: String, maxChars: Int, shouldEnumerate: Boolean) {
    var i = 0
    var j = 0
    var outputFile = nextFile(baseOutputFile, shouldEnumerate, i)
    var writer = File(outputFile).bufferedWriter()
    val r = File(inputFile).reader()
    var c = r.read()
    while (c != -1) {
        if (j == maxChars) {
            writer.close()
            i++
            outputFile = nextFile(baseOutputFile, shouldEnumerate, i)
            writer = File(outputFile).bufferedWriter()
            j = 0
        }
        writer.write(c)
        c = r.read()
        //if(!c.toChar().toString().matches(Regex("""\r""")))
        j++
    }
    writer.close()
}

fun nWork(inputFile: String, baseOutputFile: String, maxFiles: Int, shouldEnumerate: Boolean) {

    val outputSize = ceil(File(inputFile).length() / maxFiles.toDouble()).toInt()

    //println(inpStr.length)
    //println (outputSize)

    /*var i = 0
    var j = 0
    var fOutput = nextFile(baseOutputFile, shouldEnumerate, i)
    var writer = File(fOutput).bufferedWriter()
    for (charS in inpStr.toString()) {
        writer.write(charS.toInt())
        j++
        if(j > outputSize) {
            writer.close()
            i++
            fOutput = nextFile(baseOutputFile, shouldEnumerate, i)
            writer = File(fOutput).bufferedWriter()
            j = 1
        }
    }
    */

    if (maxFiles < File(inputFile).length())
        cWork(inputFile, baseOutputFile, outputSize, shouldEnumerate)
    else {
        cWork(inputFile, baseOutputFile, 1, shouldEnumerate)
        //val i = maxFiles - inpStr.toString().length
        var outputFile: String

        for (k in File(inputFile).length() until maxFiles) {
            outputFile = nextFile(baseOutputFile, shouldEnumerate, k.toInt())
            val writer = File(outputFile).bufferedWriter()
            writer.close()
        }
    }
}


fun main(args: Array<String>) {

    val parser = Parser(args)

    val inputFile = parser.inputFile

    val shouldEnumerate = parser.shouldEnumerate

    // Default type of work
    val workToDo = parser.workToDo

    // Default units of measurement
    val unitsOfMeasurement = parser.unitsOfMeasurement

    val baseOutputFile = parser.outputFile

    /*
    println(enNumer)
    println(toW)
    println(uoM)
    println(fBaseOutput)
    println(fInput)
    */

    if (!File(inputFile).exists())
        throw IllegalArgumentException("Input file does not exist")

    when (workToDo) {
        TypeOfWork.L -> lWork(inputFile, baseOutputFile, unitsOfMeasurement, shouldEnumerate)
        TypeOfWork.C -> cWork(inputFile, baseOutputFile, unitsOfMeasurement, shouldEnumerate)
        TypeOfWork.N -> nWork(inputFile, baseOutputFile, unitsOfMeasurement, shouldEnumerate)
    }
}

