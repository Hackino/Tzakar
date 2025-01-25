package com.senior25.tzakar.platform_specific.toast_helper

import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication

actual fun showToast(message: String) {
    val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    val alertController = UIAlertController.alertControllerWithTitle(
        null,
        message,
        UIAlertControllerStyleAlert
    )
    val dismissAction = UIAlertAction.actionWithTitle(
        "OK", UIAlertActionStyleDefault, null
    )
    alertController.addAction(dismissAction)
    viewController?.presentViewController(alertController, true, null)
}