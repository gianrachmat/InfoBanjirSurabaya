package gq.gianr.infobanjirsurabaya;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;

import java.text.SimpleDateFormat;

import android.graphics.Matrix;
import android.location.Address;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import gq.gianr.infobanjirsurabaya.model.User;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by j on 12/03/2017.
 */

public class LaporFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
    Uri picUri;
    Bitmap myBitmap;
    String address;
    String tanggal;
    double lat, lng; // latitude dan longitude
    GoogleApiClient googleApiClient; // membuat klien dari Google API
    LocationRequest locationRequest; //
    protected Location location; //
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private ProgressDialog mProgressDialog;
    private StorageReference storageReference;
    // [END declare_database_ref]
    private Uri filePath = null;
    String uri;
    View view;
    EditText judul, deskripsi, lokasi;
    Button kirim, klikAmbilFoto;
    ImageView foto;
    private static int UPDATE_INTERVAL = 5000; // 5 sec
    private static int FASTEST_INTERVAL = 2500; // 2.5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    public LaporFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lapor_layout, container, false);
        foto = (ImageView) view.findViewById(R.id.foto);
        judul = (EditText) view.findViewById(R.id.et_judul);
        deskripsi = (EditText) view.findViewById(R.id.et_deskripsi);
        kirim = (Button) view.findViewById(R.id.bt_kirim);
        lokasi = (EditText) view.findViewById(R.id.et_lokasi);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (checkPlayService()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        klikAmbilFoto = (Button) view.findViewById(R.id.pilih_gambar);
        klikAmbilFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(dispatchTakePictureIntent(), 200);
            }
        });
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        return view;
    }

    @SuppressWarnings("VisibleForTests")
    private void submitPost() {
        final String title = judul.getText().toString();
        final String body = deskripsi.getText().toString();

        if (TextUtils.isEmpty(title)) {
            judul.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(body)) {
            deskripsi.setError(REQUIRED);
            return;
        }
        Log.i(TAG, "Uri.EMPTY.equals(filePath):" + Uri.EMPTY.equals(filePath));

        if (Uri.EMPTY.equals(filePath) || filePath == null) {
            Log.i(TAG, "Uri.EMPTY.equals(filePath):" + Uri.EMPTY.equals(filePath));
            Toast.makeText(getContext(), "Pilih foto", Toast.LENGTH_SHORT).show();
            return;
        }

        Date tgl;
        SimpleDateFormat sdf;

        tgl = new Date(System.currentTimeMillis());
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        tanggal = sdf.format(tgl);

        setEditingEnabled(false);
        Toast.makeText(getContext(), "Posting...", Toast.LENGTH_SHORT).show();

        StorageReference sref = storageReference.child(Constans.STORAGE_PATH_UPLOADS
                + System.currentTimeMillis()
                + "."
                + getFileExtension(filePath)
        );

        sref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        setEditingEnabled(true);
//                        mProgressDialog.dismiss();
                        uri = taskSnapshot.getDownloadUrl().toString();
                        Log.i(TAG, "uri sref:" + uri);
                        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);

                                        if (user == null) {
                                            Log.e(TAG, "User " + userId + " is null");
                                            Toast.makeText(getContext(), "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.i(TAG, "uri:" + uri);
                                            writeNewPost(userId, user.username, title, uri, body, address, tanggal, location);
                                        }
                                        setEditingEnabled(true);
                                        changeFragment();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                        // [START_EXCLUDE]
                                        setEditingEnabled(true);
                                    }
                                }
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setEditingEnabled(true);
                        Toast.makeText(getContext(), "Gagal Upload", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        mProgressDialog.setMessage("Uploaded " + ((int) progress) + "%");
                    }
                });
    }

    private Intent dispatchTakePictureIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getContext().getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
//        Intent i = new Intent(
//                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        int RESULT_LOAD_IMAGE = 1;
//        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getContext().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void setEditingEnabled(boolean enabled) {
        judul.setEnabled(enabled);
        deskripsi.setEnabled(enabled);
        if (enabled) {
            hideProgressDialog();
            kirim.setVisibility(View.VISIBLE);
        } else {
            showProgressDialog();
            kirim.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView = (ImageView) view.findViewById(R.id.foto);

            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                filePath = picUri;
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);
                    imageView.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;
                imageView.setImageBitmap(myBitmap);

            }

        }
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }


    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void changeFragment() {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt("pos", 0);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment a = getFragmentManager().findFragmentByTag("lapor");
        if (a != null) getFragmentManager().beginTransaction().remove(a).commit();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.containerView, fragment);
        fragmentTransaction.commit();
    }

    private void writeNewPost(String userId, String username, String title, String uri, String body, String alamat, String tanggal, Location loc) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, uri, body, alamat, tanggal, false, loc.getLatitude(), loc.getLongitude());
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Error", "Koneksi gagal, kode = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        displayLocation();
    }

    private void displayLocation() {
        if (location != null) {
            SmartLocation.with(getContext()).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder();
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        address = builder.toString();
                        lokasi.setText(address);
                    }
                }
            });
        } else {
            address = "Null location";
            lokasi.setText(address);
        }
    }

    private boolean checkPlayService() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int resultCode = googleApi.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApi.isUserResolvableError(resultCode)) {
                googleApi.getErrorDialog(getActivity(), resultCode, 9000).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG).show();
//                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1234);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    protected void stopLocationupdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }


    @Override
    public void onPause() {
        super.onPause();
        stopLocationupdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationupdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayService();
        if (googleApiClient.isConnected()) startLocationUpdates();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }
}
