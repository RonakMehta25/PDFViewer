package com.example.ronak.pdfviewer1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ProgressBar simpleProgressBar=null;
    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final int EXTERNAL_REQUEST = 138;
    ArrayList<String> fileNameList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleProgressBar= findViewById(R.id.simpleProgressBar);
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 138);

        requestForPermission();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void readPDF()
    {
        simpleProgressBar.setVisibility(View.VISIBLE);
        PDFBoxResourceLoader.init(getApplicationContext());
        new readingAsynTask().execute();
        //simpleProgressBar.setVisibility(View.INVISIBLE);



    }

    public void closeProgressBar()
    {
        simpleProgressBar.setVisibility(View.INVISIBLE);
    }

    class readingAsynTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            File file = new File("/storage/emulated/0/Download/The-Secret-Rhonda-Byrne.pdf");
            Log.d("FilenameRead","True");
            try {
                Log.d("Loading File","True");
                PDDocument document = PDDocument.load(file);
                Log.d("File Loaded","True");
                Log.d("Getting File Text","True");
                PDFTextStripper pdfStripper = new PDFTextStripper();
                final String text = pdfStripper.getText(document);
                Log.d("Got File Text","True");
                Log.d("bookContent",text);
                document.close();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        TextView BookTextView = (TextView) findViewById(R.id.EntireBook);
                        BookTextView.setText(text);
                        closeProgressBar();
                        // Stuff that updates the UI

                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        closeProgressBar();
                        closeProgressBar();
                        Toast.makeText(MainActivity.this, "Error in reading file", Toast.LENGTH_SHORT).show();
                        // Stuff that updates the UI

                    }
                });

            }

            //Do some task
            publishProgress ("");
            return "";
        }

        @Override
        protected void onPreExecute() {
            //Setup precondition to execute some task
        }


        @Override
        protected void onProgressUpdate(String... values) {
            //Update the progress of current task
        }

        @Override
        protected void onPostExecute(String s) {
            //Show the result obtained from doInBackground
        }
    }

    public void sendMessage(View view)
    {

        //walkdir(Environment.getExternalStorageDirectory());
        readPDF();
        /*String fileName[]=new String[fileNameList.size()];
        fileNameList.toArray(fileName);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_listview, fileName);

        ListView listView = (ListView) findViewById(R.id.pdf_list);
        listView.setAdapter(adapter);*/
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
    public void walkdir(File dir) {
        String pdfPattern = ".pdf";


        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        Log.d("FileName=",listFile[i].getName());
                        fileNameList.add(listFile[i].getName());
                        //Do what ever u want

                    }
                }
            }
        }

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
