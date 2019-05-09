package consoleSplit

import org.junit.Test
import java.io.File
import java.lang.StringBuilder
import kotlin.test.assertEquals
import kotlin.test.assertFails


class HelloTest {

    private fun assertFileContent(name: String, expectedContent: String) {

        val file = File(name).reader()
        val content = StringBuilder(128)
        var c = file.read()
        while (c != -1) {
            content.append(c.toChar())
            c = file.read()
        }
        assertEquals(expectedContent, content.toString())
    }

    private fun generateText(size: Int): String {
        val ans = StringBuilder(size)
        for (i in 1..size) {
            val nextChar = kotlin.random.Random.nextInt(0,100)
            if (nextChar > 94)
                ans.append('\n')
            else
                ans.append(' ' + nextChar)
        }
        return ans.toString()
    }

    @Test
    fun testCorrectiveness() {
        assertFails { main(arrayOf("ololo")) }
        assertFails { main(arrayOf("split", "split")) }
        assertFails { main(arrayOf("-d", "-d")) }
        assertFails { main(arrayOf("-d", "-l", "-n")) }
        assertFails { main(arrayOf("-d", "-l", "-o", "-", "ololo")) }
        assertFails { main(arrayOf("-d", "-l", "ololo")) }
        assertFails { nextFile("ololo", false, 100500) }
    }

    @Test
    fun testNextFile() {
        assertEquals(nextFile("ololo", false, 0), "ololoaa")
        assertEquals(nextFile("ololo", true, 0), "ololo1")
        assertEquals(nextFile("ololo", false, 3), "oloload")
        assertEquals(nextFile("ololo", false, 26), "ololoba")
        assertEquals(nextFile("olo", false, 25), "oloaz")
        assertEquals(nextFile("olo", true, 2), "olo3")
        assertEquals(nextFile("olo", true, 25), "olo26")
        assertEquals(nextFile("olo", false, 675), "olozz")
        assertEquals(nextFile("olo", true, 9), "olo10")
        assertEquals(nextFile("olo", false, 300), "ololo")
    }

    @Test
    fun testLWork() {
        lWork("input.txt", "ololo", 134, true)
        assertFileContent(
            "ololo1",
            """something something
something
more something"""
        )
        lWork("input.txt", "ololo", 1, true)
        assertFileContent("ololo1", """something something""")
        lWork("input.txt", "ololo", 2, true)
        assertFileContent(
            "ololo1", """something something
something"""
        )
        File("ololo1").delete()
        File("ololo2").delete()
        File("ololo3").delete()
    }

    @Test
    fun testCWork() {
        cWork("input.txt", "ololo", 134, true)
        assertFileContent(
            "ololo1", """something something
something
more something"""
        )
        cWork("input.txt", maxChars = 5, shouldEnumerate = false, baseOutputFile = "x")
        assertFileContent("xaa", """somet""")
        cWork("inp.txt", "ololo", 2, true)
        assertFileContent("ololo1", """""")
        File("xaa").delete()
        File("ololo1").delete()
    }

    @Test
    fun testNWork() {
        nWork("input.txt", "x", 3, false)
        assertFileContent("xaa", """something somet""")
        nWork("inp.txt", "ololo", 8, true)
        assertFileContent("ololo1", """""")
        File("ololo1").delete()
    }

    @Test
    fun uniTest() {
        for (i in 1..3) {

            val defaultInputFile = "autoTestInput"
            val textSize = 128
            val defaultOutputFile = "x"
            val text = generateText(textSize)

            val inp = File(defaultInputFile).bufferedWriter()
            inp.write(text)
            inp.close()

            for (j in 0..7) {

                val fraction = Math.pow(2.0,j.toDouble()).toInt()

                nWork(
                    defaultInputFile,
                    defaultOutputFile,
                    fraction,
                    true
                )

                for (k in 1..fraction) {

                    val textChunked = text.chunked(textSize / fraction)
                    assertFileContent(
                        nextFile("x",true, k - 1),
                        textChunked[k - 1])

                    File(nextFile("x",true, k - 1)).delete()
                }
            }

        }
    }
}
