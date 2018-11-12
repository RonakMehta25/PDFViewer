package com.example.ronak.pdfviewer1.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ronak.pdfviewer1.BO.BasicFileProperties;
import com.example.ronak.pdfviewer1.R;

import java.io.File;
import java.util.ArrayList;

public class listDevicePDFActivity extends AppCompatActivity {
    String listingMode="";
    ProgressBar listDevidePDFProgressBar=null;
    MyListAdapter adapter;
    ArrayList<BasicFileProperties> fileBasicPropertyList=new ArrayList<BasicFileProperties>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_device_pdf);
        listDevidePDFProgressBar= findViewById(R.id.listDevidePDFProgressBar);
        listDevidePDFProgressBar.setVisibility(View.VISIBLE);
        if(getIntent().getExtras().get("ListingMode").equals("importedBooks"))
        {
            listingMode="importedBooks";
        }
        else
        {
            listingMode="deviceBooks";
        }
        new listingAsynTask().execute();


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
                        BasicFileProperties basicFilePropertiesObject = new BasicFileProperties(listFile[i].getName(),listFile[i].getPath());
                        fileBasicPropertyList.add(basicFilePropertiesObject);
                        //Do what ever u want

                    }
                }
            }
        }

    }

    public void findAllImportedBooks()
    {

        File dir=new File(Environment.getExternalStorageDirectory()+"/PDFViewer/Books_Folder/");
        File listFile[] = dir.listFiles();
        if(listFile!=null)
        {
            for (int i = 0; i < listFile.length; i++) {
                BasicFileProperties basicFilePropertiesObject = new BasicFileProperties(listFile[i].getName(),listFile[i].getPath());
                fileBasicPropertyList.add(basicFilePropertiesObject);
            }
        }
    }

    public void initializeSearchBar()
    {
        EditText SearchBar= (EditText) findViewById(R.id.bookDeviceSearch);
        SearchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public void closeProgressBar()
    {
        listDevidePDFProgressBar.setVisibility(View.GONE);
    }



    class listingAsynTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            if(listingMode.equals("deviceBooks")) {
            Log.d("listing all pdf files", "True");
            try {


                walkdir(Environment.getExternalStorageDirectory());
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (fileBasicPropertyList != null && fileBasicPropertyList.size() != 0) {
                            //String fileBasics[] = new String[fileBasicPropertyList.size()];
                            //fileBasicPropertyList.toArray(fileBasics);
                            adapter = new MyListAdapter(listDevicePDFActivity.this, fileBasicPropertyList);
                            //ArrayAdapter adapter = new ArrayAdapter<String>(listDevicePDFActivity.this, R.layout.activity_listview, fileBasics);

                            ListView listView = (ListView) findViewById(R.id.pdf_list);
                            listView.setAdapter(adapter);
                        }
                        closeProgressBar();
                        initializeSearchBar();
                        // Stuff that updates the UI

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        closeProgressBar();
                        Toast.makeText(listDevicePDFActivity.this, "Error in listing PDF file", Toast.LENGTH_SHORT).show();
                        // Stuff that updates the UI

                    }
                });

            }
        }
        else
            {
                findAllImportedBooks();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (fileBasicPropertyList != null && fileBasicPropertyList.size() != 0) {
                            //String fileBasics[] = new String[fileBasicPropertyList.size()];
                            //fileBasicPropertyList.toArray(fileBasics);
                            adapter = new MyListAdapter(listDevicePDFActivity.this, fileBasicPropertyList);
                            //ArrayAdapter adapter = new ArrayAdapter<String>(listDevicePDFActivity.this, R.layout.activity_listview, fileBasics);

                            ListView listView = (ListView) findViewById(R.id.pdf_list);
                            listView.setAdapter(adapter);
                        }
                        closeProgressBar();
                        initializeSearchBar();
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


    private class MyListAdapter extends BaseAdapter  implements Filterable {

        private ArrayList<BasicFileProperties> mOriginalValues; // Original Values
        private ArrayList<BasicFileProperties> mDisplayedValues;    // Values to be displayed
        LayoutInflater inflater;


        public class ViewHolder {
            LinearLayout llcontainer;
            TextView fileNameView;
            TextView filePathView;

        }

        public MyListAdapter(Context context, ArrayList<BasicFileProperties> mProductArrayList) {
            this.mOriginalValues = mProductArrayList;
            this.mDisplayedValues = mProductArrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mDisplayedValues.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position,View contentView,ViewGroup parent) {

            ViewHolder holder = null;
            View itemView = contentView;
            if (contentView == null) {
                //itemView = inflater.inflate(R.layout.activity_listview,null);
                holder = new ViewHolder();
                contentView = inflater.inflate(R.layout.activity_listview, null);
                holder.llcontainer= (LinearLayout)contentView.findViewById(R.id.llContainer);
                holder.fileNameView = (TextView) contentView.findViewById(R.id.fileName);
                holder.filePathView = (TextView) contentView.findViewById(R.id.filePath);
                contentView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) contentView.getTag();
            }
            BasicFileProperties basicFileProperties=fileBasicPropertyList.get(position);
            holder.fileNameView.setText(mDisplayedValues.get(position).getFileName());
            holder.filePathView.setText("path:"+mDisplayedValues.get(position).getFilePath());
            /*TextView fileNameView = (TextView) itemView.findViewById(R.id.fileName);
            fileNameView.setText(basicFileProperties.getFileName());
            TextView filePathView = (TextView) itemView.findViewById(R.id.filePath);
            filePathView.setText("path:"+basicFileProperties.getFilePath());*/

            holder.llcontainer.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    Toast.makeText(listDevicePDFActivity.this, mDisplayedValues.get(position).getFileName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (listDevicePDFActivity.this, BookContentDisplayActivity.class);
                    intent.putExtra("BasicFileProperties",mDisplayedValues.get(position));
                    startActivity(intent);
                }
            });

            return contentView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,FilterResults results) {

                    mDisplayedValues = (ArrayList<BasicFileProperties>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<BasicFileProperties> FilteredArrList = new ArrayList<BasicFileProperties>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<BasicFileProperties>(mDisplayedValues); // saves the original data in mOriginalValues
                    }

                    /*
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     */

                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String data = mOriginalValues.get(i).getFileName();
                            if (data.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new BasicFileProperties(mOriginalValues.get(i).getFileName(),mOriginalValues.get(i).getFilePath()));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }





}
