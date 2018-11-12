package com.example.ronak.pdfviewer1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import com.example.ronak.pdfviewer1.Activity.ListImportedBooksActivity;
import com.example.ronak.pdfviewer1.Activity.listDevicePDFActivity;
import com.example.ronak.pdfviewer1.R;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final int EXTERNAL_REQUEST = 138;
    ArrayList<String> fileNameList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 138);

        requestForPermission();
        File Mainfolder = new File(Environment.getExternalStorageDirectory()+"/PDFViewer");
        boolean success = true;
        if (!Mainfolder.exists()) {
            success = Mainfolder.mkdirs();
        }

        if (success) {
            Log.d("Folder Creation","Success");
            File propertiesFolder = new File(Environment.getExternalStorageDirectory()+"/PDFViewer/Properties_Folder");
            if (!propertiesFolder.exists()) {
                success = propertiesFolder.mkdirs();
                Log.d("Property Folder","Success");
            }
            File booksFolder = new File(Environment.getExternalStorageDirectory()+"/PDFViewer/Books_Folder");
            if (!booksFolder.exists()) {
                success = booksFolder.mkdirs();
                Log.d("Books Folder","Success");
            }
            // Do something on success
        } else {
            Log.d("Folder Creation","Failed");
            // Do something else on failure
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    public void goToListDevicePDFActivity(View view)
    {
        Intent intent = new Intent (this, listDevicePDFActivity.class);
        intent.putExtra("ListingMode","deviceBooks");
        startActivity(intent);
    }

    public void goToimportedBooksActivity(View view)
    {
        Intent intent = new Intent (this, listDevicePDFActivity.class);
        intent.putExtra("ListingMode","importedBooks");
        startActivity(intent);
    }





    public void goToCurrentBooksActivity(View view)
    {
        Intent intent = new Intent (this, ListImportedBooksActivity.class);
        startActivity(intent);
    }




    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
