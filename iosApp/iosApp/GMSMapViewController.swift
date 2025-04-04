import UIKit
import SwiftUI
import ComposeApp
import GoogleMaps

    class IOSNativeViewFactory: NativeViewFactory {

        static var shared = IOSNativeViewFactory()

        func createGoogleMap(interaction: MapInteraction) ->UIViewController{
        print("creating map")
            return GoogleMapViewController(action: interaction)
        }

        func updateGoogleMapMarker(view:UIViewController,latitude: Double, longitude: Double){
                print("updating map")

            if let googleMapViewController = view as? GoogleMapViewController {
                googleMapViewController.updateMarker(latitude: latitude, longitude: longitude)
            }
        }
    }

    public class GoogleMapViewController: UIViewController,GMSMapViewDelegate {

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
            print("Location received for camera: Latitude = \(interaction.getCameraLat()), Longitude = \(interaction.getCameraLong())")
            super.viewDidLoad()
            let camera = GMSCameraPosition.camera(
                withLatitude: CLLocationDegrees(interaction.getCameraLat()),
                longitude: CLLocationDegrees(interaction.getCameraLong()),
                zoom: 10.0
            )

            print("drawing first cam")

            mapView = GMSMapView(frame: .zero, camera: camera)
            mapView.frame = view.bounds
            mapView.translatesAutoresizingMaskIntoConstraints = false
            view.addSubview(mapView)

            NSLayoutConstraint.activate([
                mapView.topAnchor.constraint(equalTo: view.topAnchor),
                mapView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
                mapView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
                mapView.trailingAnchor.constraint(equalTo: view.trailingAnchor)
            ])
            mapView.settings.setAllGesturesEnabled(false)
            mapView.delegate = self

            // Enable user interaction
            mapView.isUserInteractionEnabled = interaction.showControl() == true
            mapView.settings.scrollGestures = interaction.showControl() == true   // Enable scrolling

            if(interaction.showControl() == true){
              addZoomControls()
            }

            if let latitude = interaction.getMarkerLat() as? Double,
               let longitude = interaction.getMarkerLong() as? Double,
               latitude != 0.0, longitude != 0.0 {
            setMarker(latitude: latitude, longitude: longitude)
            }
        }

        @objc public func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
            if(interaction.enableSetMarker() == true ){
                mapView.clear()
                setMarker(latitude: coordinate.latitude, longitude: coordinate.longitude)
                interaction.onMarkerSet(long:coordinate.longitude ,lat: coordinate.latitude)
            }
        }

         private func setMarker(latitude: Double, longitude: Double) {
                print("setting marker cam")

                let marker = GMSMarker()
                marker.position = CLLocationCoordinate2D(latitude: CLLocationDegrees(latitude), longitude: CLLocationDegrees(longitude))
                marker.map = mapView

                let currentZoom = mapView.camera.zoom
                let currentCamera = mapView.camera

                let camera = GMSCameraPosition.camera(withLatitude: CLLocationDegrees(latitude), longitude: CLLocationDegrees(longitude), zoom: currentZoom)

                if currentCamera.target.latitude != camera.target.latitude || currentCamera.target.longitude != camera.target.longitude {
                    print("updating cam location")
                    mapView.animate(to: camera)
                }
            }


            @objc public func updateMarker(latitude: Double, longitude: Double) {
              mapView.clear()
              if latitude != 0.0, longitude != 0.0 {
                       setMarker(latitude: latitude, longitude: longitude)
              }
            }



        private func addZoomControls() {
            let buttonSize: CGFloat = 50
            let offset: CGFloat = 20

            // Zoom In Button
            let zoomInButton = UIButton()
            zoomInButton.setTitle("+", for: .normal)
            zoomInButton.backgroundColor = .white
            zoomInButton.setTitleColor(.black, for: .normal)
            zoomInButton.layer.cornerRadius = buttonSize / 2
            zoomInButton.addTarget(self, action: #selector(zoomIn), for: .touchUpInside)

            // Zoom Out Button
            let zoomOutButton = UIButton()
            zoomOutButton.setTitle("-", for: .normal)
            zoomOutButton.backgroundColor = .white
            zoomOutButton.setTitleColor(.black, for: .normal)
            zoomOutButton.layer.cornerRadius = buttonSize / 2
            zoomOutButton.addTarget(self, action: #selector(zoomOut), for: .touchUpInside)

            // Add buttons to the view
            view.addSubview(zoomInButton)
            view.addSubview(zoomOutButton)

            // Set Auto Layout constraints
            zoomInButton.translatesAutoresizingMaskIntoConstraints = false
            zoomOutButton.translatesAutoresizingMaskIntoConstraints = false

            NSLayoutConstraint.activate([
                zoomInButton.widthAnchor.constraint(equalToConstant: buttonSize),
                zoomInButton.heightAnchor.constraint(equalToConstant: buttonSize),
                zoomInButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20),
                zoomInButton.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -160),

                zoomOutButton.widthAnchor.constraint(equalToConstant: buttonSize),
                zoomOutButton.heightAnchor.constraint(equalToConstant: buttonSize),
                zoomOutButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20),
                zoomOutButton.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -100)
            ])
        }

        @objc private func zoomIn() {
            let zoom = mapView.camera.zoom + 1
            mapView.animate(toZoom: zoom)
        }

        @objc private func zoomOut() {
            let zoom = mapView.camera.zoom - 1
            mapView.animate(toZoom: zoom)
        }

         deinit {
                print("GoogleMapViewController deallocated")
                mapView.clear() // Clean up markers and other objects
            }
    }
