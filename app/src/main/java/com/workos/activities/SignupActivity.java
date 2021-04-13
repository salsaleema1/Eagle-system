package com.workos.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.workos.R;
import com.workos.adapters.UserSessionManager;
import com.workos.api.RequestHandler;
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
    private TextInputLayout username, email, phonenumber;
    private TextInputLayout pass, confirmPass, firstName, lastName;
    private String passStr;
    private String confirmPassStr;
    private String tag = "SignupActivity";
    private ImageView profileImg;
    private String pathToImage;// get the path of the image on the device to upload it
    private String mCurrentPhotoPath;//path of the image in case of API >=24
    String encodedString;
    private ProgressDialog progressDialog;
    private String imgString;
    private Context context = this;

    private int REQUEST_CAMERA = 1, SELECT_IMAGE = 0;

    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestHandler.init(this);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.atoolbar);
        setSupportActionBar(toolbar);

        session = new UserSessionManager(getApplicationContext());

        username = (TextInputLayout) findViewById(R.id.username);
        email = (TextInputLayout) findViewById(R.id.email);
        pass = (TextInputLayout) findViewById(R.id.password);
//        phonenumber = (TextInputLayout) findViewById(R.id.phonenumber);
//        firstName = (EditText) findViewById(R.id.firstname);
//        lastName = (TextInputLayout) findViewById(R.id.lasttname);
        confirmPass = (TextInputLayout) findViewById(R.id.confirmPassword);
//        male = (RadioButton) findViewById(R.id.male);
//        female = (RadioButton) findViewById(R.id.female);
//        male.setChecked(true);
        Button signup = (Button) findViewById(R.id.signup);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    Pattern pattern = Pattern.compile("^(?=.{4,20}$)(?![_.'])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
                    if (!pattern.matcher(username.getEditText().getText()).matches()) {
                        username.setError("Username is in incorrect format");
                    }
                }
            }
        });
//        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean focused) {
//                if (!focused) {
//                    if (pass.getEditText().getText().length()<6) {
//                        pass.setError("password is too short");
//                    }
//                }
//            }
//        });
//        phonenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean focused) {
//                if (!focused) {
//                    if (!Patterns.PHONE.matcher(phonenumber.getEditText().getText()).matches()) {
//                        phonenumber.setError("Phone number is in incorrect format");
//                    }
//                }
//            }
//        });
//        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean focused) {
//                if (!focused) {
//                    if (!Patterns.EMAIL_ADDRESS.matcher(email.getEditText().getText()).matches()) {
//                        email.setError("Email is in incorrect format");
//                    }
//                }
//            }
//        });
        if (signup != null) {
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    passStr = pass.getEditText().getText().toString();
                    confirmPassStr = confirmPass.getEditText().getText().toString();
                    Log.i(tag, "signup" + passStr + " " + confirmPassStr);

                    if (passStr.equals(confirmPassStr)) {
                        UserModel newUser = createNewUser();
                        if (newUser != null) ;
                        signup(newUser);

                        // Bitmap bitmap = BitmapFactory.decodeByteArray(newUser.getImageData(), 0, newUser.getImageData().length);
                        // profileImg.setImageBitmap(bitmap);
                    } else {
                        confirmPass.setError("Password not match ");
                    }
                }
            });
        }
//        profileImg = (ImageView) findViewById(R.id.profileImage);
//        profileImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getImage();
//            }
//        });
        progressDialog = new ProgressDialog(SignupActivity.this);

    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
//        overridePendingTransition(0x7f010010,android.R.anim.slide_out_right);

    }


    private void signup(final UserModel newUser)//
    {
        // UserController user = new UserController(this);
        //create user in the server
        //   String signupRespnoce = user.createUser(progressDialog, newUser);

//        requestHandler.createUser(new RequestHandler.VolleyCallback() {
//            @Override
//            public void onSuccess(String result) {
//                Log.i(tag, result);
//                try {
//                    //log in after signup
//                    JSONObject jsonresponce = new JSONObject(result);
//
//                    String res = jsonresponce.getString("message");
//                    if (res.equals("Account created successfly.")) {
//                        UserController user = UserController.getIns();
//                        UserController.init(getApplicationContext());
//
//                        boolean loginResult = user.login(progressDialog, newUser.getUsername(), newUser.getPassword());//get log in result from the username and password from the server
//                        if (loginResult) {
//                            Log.i(tag, "log in successful");
//                            //    Log.i(tag, "the resulting token is restored from preferences" + user.getToken());
//
//                            session.createUserLoginSession(newUser.getUsername());
//                            Intent in = new Intent(SignupActivity.this, MainPageActivity.class);
//                            startActivity(in);
//                            setResult(RESULT_OK, null);
//
//                            finish();
//
//                        } else {
//                            pass.getEditText().setText("");
//                            Log.i(tag, "log in is unsuccessful");
//                        }
//
//                        Log.i(tag, "    signup  successful");
//                        //    onBackPressed();
//                    } else {
//                        Log.i(tag, "signup is unsuccessful");
//                    }
//                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onSuccess(JSONObject result) {
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        }, newUser);
        Log.i(tag, "Trying to sign in");

    }

    private UserModel createNewUser() {

        String usernameStr = username.getEditText().getText().toString();
        String emailStr = email.getEditText().getText().toString();
//        String phonenumberStr = phonenumber.getEditText().getText().toString();
//        String firstNameStr = firstName.getEditText().getText().toString();
//        String lastNameStr = lastName.getEditText().getText().toString();

        if (usernameStr.isEmpty()) {
            username.setError("This field is necessary ");
        }
        if (emailStr.isEmpty()) {
            email.setError("This field is necessary ");
        }
        if (passStr.isEmpty()) {
            pass.setError("This field is necessary ");
        }
        if (confirmPassStr.isEmpty()) {
            confirmPass.setError("This field is necessary ");
        }
//        if (firstNameStr.isEmpty()) {
//            firstName.setError("This field is necessary ");
//        }
//        if (lastNameStr.isEmpty()) {
//            lastName.setError("This field is necessary ");
//        }
//        if (phonenumberStr.isEmpty()) {
//            phonenumber.setError("This field is necessary ");
//        }

        UserModel newUser = new UserModel();
        newUser.setUsername(usernameStr);
//        newUser.setFirstname(firstNameStr);
//        newUser.setLastname(lastNameStr);
        newUser.setEmail(emailStr);
        newUser.setPassword(passStr);
//        newUser.setPhonenumber(phonenumberStr);
        if (pathToImage != null) {
            Log.i(tag, "path to image  " + pathToImage);
            //get the image file and convert it to bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(pathToImage);
            // change the format of the image compressed
            // now it is set up to 640 x 480;
            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 640, 480, true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // CompressFormat set up to JPG, you can change to PNG or whatever we want;
            bmpCompressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            //
            byte[] data = byteArrayOutputStream.toByteArray();
            encodedString = Base64.encodeToString(data, Base64.DEFAULT);

            newUser.setImageData(data);
        } else {
            newUser.setImageData(null);

        }
        return newUser;
    }

    private void getImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(this, "External Storage permission required", Toast.LENGTH_SHORT);
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                Toast.makeText(this, "External Storage permission required", Toast.LENGTH_SHORT);
            else {
                ActivityCompat.requestPermissions(SignupActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            selectImage();
        }
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
