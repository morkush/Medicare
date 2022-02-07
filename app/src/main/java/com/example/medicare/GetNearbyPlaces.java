package com.example.medicare;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object,String,String> {

    private String googleplaceData;
     String url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {

        mMap=(GoogleMap)objects[0];
        url=(String)objects[1];

        DownloadUrl downloadUrl=new DownloadUrl();
        try {
            googleplaceData=downloadUrl.ReadTheURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleplaceData;
    }
    @Override
    protected void onPostExecute(String s){
        List<HashMap<String,String>> nearByPlacesList=null;
        DataParser dataParser=new DataParser();
        nearByPlacesList=dataParser.parse(s);
        Log.d("nearbyplacesdata","called parse method");

        DisplayNearbyPlaces(nearByPlacesList);
    }

    private void DisplayNearbyPlaces(List<HashMap<String,String>> nearByPlacesList){
        for(int i=0;i<nearByPlacesList.size();i++){
            MarkerOptions markerOptions=new MarkerOptions();
            HashMap<String,String>googleNearbyPlace= nearByPlacesList.get(i);
            String nameofPlace= googleNearbyPlace.get("place_name");
            String vicinity= googleNearbyPlace.get("vicinity");
            double lat=Double.parseDouble(googleNearbyPlace.get("lat"));
            double lng=Double.parseDouble(googleNearbyPlace.get("lng"));
            LatLng latLng=new LatLng(lat,lng);
          markerOptions.position(latLng);
            markerOptions.title(nameofPlace+" : "+vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            //currentUserLocationMarker=mMap.addMarker(markerOptions);
            mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
        }
    }
}
