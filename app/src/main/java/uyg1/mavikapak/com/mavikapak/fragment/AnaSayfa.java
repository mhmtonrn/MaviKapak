package uyg1.mavikapak.com.mavikapak.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uyg1.mavikapak.com.mavikapak.R;
import uyg1.mavikapak.com.mavikapak.model.LocationModel;
import uyg1.mavikapak.com.mavikapak.util.CustomDialogClass;
import uyg1.mavikapak.com.mavikapak.util.GPSManager;


@SuppressLint("ValidFragment")
public class AnaSayfa extends Fragment {

    //declaration
    Context mContext;
    MapView mMapView;
    private GoogleMap googleMap;
    public LatLng currentLocation;
    public List<LocationModel> locationModelList;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myRef2;
    String value = "";
    GPSManager gpsManager;


    public AnaSayfa(Context context) {
        this.mContext = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        myRef = database.getReference("Location");
        myRef2 = database.getReference("Location");

        //getting current location
        gpsManager = new GPSManager(mContext);
        currentLocation= new LatLng(gpsManager.getLatitude(),gpsManager.getLongitude());
        Log.d("MyLocation","MyLocation "+currentLocation.toString());
        Toast.makeText(mContext, "location "+currentLocation.toString(), Toast.LENGTH_SHORT).show();
        //end-getting current location






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ana_sayfa, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately
        //declare arraylist
        locationModelList = new ArrayList<LocationModel>();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }



        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                //filling the model
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            LocationModel model = data.getValue(LocationModel.class);

                            locationModelList.add(new LocationModel(model.getActive(),model.getLatitu(),model.getLongttitu()));
                            if (model.getActive().equals("1")){
                                LatLng latLng = new LatLng(Double.parseDouble(model.getLatitu()),Double.parseDouble(model.getLongttitu()));
                                Log.e("Location","Location "+model.toString());

                                List<Address> list1=null;
                                try {
                                    Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());;
                                    list1 = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String countryName = "Mavi Kapak Noktası";
                                try{
                                    countryName = list1.get(0).getCountryName()+list1.get(0).getFeatureName();
                                }catch (Exception e){

                                }


                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(countryName)
                                        .snippet(countryName)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.kapak)));
                                marker.showInfoWindow();
                            }


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //end filling the model

                for (LocationModel model:locationModelList ) {

                    Log.e("Location2","Location "+model.toString());
                    if (model.active=="1"){
                        LatLng latLng = new LatLng(Double.parseDouble(model.latitu),Double.parseDouble(model.longttitu));

                        List<Address> list1=null;
                        try {
                            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());;
                            list1 = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String countryName= list1.get(0).getCountryName()+list1.get(0).getFeatureName();
                        
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(countryName)
                                .snippet(countryName)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.kapak)));
                        marker.showInfoWindow();
                    }

                }



                googleMap.getUiSettings().setZoomGesturesEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                googleMap.setMyLocationEnabled(true);



                float zoomLevel = (float) 12.0;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(zoomLevel).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));





                Toast.makeText(mContext, "location "+currentLocation.toString(), Toast.LENGTH_SHORT).show();

                //marker click
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        ///Toast.makeText(mContext, "bu noktaya rota oluşturacaz :" +currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                        CustomDialogClass cdd=new CustomDialogClass((Activity) mContext,currentUser,database,marker);
                        cdd.show();
                        return false;
                    }
                });
                //end marker click

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    public void onMapClick(LatLng latLng) {

                        myRef=myRef.push();
                        myRef.child("active").setValue("0");
                        myRef.child("latitu").setValue(String.valueOf(latLng.latitude));
                        myRef.child("longttitu").setValue(String.valueOf(latLng.longitude));
                        myRef=myRef2;

                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("Yeni eknen nokta ekran \nyenilernince iptal olacak ancak")
                                .snippet("admin onaylarsa aktif kalır")
                                .icon(BitmapDescriptorFactory.defaultMarker()));

                        marker.showInfoWindow();


                        /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Burayı Mavi Kapak Noktası Olarak Belirlemek İstermisiniz?");
                        builder.setMessage("buraya mesaj ekleriz"+value);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button


                            }
                        });
                        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();*/




                        //map onclick end







                    }

                });
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
