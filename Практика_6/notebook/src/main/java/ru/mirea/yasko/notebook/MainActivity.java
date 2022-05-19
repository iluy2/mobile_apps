package ru.mirea.yasko.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editText;
    private SharedPreferences preferences;
    private String SAVED_PATH;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.editName);
        editText = findViewById(R.id.editText);
        path = preferences.getString(SAVED_PATH, "Empty");

        editName.setText(path);
        editText.setText(getText());
    }

    public void onSave(View view){
        String name = editName.getText().toString() + ".txt";
        String text = editText.getText().toString();

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(name, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVED_PATH, name);
        editor.apply();
    }

    public String getText(){
        try {
            FileInputStream fileInputStream = null;
            fileInputStream = openFileInput(path);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            return new String(bytes);
        } catch (IOException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}