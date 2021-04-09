package com.dvm.menu.menu.presentation.model

internal sealed class MenuEvent{
    data class MenuItemClick(val id: String): MenuEvent()
    object SearchClick: MenuEvent()
    object AppMenuClick: MenuEvent()
}