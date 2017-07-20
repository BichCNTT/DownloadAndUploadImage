package com.example.asus.downloadanduploadimage;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    String string_url = "http://mobileadvertisingwatch.com/wp-content/uploads/2016/02/Can-You-Name-the-Best-Android-App-Dev-Companies-GoodFirms-Just-Released-Its-List.jpg";
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.image_view)
    ImageView imageView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        DownLoadTask downLoadTask = new DownLoadTask();
        downLoadTask.execute(string_url);
    }

    class DownLoadTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... voids) {
            String path = voids[0];
            int fileLength = 0;
            int count = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                fileLength = urlConnection.getContentLength();
                File newFolder = new File(Environment.getExternalStorageDirectory().getPath());
                //nếu k tồn tại thì tạo tệp sdcard mới
                if (!newFolder.exists()) {
                    newFolder.mkdir();

                }
                File fileInput = new File(newFolder, "download_image.jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream());
                OutputStream outputStream = new FileOutputStream(fileInput);
                byte[] data = new byte[1024];
                int total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) total * 100 / fileLength);
                    outputStream.write(data, 0, count);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download complete";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Download in progress...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String aVoid) {
            progressDialog.hide();
            Toast.makeText(getApplicationContext(), aVoid, Toast.LENGTH_LONG).show();
            String path = Environment.getExternalStorageDirectory().getPath().toString()+"/download_image.jpg";
            imageView.setImageDrawable(Drawable.createFromPath(path));
        }
    }
}
