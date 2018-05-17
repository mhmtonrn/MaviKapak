package uyg1.mavikapak.com.mavikapak.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import uyg1.mavikapak.com.mavikapak.R;


@SuppressLint("ValidFragment")
public class AnaSayfa extends Fragment {

    Context mContext;
    MapView mMapView;
    private GoogleMap googleMap;
    public LatLng location;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String value = "";

    public AnaSayfa(Context context) {
        this.mContext = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        myRef = database.getReference("User");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ana_sayfa, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                googleMap.getUiSettings().setZoomGesturesEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(40.9792, 29.7214);
                //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                float zoomLevel = (float) 18.0;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


                    public void onMapClick(LatLng latLng) {
                        location = latLng;
                        //map onclick start

                        myRef.child("User").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                value = dataSnapshot.getValue(String.class);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Burayı Mavi Kapak Noktası Olarak Belirlemek İstermisiniz?");
                        builder.setMessage("buraya mesaj ekleriz"+value);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(location)
                                        .title("Mavi Kapak Noktası")
                                        .snippet(value)
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.kapak)));

                                marker.showInfoWindow();

                            }
                        });
                        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();




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
