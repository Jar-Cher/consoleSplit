package consoleSplit

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFails


class HelloTest {

    private fun assertFileContent(name: String, expectedContent: String) {
        val file = File(name)
        val content = file.readLines().joinToString("\n")
        assertEquals(expectedContent, content)
    }

    @Test
    fun testCorrectiveness() {
        assertFails { main(arrayOf("ololo") ) }
        assertFails { main(arrayOf("split", "split") ) }
        assertFails { main(arrayOf("-d", "-d") ) }
        assertFails { main(arrayOf("-d", "-l" ,"-n") ) }
        assertFails { main(arrayOf("-d", "-l", "-o", "-", "ololo") ) }
        assertFails { main(arrayOf("-d", "-l", "ololo") ) }
        assertFails { nextFile("ololo", false, 100500) }
    }

    @Test
    fun testNextFile() {
        assertEquals(nextFile("ololo", false, 3), "oloload")
        assertEquals(nextFile("ololo", false, 26), "ololoba")
        assertEquals(nextFile("olo", false, 25), "oloaz")
        assertEquals(nextFile("olo", true, 25), "olo26")
    }

    @Test
    fun testLWork() {
        lWork("input.txt", "ololo", 134, true)
        assertFileContent("ololo1", """something something
something
more something""")
        lWork("input.txt", "ololo", 1, true)
        assertFileContent("ololo1", """something something""")
        lWork("input.txt", "ololo", 2, true)
        assertFileContent("ololo1", """something something
something""")
        File("ololo1").delete()
        File("ololo2").delete()
        File("ololo3").delete()
    }

    @Test
    fun testCWork() {
        cWork("input.txt", "ololo", 134, true)
        assertFileContent("ololo1", """something something
something
more something""")
        cWork("input.txt", mChars = 5, enNumer = false, fBaseOutput = "x")
        assertFileContent("xaa", """somet""")
        cWork("inp.txt", "ololo", 2, true)
        assertFileContent("ololo1", """""")
        File("xaa").delete()
        File("ololo1").delete()
    }

    @Test
    fun testNWork() {
        nWork("input.txt", "x",3, enNumer = false)
        assertFileContent("xaa", """something somet""")
        nWork("inp.txt", "ololo", 8, true)
        assertFileContent("ololo1", """""")
        File("ololo1").delete()
    }
}
