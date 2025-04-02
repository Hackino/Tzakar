package com.senior25.tzakar.platform_specific.map

import platform.UIKit.UIViewController

interface NativeViewFactory{
    fun createGoogleMap(interaction:MapInteraction):UIViewController
    fun updateGoogleMapMarker(view:UIViewController,latitude: Double, longitude: Double)
}