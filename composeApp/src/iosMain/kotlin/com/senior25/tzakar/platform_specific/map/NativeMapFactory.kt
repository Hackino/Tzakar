package com.senior25.tzakar.platform_specific.map

import platform.UIKit.UIViewController

interface NativeViewFactory{
    fun createGoogleMap():UIViewController
}