package uyg1.mavikapak.com.mavikapak.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uyg1.mavikapak.com.mavikapak.R;

public class CustomDialogClass extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public EditText miktar;
    public FirebaseUser user;
    public FirebaseDatabase db;
    public Marker marker;
    public DatabaseReference myref;
    public String sayi="-1";
    private DatabaseReference myRefEmail;
    private String emailSayi="0";

    public CustomDialogClass(Activity a, FirebaseUser currentUser, FirebaseDatabase db, Marker marker) {
        super(a);
        this.c = a;
        this.user = currentUser;
        this.db = db;
        this.marker = marker;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        miktar = findViewById(R.id.miktar);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        myref = db.getReference("Bagis");
        myRefEmail = db.getReference("EmailBagis");

        String markerPosition= marker.getPosition().latitude+""+marker.getPosition().longitude;
        markerPosition=markerPosition.replace(".","_");
        markerPosition=markerPosition.replace(",","-");

        myref.child(markerPosition).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sayi = dataSnapshot.getValue(String.class);


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRefEmail.child(user.getEmail().replace(".","_")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailSayi=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:

                makeDonation();
                Toast.makeText(c, "Teşekkürler", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }




    private void makeDonation() {
        myref = db.getReference("Bagis");
        String markerPosition= marker.getPosition().latitude+""+marker.getPosition().longitude;
        markerPosition=markerPosition.replace(".","_");
        markerPosition=markerPosition.replace(",","-");



        int say=0;
        if (sayi!=null){
            say=Integer.parseInt(sayi.toString());
        }else{
            say=0;
        }
        int toplam= Integer.parseInt(miktar.getText().toString())+ say;
        myref.child(markerPosition).setValue(String.valueOf(toplam));


        int emailsayi2=0;
        if (emailSayi!=null){
            emailsayi2=Integer.parseInt(miktar.getText().toString());
        }else{
            emailsayi2=0;
        }

        int toplam2=Integer.parseInt(miktar.getText().toString())+ emailsayi2;
        myRefEmail.child(user.getEmail().replace(".","_")).setValue(String.valueOf(toplam2));
    }
}