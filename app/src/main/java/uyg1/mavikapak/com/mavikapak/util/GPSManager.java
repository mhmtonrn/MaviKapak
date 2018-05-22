package uyg1.mavikapak.com.mavikapak.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;


public class GPSManager extends Service implements LocationListener {

    private final Context mContext;
    private String provider;
    // flag for GPS Status
    boolean isGPSEnabled = false;

    //flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location;// location
    double latitude;//latitude
    double longitude;//longitude

    //The minimum distance to change updates in meters.
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;//10 meters

    //The minimum time to change updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1; // 1 minute

    //Declaring LocationManager
    protected LocationManager locationManager;

    public GPSManager(Context context){
        this.mContext = context;
        getLocation();
        //onLocationChanged(location);

    }


    @SuppressLint("MissingPermission")
    public Location getLocation(){
        try{
            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            //Criteria criteria = new Criteria();
            //provider = locationManager.getBestProvider(criteria, true);
            //Location location = locationManager.getLastKnownLocation(provider);


            // if (location == null) {
            // request for a single update, and try again.
            // Later will request for updates every 10 mins
            //locationManager.requestSingleUpdate(criteria, this, null);
            // location = locationManager
            //    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // }

            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            ////getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled){
                //no network provider is enabled
            }else{
                this.canGetLocation = true;
                isGPSEnabled =  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                //isGPSEnabled = true;
                //First get location from Network Provider
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    Log.d("Network","Network");
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location !=null){
                            System.out.println("Provider " + provider + " has been selected.");
                            onLocationChanged(location);
                            //latitude = location.getLatitude();
                            //longitude = location.getLongitude();
                        }
                    }

                }
            }


        }catch(Exception e){
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onLocationChanged(Location location){


        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    @Override
    public void onProviderDisabled(String provider){
        Toast.makeText(this, "Disabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }

    /**
     * Function to get latitude
     *
     */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
            Toast.makeText(mContext, "Your Location is - \nLat: " + latitude, Toast.LENGTH_LONG).show();
        }

        //return latitude;
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude(){
        if(location !=null){
            longitude = location.getLongitude();
            Toast.makeText(mContext, "Your Location is - \nLng: " + longitude, Toast.LENGTH_LONG).show();
        }

        //return longitude;
        return longitude;

    }

    /**
     * Function to check if best network provider
     * @return boolean
     */
    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        //Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        //Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled.Do you want to go to settings menu?");

        //On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which){
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);

            }
        });

        //On pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which){
                dialog.cancel();
            }

        });

        //Showing Alert Message
        alertDialog.show();

    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in app
     *
     */

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSManager.this);
        }
    }
}