package com.snc.farmaccount

import android.content.Context
import com.google.common.base.CharMatcher.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
private var FAKE_LONG = ArrayList<Long>()

@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @Mock
    lateinit var mainViewModel: MainViewModel

//    @Test
//    fun addition_isCorrect() {
//
//        FAKE_LONG = arrayListOf(1,3,-5,4)
//
//        `when`(mainViewModel.priceList)
//            .thenReturn(FAKE_LONG)
//
//        var result = Mockito.spy(mainViewModel.priceList)
//
//        assertEquals(result.sum(), mainViewModel.getPriceSumTest())
//    }

    @Test
    fun addition_isCorrect() {
        FAKE_LONG = arrayListOf(1,3,-5,4)

        assertEquals(3, getPriceSumTest(FAKE_LONG))
    }

    @Test
    fun addition_isWrong() {
        FAKE_LONG = arrayListOf(1,3,-5,4)

        assertNotEquals(4, getPriceSumTest(FAKE_LONG))
    }

    @Test
    fun addition_isEmptyCorrect() {
        FAKE_LONG = arrayListOf()

        assertEquals(0, getPriceSumTest(FAKE_LONG))
    }

    @Test
    fun addition_isEmptyWrong() {
        FAKE_LONG = arrayListOf()

        assertNotEquals(4, getPriceSumTest(FAKE_LONG))
    }
}
