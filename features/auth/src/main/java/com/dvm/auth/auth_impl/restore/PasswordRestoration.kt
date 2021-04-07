package com.dvm.auth.auth_impl.restore

import androidx.compose.runtime.Composable
import com.dvm.appmenu.Drawer
import com.dvm.auth.auth_impl.restore.model.RestoreEvent
import com.dvm.auth.auth_impl.restore.model.RestoreState
import com.dvm.navigation.Navigator

@Composable
fun PasswordRestoration(
    state: RestoreState,
    navigator: Navigator,
    onEvent: (RestoreEvent) -> Unit
) {
    Drawer(navigator = navigator) {

    }
}