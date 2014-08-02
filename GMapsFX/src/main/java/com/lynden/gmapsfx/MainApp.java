// java docs : http://rterp.github.io/GMapsFX/apidocs/

package com.lynden.gmapsfx;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapShape;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Example Application for creating and loading a GoogleMap into a JavaFX
 * application
 *
 * @author Rob Terpilowski
 */
public class MainApp extends Application implements MapComponentInitializedListener {

    protected GoogleMapView mapComponent;
    protected GoogleMap map;

    private Button btnZoomIn;
    private Button btnZoomOut;
    private Label lblZoom;
    private Label lblCenter;
    private Label lblClick;
    private ComboBox<MapTypeIdEnum> mapTypeCombo;
    

    @Override
    public void start(final Stage stage) throws Exception {
        mapComponent = new GoogleMapView();
        mapComponent.addMapInializedListener(this);
        
        // define border and styling
        
        BorderPane bp = new BorderPane(); 
        ToolBar tb = new ToolBar();

        bp.setTop(tb);
        bp.setCenter(mapComponent);

        // define border and styling
        
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void mapInitialized() {
        //Once the map has been loaded by the Webview, initialize the map details.
        
                List<LatLong> arylist = new ArrayList<LatLong>();
        try {
            BufferedReader filereader = new BufferedReader(new FileReader("/home/skhokhar/workspace/trackManipulation/rsc/text_files/clean_texts_with_labels/Ellis_National_GPS_061714_152841_000[left].txt"));
            String line;
        
            try {
                
                while((line = filereader.readLine()) != null)
                {
                    
                    System.out.println(line);
                    String[] parts = line.split(" ");
                    
                    LatLong temp = new LatLong(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
                    arylist.add(temp); //new LatLong(double(line), double(line));
                    
                }
                
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                filereader.close();
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        LatLong[] ary = arylist.toArray(new LatLong[0]);
         // read in gps file to plot
        MVCArray mvc = new MVCArray(ary); // function expects a 1D array of latlong objects as input 
        
//        LatLong center = new LatLong(37.400816, -122.053982); // view starts here
        LatLong center = ary[0];
        mapComponent.addMapReadyListener(() -> {
            // This call will fail unless the map is completely ready.
            checkCenter(center);
        });
        
        MapOptions options = new MapOptions();
        options.center(center)
                .mapMarker(true)
                .zoom(20)
                .overviewMapControl(true)
                .panControl(true)
                .rotateControl(false)
                .scaleControl(true)
                .streetViewControl(true)
                .zoomControl(true)
                .mapType(MapTypeIdEnum.SATELLITE);

        map = mapComponent.createMap(options);
        
        map.setHeading(123.2);
        System.out.println("Heading is: " + map.getHeading() );

        MarkerOptions markerOptions = new MarkerOptions();
        LatLong markerLatLong = new LatLong(37.400898, -122.054035);
        markerOptions.position(markerLatLong)
                .title("HONDA RI")
                .visible(true);

        final Marker myMarker = new Marker(markerOptions);

        map.addMarker(myMarker);
        
        
        
//        LatLong[] ary = new LatLong[]{new LatLong(37.400898, -122.054034), new LatLong(37.400898, -122.054033), new LatLong(37.400888, -122.054033)};

        PolylineOptions polyOpts = new PolylineOptions()
                .path(mvc)
                .strokeColor("red")
                .strokeWeight(2);

        Polyline poly = new Polyline(polyOpts);
        map.addMapShape(poly);
        
    }

    private void checkCenter(LatLong center) {
        System.out.println("Testing fromLatLngToPoint using: " + center);
        Point2D p = map.fromLatLngToPoint(center);
        System.out.println("Testing fromLatLngToPoint result: " + p);
        System.out.println("Testing fromLatLngToPoint expected: " + mapComponent.getWidth()/2 + ", " + mapComponent.getHeight()/2);
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.net.useSystemProxies", "true");
        launch(args);
    }

}
