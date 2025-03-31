import UIKit
import SwiftUI
import ComposeApp
import GoogleMaps


class IOSNativeViewFactory: NativeViewFactory {

    static var shared = IOSNativeViewFactory()

    func createGoogleMap() ->UIViewController{
        return GoogleMapViewController()
    }

}

 // 👈 Ensure a unique Objective-C name
public class GoogleMapViewController: UIViewController {
    
    private var mapView: GMSMapView!
    
    override public func viewDidLoad() {
        super.viewDidLoad()
        
        let options = GMSMapViewOptions()
        options.camera = GMSCameraPosition.camera(withLatitude: 12.952636, longitude: 77.653059, zoom: 10.0)
        mapView = GMSMapView(options: options)
        mapView.frame = view.bounds
        view.addSubview(mapView)

        let marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: 12.952636, longitude: 77.653059)
        marker.title = "Indiranagar"
        marker.snippet = "Bengaluru"
        marker.map = mapView
    }
    
     public func setInitialLocation(latitude: Double, longitude: Double) {
        let camera = GMSCameraPosition.camera(withLatitude: latitude, longitude: longitude, zoom: 10.0)
        mapView.camera = camera
    }
}
