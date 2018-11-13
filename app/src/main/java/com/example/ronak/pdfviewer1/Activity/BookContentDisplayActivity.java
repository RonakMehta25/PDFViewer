package com.example.ronak.pdfviewer1.Activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

public class BookContentDisplayActivity extends AppCompatActivity {
    BasicFileProperties basicFileProperties;
    ProgressBar bookLoadingProgressBar=null;
    String readingMode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_content_display);
        basicFileProperties= (BasicFileProperties) getIntent().getExtras().get("BasicFileProperties");

        Log.d("Got File Properties",basicFileProperties.getFileName());
        bookLoadingProgressBar= findViewById(R.id.bookLoadingProgressBar);
        String name=basicFileProperties.getFileName();
        readingMode= (String) getIntent().getExtras().get("ListingMode");

        if(readingMode.equals("importedBooks"))
        {
            bookLoadingProgressBar.setVisibility(View.VISIBLE);
            new readingAsynTask().execute();
        }
        else
        {
            if(checkIfFileAlreadyPresent(Environment.getExternalStorageDirectory()+"/PDFViewer/Books_Folder/"+name.substring(0,name.length()-3)+"txt"))
            {

                createDialog();

            }
            else
            {
                bookLoadingProgressBar.setVisibility(View.VISIBLE);
                PDFBoxResourceLoader.init(getApplicationContext());
                new readingAsynTask().execute();
            }
        }



    }

    public String loadImportedBooks() {
        String myData = "";
        try {

            FileInputStream fis = new FileInputStream(new File(basicFileProperties.getFilePath()));
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
        }catch (IOException ex){
            Log.e("Error","Error in reading imported file");
            Toast.makeText(BookContentDisplayActivity.this, "Error in reading imported file", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        return myData;
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
            OutputStream output =new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/PDFViewer/Properties_Folder/"+name.substring(0,name.length()-4)+"_config.properties"),false);
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

    public String getPDFData()
    {
        String text="";
        try{
            File file = new File(basicFileProperties.getFilePath());
            Log.d("FilenameRead", "True");
            Log.d("Loading File", "True");
            PDDocument document = PDDocument.load(file);
            Log.d("File Loaded", "True");
            Log.d("Getting File Text", "True");
            PDFTextStripper pdfStripper = new PDFTextStripper();
            long startTime = System.nanoTime();
            text = pdfStripper.getText(document);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            Log.d("Got File Text", "True");
            Log.d("Time taken", duration + "");
            document.close();
            Log.d("bookContent", text);
        }catch (IOException e) {
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

        return text;
    }


    public void writeBookContentToFile(String text)
    {

        try {
            String path = Environment.getExternalStorageDirectory() + "/PDFViewer/Books_Folder/";
            String name = basicFileProperties.getFileName();
            FileOutputStream fos = new FileOutputStream(new File(path + name.substring(0, name.length() - 3) + "txt"),false);
            fos.write(text.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("The File "+basicFileProperties.getFileName()+" is already imported in the library. Do you want to import it again");

        // add a list
        String[] animals = {"Load the book from library", "Load the pdf again"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        readingMode="importedBooks";
                        String name=basicFileProperties.getFileName();
                        name=name.substring(0,name.length()-3)+"txt";
                        basicFileProperties.setFileName(name);
                        basicFileProperties.setFilePath(Environment.getExternalStorageDirectory()+"/PDFViewer/Books_Folder/"+name);
                        bookLoadingProgressBar.setVisibility(View.VISIBLE);
                        new readingAsynTask().execute();
                        break;
                    case 1:
                        readingMode="deviceBooks";
                        PDFBoxResourceLoader.init(getApplicationContext());
                        bookLoadingProgressBar.setVisibility(View.VISIBLE);
                        new readingAsynTask().execute();
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updateUI(final String text)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView BookTextView = (TextView) findViewById(R.id.EntireBook);
                BookTextView.setText(text);
                closeProgressBar();
                // Stuff that updates the UI

            }
        });
    }

    class readingAsynTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            final String textForBook;
            if(readingMode.equals("deviceBooks")) {
                textForBook=getPDFData();
                writeBookContentToFile(textForBook);
                writeBasicPropertyFile();
            }
            else
            {
                textForBook=loadImportedBooks();
            }
            updateUI(textForBook);

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


