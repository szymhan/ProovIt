package com.example.proovit;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.proovit.service.CheckService;


public class CheckActivity extends AppCompatActivity {


    private final String ADDRESS = "ADDRESS";
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        setAssets();
        Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();
        String receivedType = receivedIntent.getType();

        if (receivedAction != null) {
            if  (receivedAction.equals(Intent.ACTION_SEND) && receivedType.startsWith("text/")) {

                String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                setAddress(receivedText);
                Intent i = new Intent(this, CheckService.class);
                i.putExtra("ADDRESS", receivedText);
                startService(i);
                moveTaskToBack(true);
                finish();

            } else if (receivedAction.equals(Intent.ACTION_MAIN)) {
                setAddress(receivedIntent.getStringExtra(ADDRESS));

            }
        } else {
            setAddress(receivedIntent.getStringExtra(ADDRESS));
        }

    }

    private void setAddress(String text) {
        address.setText(text);
        address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setAssets() {
        address = findViewById(R.id.check_address);
    }

}
