package gq.gianr.infobanjirsurabaya;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by j on 23/07/2017.
 */

public class GetLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    String TAG = "GetLocation";
    private GetLoc delegate = null;
    Context context;
    GoogleApiClient googleApiClient; // membuat klien dari Google API
    LocationRequest locationRequest; //
    protected Location location; //
    private static int UPDATE_INTERVAL = 5000; // 5 sec
    private static int FASTEST_INTERVAL = 2500; // 2.5 sec
    private static int DISPLACEMENT = 500; // 500 meters
    String address;
    Activity activity;

    public GetLocation(GetLoc delegate, Context context, Activity activity) {
        this.delegate = delegate;
        this.context = context;
        this.activity = activity;
        if (checkPlayService()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected: ");
//        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended: ");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Error", "Koneksi gagal, kode = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        System.out.println("location");
        delegate.onLocation(this.location);

//        displayLocation();
    }

    private void displayLocation() {
        if (location != null) {
            System.out.println("location");
            delegate.onLocation(location);
        } else {
            delegate.onLocation(location);
            address = "Null location";
        }
    }

    void onStart() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private boolean checkPlayService() {
        Log.i(TAG, "checkPlayService: ");
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int resultCode = googleApi.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApi.isUserResolvableError(resultCode)) {
                googleApi.getErrorDialog(activity, resultCode, 9000).show();
            } else {
                Toast.makeText(context.getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG).show();
//                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "buildGoogleApiClient: ");
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        Log.i(TAG, "createLocationRequest: ");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates: ");
        if (ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1234);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    protected void stopLocationupdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    interface GetLoc {
        void onLocation(Location location);
    }
}
