import FirebaseCore
import SwiftUI
import GoogleSignIn
import GoogleMaps


@main
struct iOSApp: App {
        init() {
            FirebaseApp.configure()
            GMSServices.provideAPIKey("AIzaSyC8kq7XKe208OZSCaxG0qC11ctZ0PGHZJ4")
        }
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    var body: some Scene {
        WindowGroup {
            ContentView().onOpenURL { url in
                GIDSignIn.sharedInstance.handle(url)
            }
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
      _ app: UIApplication,
      open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool {
    
      var handled: Bool
          handled = GIDSignIn.sharedInstance.handle(url)
      if handled {
        return true
      }
      return false
    }
}
