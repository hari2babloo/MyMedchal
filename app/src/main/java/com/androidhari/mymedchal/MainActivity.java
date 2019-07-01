package com.androidhari.mymedchal;
 import android.app.AlertDialog;
 import android.app.ProgressDialog;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.database.Cursor;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.graphics.drawable.BitmapDrawable;
 import android.net.Uri;
 import android.os.Build;
 import android.os.Bundle;

 import com.google.android.gms.tasks.Continuation;
 import com.google.firebase.iid.FirebaseInstanceId;
 import com.google.firebase.iid.InstanceIdResult;
 import com.google.firebase.messaging.FirebaseMessaging;
 import com.google.firebase.storage.FirebaseStorage;
 import android.os.Environment;
 import android.provider.MediaStore;
 import android.support.annotation.NonNull;
 import android.support.design.widget.Snackbar;
 import android.support.v4.app.ActivityCompat;
 import android.support.v4.content.ContextCompat;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.CardView;
 import android.text.TextUtils;
 import android.util.Log;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ImageView;
 import android.widget.RadioButton;
 import android.widget.RadioGroup;
 import android.widget.RelativeLayout;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.androidhari.mymedchal.SupportFiles.ScalingUtilities;
 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.Task;
        import com.google.firebase.FirebaseException;
        import com.google.firebase.FirebaseTooManyRequestsException;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.auth.PhoneAuthCredential;
        import com.google.firebase.auth.PhoneAuthProvider;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;
 import com.google.firebase.storage.StorageReference;
 import com.google.firebase.storage.UploadTask;


 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.util.concurrent.TimeUnit;

 import Classess.Signup;
 import Classess.TinyDB;

public class  MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";
ProgressDialog pd;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
String picturepath;
String phonenumbrtxt;
TinyDB tinyDB;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private static int RESULT_LOAD_IMAGE = 1;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    RelativeLayout bottom;
    String gendervalue;
   // LinearLayout top;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;

    private TextView mStatusText;
    private TextView mDetailText;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    String phoneNumber;
    CardView top;
    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton;
    ImageView profileimg;
    EditText  fullnametxt;
    EditText agetxt,emailtxt;
    RadioGroup gendertxt;
    Button createprofilebtn;
    String profileimgurl;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pd= new ProgressDialog(MainActivity.this);
        tinyDB = new TinyDB(this);
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }
        profile();
        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        setNotifications();
        // Assign views
        mPhoneNumberViews = findViewById(R.id.phoneAuthFields);
        mSignedInViews = findViewById(R.id.signedInButtons);

        bottom = (RelativeLayout)findViewById(R.id.bottom);
        top = (CardView)findViewById(R.id.card_view2);
        top.setVisibility(View.GONE);
        mStatusText = findViewById(R.id.statusmsg);
        mDetailText = findViewById(R.id.detail);
        mDetailText.setVisibility(View.GONE);

        mPhoneNumberField = findViewById(R.id.fieldPhoneNumber);
        mVerificationField = findViewById(R.id.fieldVerificationCode);
        mVerificationField.setVisibility(View.GONE);

        mStartButton = findViewById(R.id.buttonStartVerification);
        mVerifyButton = findViewById(R.id.buttonVerifyPhone);
        mVerifyButton.setVisibility(View.GONE);
        mResendButton = findViewById(R.id.buttonResend);
        mResendButton.setVisibility(View.GONE);
        mSignOutButton = findViewById(R.id.signOutButton);

        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("No Internet!!")
                        .setMessage("Please Check your internet connection and Try again.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation'
                                pd.dismiss();
                            }
                        })
                        .show();

                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
