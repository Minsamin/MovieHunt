package com.rpimx.moviehunt

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class TestCoroutineRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),

    ) : TestWatcher() {
    private val testCoroutineScope = TestScope(testDispatcher)


    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }

    fun testRunBlocking(block: suspend TestScope.() -> Unit) {
        runTest(timeout = 10.seconds, context = testDispatcher) { block() }
    }

    fun runTest(block: suspend TestScope.() -> Unit) = testCoroutineScope.runTest(timeout = 10.seconds) { block() }

    fun TestCoroutineRule.newCoroutineScope(): TestScope = TestScope(context = testDispatcher as CoroutineContext)
}