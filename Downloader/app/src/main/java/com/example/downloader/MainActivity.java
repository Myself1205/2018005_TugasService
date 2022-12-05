package com.example.downloader;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
public class MainActivity extends AppCompatActivity {
    Button btnShowProgress;

    private ProgressDialog ProDialog;
    ImageView My_Image;
    private static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        My_Image = (ImageView) findViewById(R.id.My_Image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                new DownloadFileFromURL().execute(file_url);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    protected Dialog onCreateDialog(int id){
        switch (id){
            case progress_bar_type:
                ProDialog = new ProgressDialog(this);
                ProDialog.setMessage("Downloading file. Please Wait");
                ProDialog.setIndeterminate(false);
                ProDialog.setMax(100);
                ProDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                ProDialog.setCancelable(true);
                ProDialog.show();
                return ProDialog;
            default:
                return null;
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String>

    {
        protected  void onPreExecute(){
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String… f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lenghOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != –1){
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e){
                Log.e("Error : ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        protected void onPostExecute(String file_url){
            dismissDialog(progress_bar_type);

            String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            My_Image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }
}