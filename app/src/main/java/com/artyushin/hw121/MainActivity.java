package com.artyushin.hw121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button addButton;
    private SwipeRefreshLayout refreshLayout;
    private CustomAdapter adapter;
    final int REQUEST_ID_READ_PERMISSION = 100;
    final int REQUEST_ID_WRITE_PERMISSION = 200;

    private String fileName = "sample.txt";
    private String dataSaveAll = "";
    private int addSample = 0;
    List<String> result = new ArrayList<> ( );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        listView = findViewById (R.id.list);
        addButton = findViewById (R.id.addButton);
        refreshLayout = findViewById(R.id.swipe_refresh);

        File file = new File(getAppExternalFilesDir().toString (), fileName);
        if (!file.exists()) {
            result.add("Записная книжка");
            result.add("Регистрация");
            result.add("Платеж");
            Toast.makeText(this, "Файл отстутствует. Первая загрузка!", Toast.LENGTH_LONG).show();
        } else {
            askPermissionAndReadFile();
        }

        adapter = new CustomAdapter (result);
        listView.setAdapter (adapter);

        addButton.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( MainActivity.this, AddActivity.class );
                startActivity (intent);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addSample = 1;
                onRestart();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private boolean askPermission(int requestId, String permissionName) {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }

    private void askPermissionAndWriteFile() {
        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(!canWrite)  { return; }
        writeFile();
    }

    private void askPermissionAndReadFile() {
        boolean canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!canRead) { return; }
        loadFile();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_READ_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadFile();
                }
                return;
            case REQUEST_ID_WRITE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeFile();
                }
                return;
        }
    }

    public File getAppExternalFilesDir()  {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            return this.getExternalFilesDir(null);
        } else {
            return Environment.getExternalStorageDirectory();
        }
    }

    public void loadFile() {

        File extStore = this.getAppExternalFilesDir();
        String path = extStore.getAbsolutePath() + "/" + fileName;

        String s = "";
        String fileContent = "";
        try {
            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader (fIn));

            while ((s = myReader.readLine()) != null) {
                fileContent += s;
            }
            myReader.close();

            List<String> data = new ArrayList<> ( );
            String[] arrayContent = fileContent.split(";");
            for(int i = 0; i < arrayContent.length; i++) {
                data.add(String.valueOf(arrayContent[i]));
            }
            result = data;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile() {
        try {
            File extStore = this.getAppExternalFilesDir( );
            String path = extStore.getAbsolutePath() + "/" + fileName;

            File saveFile = new File(path);
            FileOutputStream fOut = new FileOutputStream(saveFile);
            fOut.write(dataSaveAll.getBytes("UTF-8"));
            fOut.close();

            Toast.makeText(getApplicationContext(), "Схранено в файл: " + fileName, Toast.LENGTH_LONG).show();
            dataSaveAll = "";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onRestart() {
        super.onRestart ( );

        if (addSample == 0) {
            result.add(Sample.getSample ());
        } else {
            addSample = 0;
        }

        adapter = new CustomAdapter (result);
        listView.setAdapter (adapter);
        adapter.notifyDataSetChanged ( );

        String dataSave = "";
        for (int i = 0; i < result.size(); i++){
            dataSave = String.valueOf(result.get(i));
            dataSaveAll = dataSaveAll + dataSave + ";";
        }

        askPermissionAndWriteFile();
    }
}