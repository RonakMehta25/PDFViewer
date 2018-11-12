package com.example.ronak.pdfviewer1.Activity;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ronak.pdfviewer1.BO.BasicFileProperties;
import com.example.ronak.pdfviewer1.R;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class BookContentDisplayActivity extends AppCompatActivity {
    BasicFileProperties basicFileProperties;
    ProgressBar bookLoadingProgressBar=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_content_display);
        basicFileProperties= (BasicFileProperties) getIntent().getExtras().get("BasicFileProperties");

        Log.d("Got File Properties",basicFileProperties.getFileName());
        bookLoadingProgressBar= findViewById(R.id.bookLoadingProgressBar);
        String name=basicFileProperties.getFileName();
        if(checkIfFileAlreadyPresent(Environment.getExternalStorageDirectory()+"/PDFViewer/Books_Folder/"+name.substring(0,name.length()-3)+"txt"))
        {

        }
        else
        {
            bookLoadingProgressBar.setVisibility(View.VISIBLE);
            PDFBoxResourceLoader.init(getApplicationContext());
            new readingAsynTask().execute();
        }


    }

    public boolean checkIfFileAlreadyPresent(String filePath)
    {
        File file = new File(filePath);
        if(file.exists())
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public void writeBasicPropertyFile()
    {
        try{
            Properties prop = new Properties();
            String name=basicFileProperties.getFileName();
            OutputStream output =new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/PDFViewer/Properties_Folder/"+name.substring(0,name.length()-4)+"_config.properties"));
            prop.setProperty("FileName",name.substring(0,name.length()-4));
            prop.setProperty("FilePath",basicFileProperties.getFilePath());
            prop.store(output,null);
        }catch(IOException io) {
        io.printStackTrace();
    }

    }
    public void closeProgressBar()
    {
        bookLoadingProgressBar.setVisibility(View.GONE);
    }

    class readingAsynTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            File file = new File(basicFileProperties.getFilePath());
            Log.d("FilenameRead","True");
            try {
                Log.d("Loading File","True");
                PDDocument document = PDDocument.load(file);
                Log.d("File Loaded","True");
                Log.d("Getting File Text","True");
                PDFTextStripper pdfStripper = new PDFTextStripper();
                long startTime = System.nanoTime();
                final String text = pdfStripper.getText(document);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                Log.d("Got File Text","True");
                Log.d("bookContent",text);
                String path=Environment.getExternalStorageDirectory()+"/PDFViewer/Books_Folder/";
                String name=basicFileProperties.getFileName();
                FileOutputStream fos = new FileOutputStream(new File(path+name.substring(0,name.length()-3)+"txt"));
                fos.write(text.getBytes());
                fos.close();
                writeBasicPropertyFile();
                Log.d("Time taken",duration+"");
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
                        Toast.makeText(BookContentDisplayActivity.this, "Error in reading file", Toast.LENGTH_SHORT).show();
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

}


