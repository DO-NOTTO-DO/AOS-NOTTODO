package kr.co.nottodo.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions

val NavController.startDestination: NavDestination
    get() {
        var startDestination: NavDestination = this.graph
        while (startDestination is NavGraph) {
            val graphStartDestination = startDestination
            startDestination =
                graphStartDestination.findNode(graphStartDestination.startDestinationId)
                    ?: throw IllegalStateException("Failed Found Start Destination")
        }
        return startDestination
    }

fun NavController.navigateWithClearingBackstack(@IdRes resId: Int, args: Bundle? = null) {
    val clearBackstackNavOptions =
        NavOptions.Builder().setPopUpTo(this.startDestination.id, true).build()
    this.navigate(
        resId, args, clearBackstackNavOptions
    )
}