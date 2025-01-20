package com.senior25.tzakar.platform_specific

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion


    fun adsfsdf(){
        getPlatform().name
    }
}

actual fun getPlatform(): Platform = IOSPlatform()