//                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
//                            Snackbar.LENGTH_SHORT).show();
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, "Quota exceeded.", Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                mVerificationField.setVisibility(View.VISIBLE);
                mVerifyButton.setVisibility(View.VISIBLE);

                mResendButton.setVisibility(View.VISIBLE);
                mPhoneNumberField.setVisibility(View.GONE);
                mStartButton.setVisibility(View.GONE);
                pd.dismiss();




                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]-
    }

    private void setNotifications() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                      //  String msg = getString("TOKEN", token);
                        Log.d("token", token);
                        tinyDB.putString("token",token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //String msg = getString("subscribed");
                        if (!task.isSuccessful()) {
                            Log.e("Firebasetopic ","Succes Topic Registration");

                        }
                        Log.d(TAG, "Topic Registered");
                        Toast.makeText(MainActivity.this, "Topic Registeredd", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }



    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {

        pd.setCancelable(false);
        pd.show();
        pd.setMessage("Sending OTP");
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this,GetStarted.class));
        //updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, final FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews(mStartButton, mPhoneNumberField);
                disableViews(mVerifyButton, mResendButton, mVerificationField);
                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                disableViews(mStartButton);
                mDetailText.setText(R.string.status_code_sent);
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                mDetailText.setText(R.string.status_verification_failed);
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                mDetailText.setText(R.string.status_verification_succeeded);
                tinyDB.putString("uphone",phoneNumber);
                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                mDetailText.setText(R.string.status_sign_in_failed);
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                break;
        }

        if (user == null) {
            // Signed out
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            mSignedInViews.setVisibility(View.GONE);

            mStatusText.setText(R.string.signed_out);
        } else {
            // Signed in
            mPhoneNumberViews.setVisibility(View.GONE);
            mSignedInViews.setVisibility(View.VISIBLE);

            enableViews(mPhoneNumberField, mVerificationField);
            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);



            mStatusText.setText("One Last Step to Complete your Profile");

            mDetailText.setText(user.getUid());
            pd.setMessage("Gathering Previous Informations");
            pd.show();
            pd.setCancelable(false);
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(mDetailText.getText().toString())) {


                        Log.e("Y","Y");
                        pd.dismiss();
                        tinyDB.putString("uid",user.getUid());

                        startActivity(new Intent(MainActivity.this,Main2Activity.class));
                        // run some code
                    }
                    else {
                        pd.dismiss();
                                                        bottom.setVisibility(View.GONE);

                top.setVisibility(View.VISIBLE);

                        Log.e("N","N");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//            boolean ref = database.getReference().child("users").getKey().equalsIgnoreCase(mDetailText.getText().toString());
//
//            if (ref = true){
//
//                startActivity(new Intent( MainActivity.this,Dashpage.class));
//
//                Log.e("data","data");
//            }
//            else {
//
//                                bottom.setVisibility(View.GONE);
//                top.setVisibility(View.VISIBLE);
//                Log.e("Nodata","nodata");
//            }
//            if (ref.getKey().equals(mDetailText.getText().toString())){
//
//
//                Log.e("Exists","Exists");
//            }
//            else {

//                Log.e("noExists","noExists");
//
//            }


            //DatabaseReference myRef = database.getReference("users").child(mDetailText.getText().toString());


        }
    }

    private boolean validatePhoneNumber() {
        phoneNumber = mPhoneNumberField.getText().toString();
        int length =mPhoneNumberField.getText().length();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }
        else if (!(length ==10)){

            mPhoneNumberField.setError("Enter 10 Digit phone number.");
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStartVerification:
                if (!validatePhoneNumber()) {
                    return;
                }


                phonenumbrtxt = mPhoneNumberField.getText().toString();


                startPhoneNumberVerification(phonenumbrtxt);
                break;
            case R.id.buttonVerifyPhone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.buttonResend:
                resendVerificationCode(phonenumbrtxt, mResendToken);
                break;
            case R.id.signOutButton:
                signOut();
                break;
        }
    }


    private void profile() {




        profileimg = (ImageView)findViewById(R.id.profileimage);
        fullnametxt = (EditText)findViewById(R.id.nametxt);
        agetxt = (EditText)findViewById(R.id.agetxt);
        gendertxt = (RadioGroup)findViewById(R.id.gender);
        emailtxt = (EditText)findViewById(R.id.email);




        createprofilebtn = (Button)findViewById(R.id.createprofilebtn);
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);



            }



        });



        createprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int checkedradiobtn = gendertxt.getCheckedRadioButtonId();
                if (profileimg.getDrawable()==null ){

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Add Profile Image", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if (TextUtils.isEmpty(fullnametxt.getText())){
                    fullnametxt.setError("Please Enter your name");

                }
                else if (TextUtils.isEmpty(emailtxt.getText())){

                    emailtxt.setError("Enter Email");
                }
                else if (TextUtils.isEmpty(agetxt.getText())){

                    agetxt.setError(" Enter your Age");
                }
                else

                if (checkedradiobtn==-1){

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Select Gender", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {

                    pd.setMessage("Creating Profile..");
                    pd.setCancelable(false);
                    pd.show();
                    int radioButtonID = gendertxt.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) gendertxt.findViewById(radioButtonID);
                   gendervalue = (String) radioButton.getText();
                   Log.e("gender valie",gendervalue);

                    StorageReference storage = FirebaseStorage.getInstance().getReference();
                 //   StorageReference storageRef = storage.getReference();

