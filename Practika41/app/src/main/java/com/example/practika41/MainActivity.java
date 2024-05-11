package com.example.practika41;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_first;
    private Button btn_Second;
    private Button btn_Third;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_first = findViewById(R.id.btn_first);
        btn_Second = findViewById(R.id.btn_second);
        btn_Third = findViewById(R.id.btn_third);

        showFragment1(getSupportFragmentManager());

        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment1(getSupportFragmentManager());
            }
        });

        btn_Second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment2(getSupportFragmentManager());
            }
        });

        btn_Third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment3(getSupportFragmentManager());
            }
        });
    }
    @Override
    public void onClick(View v){

    }
    public static void showFragment1(FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BlankFragment fragment1 = new BlankFragment();
        fragmentTransaction.replace(R.id.fragment, fragment1);
        fragmentTransaction.commit();
    }


    public static void showFragment2(FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BlankFragment2 fragment_blank2 = new BlankFragment2();
        fragmentTransaction.replace(R.id.fragment, fragment_blank2);
        fragmentTransaction.commit();
    }

    public static void showFragment3(FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BlankFragment3 fragment_blank3 = new BlankFragment3();
        fragmentTransaction.replace(R.id.fragment, fragment_blank3);
        fragmentTransaction.commit();
    }

}
