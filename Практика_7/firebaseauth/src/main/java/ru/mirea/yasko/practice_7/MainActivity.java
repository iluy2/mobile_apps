package ru.mirea.yasko.practice_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusTextView = findViewById(R.id.statusText);
        mDetailTextView = findViewById(R.id.emailPasswordTitle);
        mEmailField = findViewById(R.id.editEmail);
        mPasswordField = findViewById(R.id.editPassword);

        findViewById(R.id.signInBtn).setOnClickListener(this);
        findViewById(R.id.creatAccBtn).setOnClickListener(this);
        findViewById(R.id.signOutBtn).setOnClickListener(this);
        findViewById(R.id.verifyEmailBtn).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.editEmail).setVisibility(View.GONE);
            findViewById(R.id.editPassword).setVisibility(View.GONE);
            findViewById(R.id.signInBtn).setVisibility(View.GONE);
            findViewById(R.id.creatAccBtn).setVisibility(View.GONE);

            findViewById(R.id.signOutBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.verifyEmailBtn).setVisibility(View.VISIBLE);

            findViewById(R.id.verifyEmailBtn).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.editEmail).setVisibility(View.VISIBLE);
            findViewById(R.id.editPassword).setVisibility(View.VISIBLE);
            findViewById(R.id.signInBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.creatAccBtn).setVisibility(View.VISIBLE);

            findViewById(R.id.signOutBtn).setVisibility(View.GONE);
            findViewById(R.id.verifyEmailBtn).setVisibility(View.GONE);
        }
    }

    private boolean validateForm(){
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if(TextUtils.isEmpty(email)){
            mEmailField.setError("Required.");
            valid = false;
        } else{
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)){
            mPasswordField.setError("Required.");
            valid = false;
        } else{
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password){
        Log.d(TAG, "creatAccount:" + email);
        if(!validateForm()){
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(String email, String password){
        Log.d(TAG, "signIn: " + email);
        if (!validateForm()){
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else{
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Authentication failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()){
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                    }
                });
    }

    private void signOut(){
        mAuth.signOut();
        updateUI(null);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.creatAccBtn){
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if(i == R.id.signInBtn){
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if(i == R.id.signOutBtn){
            signOut();
        } else if(i == R.id.verifyEmailBtn){
            //sendEmailVerification();
        }
    }
}