package uyg1.mavikapak.com.mavikapak.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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

    public Profil(Context context) {
        this.mContext=context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        user.getPhotoUrl();

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
        return view;
    }
}

