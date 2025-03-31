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
//         let options = GMSMapViewOptions()
//         options.camera = GMSCameraPosition.camera(withLatitude: interaction.getMarkerLat(), longitude: interaction.getMarkerLong(), zoom: 10.0)
//
        let camera = GMSCameraPosition.camera(
                    withLatitude: interaction.getMarkerLat(),
                    longitude: interaction.getMarkerLong(),
                    zoom: 10.0
                )

        mapView = GMSMapView(frame: .zero, camera: camera)
            mapView.frame = view.bounds
//               mapView.translatesAutoresizingMaskIntoConstraints = false
    mapView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(mapView)
        NSLayoutConstraint.activate([
            mapView.topAnchor.constraint(equalTo: view.topAnchor),
            mapView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            mapView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            mapView.trailingAnchor.constraint(equalTo: view.trailingAnchor)
        ])
        let marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: interaction.getMarkerLat(), longitude:interaction.getMarkerLong())
        marker.map = mapView
    }
}
