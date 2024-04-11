package com.fp.padabajka.testing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun testScope(): CoroutineScope = CoroutineScope(Dispatchers.Default)
