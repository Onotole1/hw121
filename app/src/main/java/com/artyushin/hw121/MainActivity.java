package com.artyushin.hw121;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout refreshLayout;
    List<String> result = new ArrayList<>();
    private final CustomAdapter adapter = new CustomAdapter(result, new RemoveClickListener() {
        @Override
        public void onRemoveClicked(int position) {
            result.remove(position);
            adapter.notifyDataSetChanged();
            saveResultToFile();
        }
    });

    private static final String FILE_NAME = "sample.txt";
    private final StringBuilder dataSaveAll = new StringBuilder();
    private int addSample = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        Button addButton = findViewById(R.id.addButton);
        refreshLayout = findViewById(R.id.swipe_refresh);

        File file = new File(getAppExternalFilesDir().toString(), FILE_NAME);
        if (!file.exists()) {
            result.add("Записная книжка");
            result.add("Регистрация");
            result.add("Платеж");
            Toast.makeText(this, "Файл отстутствует. Первая загрузка!", Toast.LENGTH_LONG).show();
        } else {
            loadFile();
            adapter.notifyDataSetChanged();
        }

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            // TODO startActivityForResult
            startActivity(intent);
        });

        refreshLayout.setOnRefreshListener(() -> {
            // TODO Это точно нужно?
            addSample = 1;
            onRestart();
            refreshLayout.setRefreshing(false);
        });
    }

    private File getAppExternalFilesDir() {
        return getExternalFilesDir(null);
    }

    private void loadFile() {

        File extStore = this.getAppExternalFilesDir();
        String path = extStore.getAbsolutePath() + "/" + FILE_NAME;

        StringBuilder fileContent = new StringBuilder();
        File myFile = new File(path);
        // Руками не закрываем ресурсы https://habr.com/ru/post/178405/
        try(FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn))) {

            String s;
            while ((s = myReader.readLine()) != null) {
                fileContent.append(s);
            }

            String[] arrayContent = fileContent.toString().split(";");
            result.addAll(Arrays.asList(arrayContent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile() {
        File extStore = this.getAppExternalFilesDir();
        String path = extStore.getAbsolutePath() + "/" + FILE_NAME;
        File saveFile = new File(path);
        // Руками не закрываем ресурсы https://habr.com/ru/post/178405/
        try(FileOutputStream fOut = new FileOutputStream(saveFile)) {
            //noinspection CharsetObjectCanBeUsed
            fOut.write(dataSaveAll.toString().getBytes("UTF-8"));

            Toast.makeText(getApplicationContext(), "Сохранено в файл: " + FILE_NAME, Toast.LENGTH_LONG).show();
            dataSaveAll.setLength(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onRestart() {
        super.onRestart();

        // TODO Не всегда работает. Используйте лучше startActivityForResult
        if (addSample == 0) {
            result.add(Sample.getSample());
        } else {
            addSample = 0;
        }

        adapter.notifyDataSetChanged();

        saveResultToFile();
    }

    private void saveResultToFile() {
        String dataSave;
        for (int i = 0; i < result.size(); i++) {
            dataSave = String.valueOf(result.get(i));
            // В циклах лучше StringBuilder
            dataSaveAll.append(dataSave).append(";");
        }

        writeFile();
    }
}