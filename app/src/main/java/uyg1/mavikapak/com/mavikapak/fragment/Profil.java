package uyg1.mavikapak.com.mavikapak.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uyg1.mavikapak.com.mavikapak.R;


@SuppressLint("ValidFragment")
public class Profil extends Fragment {

    Context mContext;
    public FirebaseUser user;
    public FirebaseDatabase db;
    private FirebaseAuth mAuth;
    ImageView userImg;
    TextView mName;
    TextView mEmail;
    TextView etBagis;
    private DatabaseReference myRefEmail;
    private DatabaseReference myRef;
    ListView listView;
    List<String> list;
    Geocoder geocoder;

    public Profil(Context context) {
        this.mContext=context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        user.getPhotoUrl();
        db= FirebaseDatabase.getInstance();
        myRefEmail = db.getReference("EmailBagis").child(user.getEmail().replace(".","_"));
        myRef=db.getReference("Bagis");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        userImg = view.findViewById(R.id.imageView);
        mName=view.findViewById(R.id.textView);
        mEmail = view.findViewById(R.id.textView1);
        Picasso.get().load(user.getPhotoUrl()).into(userImg);
        mName.setText(user.getDisplayName());
        mEmail.setText(user.getEmail());
        etBagis = view.findViewById(R.id.textView3);
        listView = view.findViewById(R.id.liste);
        list = new ArrayList<>();
        list.add("Birimlere Göre Kapak Sayısı");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,android.R.id.text1,list);
        listView.setAdapter(adapter);
        init();

        geocoder = new Geocoder(mContext, Locale.getDefault());


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        myRefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etBagis.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    String location= data.getKey();
                    String lat=location.substring(0,17);
                    String lon= location.substring(17,34);
                    lat = lat.replace("_",".");
                    lon = lon.replace("_",".");
                    List<Address> list1 = null;


                    //double lat1=Double.parseDouble(lat);
                    //double lon2=Double.parseDouble(lon);

                    LatLng latLng= new LatLng(Double.valueOf(lat),Double.valueOf(lon));
                    try {
                        list1 = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String countryName= list1.get(0).getCountryName()+list1.get(0).getFeatureName();
                    list.add(countryName+": "+data.getValue(String.class));
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    private void init() {

        myRefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etBagis.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    String location= data.getKey();
                    String lat=location.substring(0,17);
                    String lon= location.substring(17,34);
                    lat = lat.replace("_",".");
                    lon = lon.replace("_",".");
                    List<Address> list1 = null;


                    //double lat1=Double.parseDouble(lat);
                    //double lon2=Double.parseDouble(lon);

                    LatLng latLng= new LatLng(Double.valueOf(lat),Double.valueOf(lon));
                    try {
                       list1 = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String countryName= list1.get(0).getCountryName()+list1.get(0).getFeatureName();
                    list.add(countryName+": "+data.getValue(String.class));
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }


    /**
     * for (DataSnapshot data : dataSnapshot.getChildren()) {
     LocationModel model = data.getValue(LocationModel.class);

     locationModelList.add(new LocationModel(model.getActive(),model.getLatitu(),model.getLongttitu()));
     if (model.getActive().equals("1")){
     LatLng latLng = new LatLng(Double.parseDouble(model.getLatitu()),Double.parseDouble(model.getLongttitu()));
     Log.e("Location","Location "+model.toString());
     Marker marker = googleMap.addMarker(new MarkerOptions()
     .position(latLng)
     .title("Mavi Kapak Noktası")
     .snippet(value)
     .icon(BitmapDescriptorFactory.fromResource(R.mipmap.kapak)));
     marker.showInfoWindow();
     }


     }
     */
}

