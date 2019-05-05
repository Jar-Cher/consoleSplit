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

    /*@Test
    fun testCorrectiveness() {
        assertFails { isCorrectInput(arrayOf("ololo") ) }
        assertFails { isCorrectInput(arrayOf("split", "split") ) }
        assertFails { isCorrectInput(arrayOf("-d", "-d") ) }
        assertFails { isCorrectInput(arrayOf("-d", "-l" ,"-n") ) }
        assertFails { isCorrectInput(arrayOf("-d", "-l", "-o", "-", "ololo") ) }
        assertFails { isCorrectInput(arrayOf("-d", "-l", "ololo") ) }
        assertFails { nextFile("ololo", false, 100500) }
    }*/

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
    }
}
