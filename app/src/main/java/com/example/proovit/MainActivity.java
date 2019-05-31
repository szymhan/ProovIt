package com.example.proovit;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.proovit.fragments.ReportedFragment;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private final String REPORT = "Report";
    private final String REPORTED = "Reported";
    private final String RANKING = "Ranking";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_report:
                    initFragment(REPORT);
                    return true;
                case R.id.navigation_reported:
                    initFragment(REPORTED);
                    return true;
                case R.id.navigation_ranking:
                    initFragment(RANKING);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void initFragment(String ne) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (ne){
            case REPORT:
               // ft.replace(R.id.container,new AlarMeFragment());
                break;
            case REPORTED:
                ft.replace(R.id.container, new ReportedFragment());
                break;
            case RANKING:
              //  ft.replace(R.id.container, new HistoryFragment());
                break;
        }

        ft.commit();
    }



}
