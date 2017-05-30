package gq.gianr.infobanjirsurabaya;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import java.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gq.gianr.infobanjirsurabaya.model.User;

import static android.app.Activity.RESULT_OK;

/**
 * Created by j on 12/03/2017.
 */

public class LaporFragment extends Fragment{
    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private ProgressDialog mProgressDialog;
    private StorageReference storageReference;
    // [END declare_database_ref]
    private Uri filePath;
    String uri;

    EditText judul, deskripsi;
    Button kirim, klikAmbilFoto;
    ImageView foto;
    public LaporFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lapor_layout, container, false);
        foto = (ImageView) view.findViewById(R.id.foto);
        judul = (EditText) view.findViewById(R.id.et_judul);
        deskripsi = (EditText) view.findViewById(R.id.et_deskripsi);
        kirim = (Button) view.findViewById(R.id.bt_kirim);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        klikAmbilFoto = (Button) view.findViewById(R.id.pilih_gambar);
        klikAmbilFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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

        if (TextUtils.isEmpty(title)){
            judul.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(body)) {
            deskripsi.setError(REQUIRED);
            return;
        }
        Log.i(TAG,"Uri.EMPTY.equals(filePath):"+Uri.EMPTY.equals(filePath));

        if (Uri.EMPTY.equals(filePath)){
            Log.i(TAG,"Uri.EMPTY.equals(filePath):"+Uri.EMPTY.equals(filePath));
            Toast.makeText(getContext(), "Pilih foto", Toast.LENGTH_SHORT).show();
            return;
        }

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
                        Log.i(TAG, "uri sref:"+uri);
                        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);

                                        if (user == null){
                                            Log.e(TAG, "User "+userId+" is null");
                                            Toast.makeText(getContext(), "Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.i(TAG, "uri:"+uri);
                                            writeNewPost(userId, user.username, title, uri, body);
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
                        mProgressDialog.setMessage("Uploaded "+((int) progress)+"%");
                    }
                });
    }

    private void dispatchTakePictureIntent() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        int RESULT_LOAD_IMAGE = 1;
        startActivityForResult(i, RESULT_LOAD_IMAGE);
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

    private void setEditingEnabled(boolean enabled){
        judul.setEnabled(enabled);
        deskripsi.setEnabled(enabled);
        if (enabled){
            hideProgressDialog();
            kirim.setVisibility(View.VISIBLE);
        } else {
            showProgressDialog();
            kirim.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Log.i(TAG,"getData: "+data.getData().toString());
//            filePath = data.getData();
            filePath = data.getData();
            Picasso.with(getContext())
                    .load(filePath)
                    .fit()
                    .into(foto);
//            foto.setImageBitmap(imageBitmap);
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void changeFragment(){
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

    private void writeNewPost(String userId, String username, String title, String uri, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, uri, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
}
