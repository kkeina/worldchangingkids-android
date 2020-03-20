package com.world_changingkids;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * An simple AppCompatActivity to display how to use app page
 */
public class HowToUseAppActivity extends AppCompatActivity {
    /**
     * A button for user to go to  previous page
     */
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use_app);
        btnBack = (Button) findViewById(R.id.how_to_use_app_button_back_to_home);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
