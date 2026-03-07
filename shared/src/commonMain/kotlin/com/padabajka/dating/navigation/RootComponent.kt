package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.deeplink.AppDeeplink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RootComponent(
    component: ComponentContext,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : ComponentContext by component {
    private val _deeplinkFlow: MutableSharedFlow<AppDeeplink> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 1
    )
    val deeplinkFlow: Flow<AppDeeplink> = _deeplinkFlow.asSharedFlow()

    fun onDeeplink(uri: AppDeeplink) {
        scope.launch {
            _deeplinkFlow.emit(uri)
        }
    }
}
