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

import uyg1.mavikapak.com.mavikapak.R;
import uyg1.mavikapak.com.mavikapak.UserMainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private EditText etMail,etSifre;
    private Button btnGiris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        etMail=findViewById(R.id.etMail);
        etSifre=findViewById(R.id.etSifre);
        btnGiris=findViewById(R.id.btnGiris);
        btnGiris.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGiris:
                if (etMail.getText().toString().trim().isEmpty()==true&& etSifre.getText().toString().trim().isEmpty()==true){
                    Toast.makeText(this, "alanları doldurun", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    login(etMail.getText().toString().trim(),etSifre.getText().toString().trim());
                }

                break;
                default:break;

        }
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Giris", "signInWithEmail:success");
                            currentUser = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), UserMainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("giris", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void kaydol(View view) {

        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }
}
