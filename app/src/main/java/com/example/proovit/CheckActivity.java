package com.example.proovit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proovit.service.CheckService;


public class CheckActivity extends AppCompatActivity {


    private final String ADDRESS = "ADDRESS";
    TextView address;
    SharedPreferences preferences;
    TextView checkReason;
    ImageView ok, nook;

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
        if(receivedIntent.getExtras().get("url")!=null) {
            Log.e("!~!", "onCreate: !!!!!!!!!!" );
            Log.e("testtse", "onCreate: " + receivedIntent.getExtras().get("url") );
            address.setText((CharSequence) receivedIntent.getExtras().get("url"));
        }

        checkReason.setText("To fake!");

    }

    private void setAddress(String text) {
        address.setText(text);
        address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setAssets() {
        address = findViewById(R.id.check_address);
        checkReason = findViewById(R.id.check_reason_text);
        ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Zaplusowano wpis", Toast.LENGTH_LONG).show();
                onBackPressed();

            }
        });
        nook = findViewById(R.id.nook);
        nook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Zaminusowano wpis", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
    }

}