//        StorageReference mountainsRef = storageRef.child("mountains.jpg");

// Create a reference to 'images/mountains.jpg'
                    final StorageReference mountainImagesRef = storage.child("userimages/"+tinyDB.getString("uid")+".jpg");

// While the file names are the same, the references point to different files
                    mountainImagesRef.getName().equals(mountainImagesRef.getName());    // true
                    mountainImagesRef.getPath().equals(mountainImagesRef.getPath());    // false
                    profileimg.setDrawingCacheEnabled(true);
                    profileimg.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) profileimg.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = mountainImagesRef.putBytes(data);



                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Log.e("Status of Upload",task.getResult().toString());
                                profileimgurl = task.getResult().toString();
                                validateprofile();
                            } else {
                                pd.dismiss();
                                Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();

                                // Handle failures
                                // ...
                            }
                        }
                    });
//
//                    uploadTask.addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//
//                            // Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            Log.e("Status of Upload","Failed");
//                            // Handle unsuccessful uploads
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            Task<Uri> dd = mountainImagesRef.getDownloadUrl();
//
//
//                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                            // Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_SHORT).show();
//                            Log.e("Status of Upload",taskSnapshot.getUploadSessionUri().toString());
//                            profileimgurl = taskSnapshot.getUploadSessionUri().toString();
//                            validateprofile();
//
//
//
//
//
//                            // ...
//                        }
//                    });




                }



            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             picturepath = cursor.getString(columnIndex);
            cursor.close();

           // ImageView imageView = (ImageView) findViewById(R.id.imgView);

//           profileimg.setImageBitmap(BitmapFactory.decodeFile(picturepath));

            Log.e("picturepath",picturepath);


            decodefile(picturepath,800,800);

        }


    }


    private String decodefile(String path, int DESIREDWIDTH , int DESIREDHEIGHT) {

        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.CROP);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
                  //  profileimg.setImageBitmap( scaledBitmap);
//                profileimg.setImageBitmap(BitmapFactory.decodeFile(strMyImagePath));
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }

        profileimg.setImageBitmap(BitmapFactory.decodeFile(strMyImagePath));

        return strMyImagePath;


    }
    private void validateprofile() {



                FirebaseDatabase database = FirebaseDatabase.getInstance();

//                myRef.child(mDetailText.getText().toString());


                Log.e("profile Details",fullnametxt.getText().toString()+emailtxt.getText().toString()+agetxt.getText().toString()+mDetailText.getText().toString()+profileimgurl+gendervalue);

                Signup signup = new Signup(fullnametxt.getText().toString(),emailtxt.getText().toString(),agetxt.getText().toString(),mDetailText.getText().toString(),profileimgurl,gendervalue,tinyDB.getString("uphone"));
                  DatabaseReference myRef = database.getReference("users").child(mDetailText.getText().toString());
                myRef.setValue(signup);

//                Map<String,Object> taskMap = new HashMap<>();
//                taskMap.put("userId", mDetailText.getText());
//                taskMap.put("fullname", fullnametxt.getText());
//                taskMap.put("profilimgurl", profileimgurl);
//                taskMap.put("age", agetxt.getText());
//                taskMap.put("gender", gendervalue);
////                taskMap.child("userId").setValue("2");
////                myRef.child("fullname").setValue(fullnametxt.getText().toString());
////                myRef.child("profilimgurl").setValue(profileimgurl);
////                myRef.child("age").setValue(agetxt.getText().toString());
////                myRef.child("gender").setValue(gendervalue);
//                myRef.setValue(taskMap);
                pd.dismiss();
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }






    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {

                    Log.e("value", "Permission Denied, You cannot use local drive .");

                  //  Toast.makeText(this, "Give Permission to upload Picture", Toast.LENGTH_SHORT).show();


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Please Give Access Permission for the App to Work Properly ");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    requestPermission();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                break;
        }
    }
}
