package com.senior25.tzakar.ktx

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreen
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope


@Composable
inline fun <reified T : ScreenModel> Screen.koinScreenModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): T {
    val st = parameters?.let { rememberUpdatedState(parameters) }
    val tag = remember(qualifier, scope) { qualifier?.value }
    return rememberScreenModel(tag = tag) {
        scope.get(qualifier, st?.value)
    }
}


@Composable
public inline fun <reified T : ScreenModel> Navigator.koinParentScreenModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null,
    parentName:String? = MainScreen::class.simpleName
):T? {
    var currentParent = parent
    while (currentParent?.lastItem?.let { it::class.simpleName != parentName } == true) {
        currentParent = currentParent.parent
        if (currentParent?.level == 0) return null
    }
   return koinNavigatorScreenModel<T>(
        qualifier = qualifier,
        scope = scope,
        parameters = parameters
    )
}

@Composable
public inline fun <reified T : ScreenModel> Navigator.koinNavigatorScreenModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): T {
    val st = parameters?.let { rememberUpdatedState(parameters) }
    val tag = remember(qualifier, scope) { qualifier?.value }
    return rememberNavigatorScreenModel(tag = tag) {
        scope.get(qualifier, st?.value)
    }
}