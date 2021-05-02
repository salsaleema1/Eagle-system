package com.workos.activities;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.workos.R;
import com.workos.adapters.UserSessionManager;
import com.workos.models.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private TextInputLayout fullName, username, email, phoneNumber, password, confirmPass;
    private String tag = "SignupActivity";
    private ImageView profileImg;
    private String pathToImage;// get the path of the image on the device to upload it
    private String mCurrentPhotoPath;//path of the image in case of API >=24
    String encodedString;
    Button signup;
    private int REQUEST_CAMERA = 1, SELECT_IMAGE = 0;

    UserSessionManager session;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.atoolbar);
        setSupportActionBar(toolbar);

        session = new UserSessionManager(getApplicationContext());

        fullName = (TextInputLayout) findViewById(R.id.fullName);
        username = (TextInputLayout) findViewById(R.id.username);
        email = (TextInputLayout) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.password);
        phoneNumber = (TextInputLayout) findViewById(R.id.phoneNumber);
        confirmPass = (TextInputLayout) findViewById(R.id.confirmPassword);
        signup = (Button) findViewById(R.id.signup);

        validteInput();

        signup.setOnClickListener(view -> {

            boolean allFieldsValid = true;
            String fullNameStr = fullName.getEditText().getText().toString();
            String usernameStr = username.getEditText().getText().toString();
            String emailStr = email.getEditText().getText().toString();
            String phoneNumberStr = phoneNumber.getEditText().getText().toString();
            String passStr = password.getEditText().getText().toString();
            String confirmPassStr = confirmPass.getEditText().getText().toString();

            Log.i(tag, "signup" + passStr + " " + confirmPassStr);
            allFieldsValid = validateSignInInput(allFieldsValid, fullNameStr, usernameStr, emailStr, phoneNumberStr, passStr, confirmPassStr);

            if (allFieldsValid && passStr.equals(confirmPassStr)) {


                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                UserModel userModel = new UserModel(fullNameStr, usernameStr, emailStr, passStr, phoneNumberStr);

                reference.child(usernameStr).setValue(userModel);

                // Bitmap bitmap = BitmapFactory.decodeByteArray(newUser.getImageData(), 0, newUser.getImageData().length);
                // profileImg.setImageBitmap(bitmap);
            } else {
                confirmPass.setError("Password not match");
            }
        });

    }

    private boolean validateSignInInput(boolean allFieldsValid, String fullNameStr, String usernameStr, String emailStr, String phoneNumberStr, String passStr, String confirmPassStr) {
        if (fullNameStr.isEmpty()) {
            fullName.setError("This field is necessary");
            allFieldsValid = false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
        }

        if (usernameStr.isEmpty()) {
            username.setError("This field is necessary");
            allFieldsValid = false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
        }
        if (emailStr.isEmpty()) {
            email.setError("This field is necessary");
            allFieldsValid = false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
        }
        if (phoneNumberStr.isEmpty()) {
            phoneNumber.setError("This field is necessary");
            allFieldsValid = false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
        }
        if (passStr.isEmpty()) {
            password.setError("This field is necessary");
            allFieldsValid = false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
        }
        if (confirmPassStr.isEmpty()) {
            confirmPass.setError("This field is necessary");
            allFieldsValid = false;
        } else {
            confirmPass.setError(null);
            confirmPass.setErrorEnabled(false);
        }
        return allFieldsValid;
    }

    private void validteInput() {
        username.getEditText().setOnFocusChangeListener((view, focused) -> {
            if (!focused) {
                Pattern pattern = Pattern.compile("^(?=.{4,20}$)(?![_.'])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
                if (!pattern.matcher(username.getEditText().getText()).matches()) {
                    username.setError("Username is in incorrect format");
                } else {
                    username.setError(null);
                    username.setErrorEnabled(false);
                }
            }
        });
        phoneNumber.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    if (!Patterns.PHONE.matcher(phoneNumber.getEditText().getText()).matches()) {
                        phoneNumber.setError("Phone number is in incorrect format");
                    } else {
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);
                    }
                }
            }
        });
        password.getEditText().setOnFocusChangeListener((view, focused) -> {
            if (!focused) {
                if (password.getEditText().getText().length() < 6) {
                    password.setError("password is too short");
                }
            }
        });
        email.getEditText().setOnFocusChangeListener((view, focused) -> {
            if (!focused) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getEditText().getText()).matches()) {
                    email.setError("Email is in incorrect format");
                } else {
                    email.setError(null);
                    email.setErrorEnabled(false);
                }
            }
        });
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
//        overridePendingTransition(0x7f010010,android.R.anim.slide_out_right);

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //get the image
                    int targetW = profileImg.getWidth();
                    int targetH = profileImg.getHeight();
                    // Get the dimensions of the bitmap
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;
                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
                    // Decode the image file into a Bitmap sized to fill the View
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;
                    bmOptions.inPurgeable = true;
                    pathToImage = mCurrentPhotoPath;
                    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                    profileImg.setImageBitmap(bitmap);

                } else {
                    Bundle bundle = data.getExtras();
                    final Bitmap bmap = (Bitmap) bundle.get("data");
                    Uri tempUri = getImageUri(getApplicationContext(), bmap);
                    pathToImage = getPathFromURI(tempUri);
                    profileImg.setImageBitmap(bmap);
                }
            } else if (requestCode == SELECT_IMAGE) {

                pathToImage = getPathFromURI(data.getData());
                Uri selectedImageUri = data.getData();
                profileImg.setImageURI(selectedImageUri);
            }
        }
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor;
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                    // permission was granted
                } else {
                    // permission denied
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                    // permission was granted
                } else {
                    // permission denied
                }
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Select Profile Photo");
        final Context context = this;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i] == "Camera") {
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri photoURI = null;
                        try {
                            photoURI = FileProvider.getUriForFile(getBaseContext(), "com.example.android.fileprovider", createImageFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    } else {
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i] == "Gallery") {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
                } else if (items[i] == "Cancel") {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
