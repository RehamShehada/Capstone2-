package com.example.giftsstore;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.core.content.ContextCompat.startActivity;


public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.signUP) Button signUP;
    @BindView(R.id.name) EditText name;
    @BindView(R.id.password)EditText password;


    private FirebaseAuth mAuth;
    private SharedPreferences s;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        s= getSharedPreferences("userName", Context.MODE_PRIVATE);
        getSavedUserName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.layout_motion);
            getWindow().setEnterTransition(slide);
        }

        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_up();
            }
        });
    }
        private void sign_up(){
            final String Name=name.getText().toString().trim();
            String Pass=password.getText().toString().trim();
            if(TextUtils.isEmpty(Name)||TextUtils.isEmpty(Pass)){
                Snackbar.make(findViewById(R.id.layout),"please fill the required information", Snackbar.LENGTH_LONG).show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(Name, Pass)

                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    SharedPreferences.Editor editor = s.edit();
                                    editor.putString("name", Name);
                                    editor.commit();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

        private void getSavedUserName(){
            String user=s.getString("name",null);
            if (user != null) {
                name.setText(user);
            }







    }
}
