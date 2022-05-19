package ru.mirea.yasko.clickbuttons;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = (TextView) findViewById(R.id.tvOut);
        Button buttonOk = (Button) findViewById(R.id.btnOk);
        Button buttonCancel = (Button) findViewById(R.id.btnCancel);

        @SuppressLint("SetTextI18n") View.OnClickListener onClickBtnOk = view -> tvOut.setText("Нажата кнопка 'Ok'");
        @SuppressLint("SetTextI18n") View.OnClickListener onClickBtnCancel = view -> tvOut.setText("Нажата кнопка 'Cancel'");

        buttonOk.setOnClickListener(onClickBtnOk);
        buttonCancel.setOnClickListener(onClickBtnCancel);

    }
}