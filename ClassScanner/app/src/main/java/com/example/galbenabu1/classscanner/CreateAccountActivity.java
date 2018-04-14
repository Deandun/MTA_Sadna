package com.example.galbenabu1.classscanner;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";
    private EditText mEmail, mPassword, mConfirmPassword, mUserName, mAcademic_institution;
    private Button mBtnCreateAccount;

    //storage and auth
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mBtnCreateAccount = (Button) findViewById(R.id.register_btn);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        mUserName = (EditText) findViewById(R.id.userName);
        mAcademic_institution = (EditText) findViewById(R.id.college);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        checkFilePermissions();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };

        mBtnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "mBtnCreateAccount:" );

                String email = mEmail.getText().toString().trim();
                String pass = mPassword.getText().toString().trim();
                String confirmPass = mConfirmPassword.getText().toString().trim();
                String userName = mUserName.getText().toString().trim();
                String academic = mAcademic_institution.getText().toString().trim();

                //boolean isRegister=true;
                boolean isValid= validateForm(email, pass, confirmPass, userName, academic);
                Log.d(TAG, "after validation: is valid- " + isValid );

                if (isValid) {
                    Log.d(TAG, " valid- before createAccount " );

                    createAccount(email, pass);
                    Log.d(TAG, "after createAccount " );

//                    if (isRegister)
//                    {
//                        Log.d(TAG, "onClick: Switching Activities.");
//                        Intent intent = new Intent(CreateAccountActivity.this, UploadActivity.class);
//                        startActivity(intent);
//                    }
                }
            }
        });
    }

//    private String checkValidation(String email, String pass, String confirmPass, String userName, String academic) {
//        String msg = null;
//
//        if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || userName.isEmpty() || academic.isEmpty())
//        {
//            msg= "You didn't fill in all the fields.";
//        }
//
//        else
//        {
//            if(!pass.equals(confirmPass))
//                msg="Passwords do not match.";
//        }
//
//        return msg;
//    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            toastMessage( "Authentication: success.");
                            Log.d(TAG, "onClick: Switching Activities.");
                            Intent intent = new Intent(CreateAccountActivity.this, UploadActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            toastMessage( "Authentication failed- \n"+ task.getException().getMessage());
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm(String email, String pass, String confirmPass, String userName, String academic) {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        if (TextUtils.isEmpty(pass)) {
            mPassword.setError("Required.");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (TextUtils.isEmpty(confirmPass)) {
            mConfirmPassword.setError("Required.");
            valid = false;
        } else {
            mConfirmPassword.setError(null);
        }

        if (TextUtils.isEmpty(userName)) {
            mUserName.setError("Required.");
            valid = false;
        } else {
            mUserName.setError(null);
        }

        if (TextUtils.isEmpty(academic)) {
            mAcademic_institution.setError("Required.");
            valid = false;
        } else {
            mAcademic_institution.setError(null);
        }

        if (!pass.equals(confirmPass)) {
            valid = false;
            mConfirmPassword.setError("Passwords do not match.");
        }
        return valid;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck = CreateAccountActivity.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += CreateAccountActivity.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < M.");
        }
    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
