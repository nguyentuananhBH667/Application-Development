package com.example.a1;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handler to transition to LoginActivity after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginForm.class);
                startActivity(intent);
                // Finish MainActivity so that it is removed from the back stack
                finish();
            }
        }, 5000); // 5000 milliseconds = 5 seconds
    }
}
