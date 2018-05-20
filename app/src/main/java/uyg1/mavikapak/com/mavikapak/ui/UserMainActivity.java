package uyg1.mavikapak.com.mavikapak.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import uyg1.mavikapak.com.mavikapak.R;
import uyg1.mavikapak.com.mavikapak.fragment.AnaSayfa;
import uyg1.mavikapak.com.mavikapak.fragment.Profil;

public class UserMainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    ActionBar actionBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        actionBar = getSupportActionBar();
        Log.d("Check","Anasayfa başladı");

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.listFragment, new AnaSayfa(getApplicationContext())).commit();
        actionBar.setSubtitle(R.string.anasayfa);
    }





    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft.replace(R.id.listFragment, new AnaSayfa(getApplicationContext())).commit();
                    actionBar.setSubtitle(R.string.anasayfa);
                    return true;
                case R.id.navigation_dashboard:
                    ft.replace(R.id.listFragment, new Profil(getApplicationContext())).commit();
                    actionBar.setSubtitle(R.string.profil);
                    return true;

                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };



}
