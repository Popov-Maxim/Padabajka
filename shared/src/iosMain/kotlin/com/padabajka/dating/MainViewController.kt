package com.padabajka.dating

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.uikit.ComposeUIViewControllerDelegate
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.pause
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.start
import com.arkivanov.essenty.lifecycle.stop
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationState
import platform.UIKit.UIApplicationWillResignActiveNotification
import platform.UIKit.UIViewController

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("FunctionNaming")
fun MainViewController(): UIViewController {
    val lifecycle = LifecycleRegistry()

    return ComposeUIViewController(
        configure = {
            delegate = object : ComposeUIViewControllerDelegate {
                private val activityObservers = mutableListOf<Any>()

                override fun viewDidLoad() {
                    lifecycle.create()
                    super.viewDidLoad()
                }

                override fun viewWillAppear(animated: Boolean) {
                    lifecycle.start()
                    super.viewWillAppear(animated)
                }

                override fun viewDidAppear(animated: Boolean) {
                    val notificationCenter = NSNotificationCenter.defaultCenter
                    activityObservers += notificationCenter.addObserverForName(
                        name = UIApplicationDidBecomeActiveNotification,
                        `object` = null,
                        queue = NSOperationQueue.mainQueue
                    ) { lifecycle.resume() }
                    activityObservers += notificationCenter.addObserverForName(
                        name = UIApplicationWillResignActiveNotification,
                        `object` = null,
                        queue = NSOperationQueue.mainQueue
                    ) { lifecycle.pause() }

                    if (UIApplication.sharedApplication.applicationState ==
                        UIApplicationState.UIApplicationStateActive
                    ) {
                        lifecycle.resume()
                    }

                    super.viewDidAppear(animated)
                }

                override fun viewWillDisappear(animated: Boolean) {
                    activityObservers.forEach {
                        NSNotificationCenter.defaultCenter.removeObserver(it)
                    }
                    activityObservers.clear()
                    lifecycle.pause()
                    super.viewWillDisappear(animated)
                }

                override fun viewDidDisappear(animated: Boolean) {
                    lifecycle.stop()
                    super.viewDidDisappear(animated)
                }
            }
        }
    ) {
        val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
        val focusManager = LocalFocusManager.current

        val backDispatcher = remember { BackDispatcher() }
        BackHandler(true) {
            if (imeBottom > 0) {
                focusManager.clearFocus()
            } else {
                backDispatcher.back()
            }
        }
        val defaultComponentContext = DefaultComponentContext(
            lifecycle = lifecycle,
            backHandler = backDispatcher
        )
        App(defaultComponentContext)
    }
}
