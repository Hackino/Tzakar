package com.senior25.tzakar.platform_specific.web_view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSAttributedString
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.NSUTF8StringEncoding
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.NSHTMLTextDocumentType
import platform.UIKit.UILabel
import platform.UIKit.UIView
import platform.WebKit.WKNavigationAction
import platform.WebKit.WKNavigationActionPolicy
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKNavigationTypeLinkActivated
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

//
//// iOS-specific actual function
//@OptIn(ExperimentalForeignApi::class)
//@Composable
//actual fun HtmlWebView(
//    htmlContent: String,
//    modifier: Modifier
//) {
//    val content = remember { test }
//    val webView = remember { WKWebView() }
//
//    UIKitView(
//        modifier = modifier.fillMaxWidth().height().padding(top = 12.dp),
//        factory = {
//            val container = UIView()
//            webView.apply {
//                WKWebViewConfiguration().apply {
//                    allowsInlineMediaPlayback = true
//                    allowsAirPlayForMediaPlayback = true
//                    allowsPictureInPictureMediaPlayback = true
//                }
//                loadHTMLString(content, baseURL = null)
//            }
//            container.addSubview(webView)
//            container
//        },
//        update = {
//            CATransaction.begin()
//            CATransaction.setValue(true, kCATransactionDisableActions)
//            it.layer.setFrame(CGRectZero.readValue())
//            webView.setFrame(CGRectZero.readValue())
//            CATransaction.commit()
//        })
//
//}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun HtmlWebView(
    htmlContent: String,
    modifier: Modifier
) {
    UIKitView(
        modifier = modifier,
        factory = {
            // Create a UILabel to display HTML content
            val label = UILabel()

            // Convert HTML string to NSAttributedString
//            val data = htmlContent.encodeToByteArray().toNSData()
//            val options = mapOf(NSHTMLTextDocumentType to true)
//            val attributedString = NSAttributedString.alloc().initWithData(data, options, NSUTF8StringEncoding)

            // Set the attributed string to the label
//            label.attributedText = attributedString
            label.numberOfLines = 0  // Allow label to expand with content
            label
        }
    )
}

val test ="""
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Privacy Policy</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                line-height: 1.6;
                margin: 20px;
                padding: 20px;
                max-width: 800px;
                margin: auto;
            }
            h1, h2 {
                color: #333;
            }
            p {
                color: #555;
            }
        </style>
    </head>
    <body>
    <h1>Privacy Policy</h1>
    <p>Last updated: 30/1/2025</p>
    <p>Welcome to our Reminder App. Your privacy is important to us. This Privacy Policy explains how we collect, use, and protect your information.</p>

    <h2>1. Information We Collect</h2>
    <p>We collect the following types of information:</p>
    <ul>
        <li><strong>Personal Information:</strong> Name, email address, and profile details when signing up.</li>
        <li><strong>Reminder Data:</strong> Events, tasks, and schedules you create within the app.</li>
        <li><strong>Location Data:</strong> If you enable location-based reminders, we may collect location data to trigger alerts.</li>
        <li><strong>Device Information:</strong> Log data such as IP address, device type, and app usage analytics.</li>
    </ul>

    <h2>2. How We Use Your Information</h2>
    <p>We use the collected data for:</p>
    <ul>
        <li>Providing and improving app functionality.</li>
        <li>Syncing reminders across devices using cloud storage.</li>
        <li>Sending notifications and alerts for your reminders.</li>
        <li>Ensuring app security and fraud prevention.</li>
    </ul>

    <h2>3. Data Sharing and Security</h2>
    <p>We do not sell or share your personal data with third parties, except:</p>
    <ul>
        <li>With trusted service providers (e.g., Firebase) for authentication and data storage.</li>
        <li>When required by law or legal processes.</li>
    </ul>
    <p>We implement strong security measures to protect your data, including encryption and secure cloud storage.</p>

    <h2>4. Your Rights</h2>
    <p>You have the right to:</p>
    <ul>
        <li>Access and update your personal data.</li>
        <li>Delete your account and associated data.</li>
        <li>Control notification and location settings.</li>
    </ul>

    <h2>5. Changes to This Policy</h2>
    <p>We may update this Privacy Policy periodically. We will notify you of any significant changes within the app.</p>

    <h2>6. Contact Us</h2>
    <p>If you have any questions, please contact us at <strong>[Your Email]</strong>.</p>

    <p>By using this app, you agree to this Privacy Policy.</p>
    </body>
    </html>

"""