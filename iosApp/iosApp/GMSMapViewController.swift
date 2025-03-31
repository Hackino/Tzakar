import UIKit
import SwiftUI
import ComposeApp
import GoogleMaps


class IOSNativeViewFactory: NativeViewFactory {

    static var shared = IOSNativeViewFactory()
    func createGoogleMap(interaction: MapInteraction) ->UIViewController{
        return GoogleMapViewController(action: interaction)
    }
}

public class GoogleMapViewController: UIViewController {
    
    private var mapView: GMSMapView!
    var interaction: MapInteraction!

    init(action: MapInteraction) {
            self.interaction = action
            super.init(nibName: nil, bundle: nil)
        }

           required init?(coder: NSCoder) {
                fatalError("init(coder:) has not been implemented")
            }

    override public func viewDidLoad() {
        super.viewDidLoad()
        
        let options = GMSMapViewOptions()
        options.camera = GMSCameraPosition.camera(withLatitude: interaction.getMarkerLat(), longitude: interaction.getMarkerLong(), zoom: 10.0)
        mapView = GMSMapView(options: options)
        mapView.frame = view.bounds
        view.addSubview(mapView)
        let marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: 12.952636, longitude: 77.653059)
        marker.map = mapView
    }
    
     public func setInitialLocation(latitude: Double, longitude: Double) {
        let camera = GMSCameraPosition.camera(withLatitude: latitude, longitude: longitude, zoom: 10.0)
        mapView.camera = camera
    }
}
