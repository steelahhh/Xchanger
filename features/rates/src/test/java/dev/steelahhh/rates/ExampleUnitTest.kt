package dev.steelahhh.rates

import com.airbnb.mvrx.test.MvRxTestRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @get:Rule
    val rule = MvRxTestRule()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
