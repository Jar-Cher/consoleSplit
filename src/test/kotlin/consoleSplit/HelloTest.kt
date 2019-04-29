package consoleSplit

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class HelloTest {
    @Test
    fun testCorrectiveness() {
        assertFails { isCorrectInput(arrayOf("ololo") ) }
        assertFails { isCorrectInput(arrayOf("split", "split") ) }
        assertFails { isCorrectInput(arrayOf("split", "-d", "-d") ) }
        assertFails { isCorrectInput(arrayOf("split", "-d", "-l" ,"-n") ) }
        assertFails { isCorrectInput(arrayOf("split", "-d", "-l", "-o", "-", "ololo") ) }
    }
}
