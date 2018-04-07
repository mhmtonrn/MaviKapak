package uyg1.mavikapak.com.mavikapak.sing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uyg1.mavikapak.com.mavikapak.R;
public class RegisterActivity extends AppCompatActivity {

    private EditText mMail,mSifre, mname;
    private Button mGiris;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mGiris=findViewById(R.id.btnRegGiris);
        mGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        mMail=findViewById(R.id.etRegMail);
        mSifre=findViewById(R.id.etRegSifre);
        mname=findViewById(R.id.etRegisim);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");

    }



    private void register() {

        mAuth.createUserWithEmailAndPassword(mMail.getText().toString().trim(), mSifre.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("register", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            kayit(user);
                            Toast.makeText(RegisterActivity.this, "Kayıt İşlemi Başarılı", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    private void kayit(FirebaseUser user) {
        myRef=myRef.child(user.getUid());
        myRef.child("name").setValue(mname.getText().toString());
    }
}

