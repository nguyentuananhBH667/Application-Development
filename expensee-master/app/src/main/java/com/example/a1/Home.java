package com.example.a1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories); // Đảm bảo bạn có layout này
    }

    public void openProfileActivity(View view) {
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("USER_EMAIL",userEmail);
        startActivity(intent);
    }

    public void openFoodActivity(View view) {
        Intent intent = new Intent(this, FoodActivity.class);
        startActivity(intent);
    }
    public void openRentActivity(View view) {
        Intent intent = new Intent(this, RentActivity.class);
        startActivity(intent);
    }

    public void openMedicineActivity(View view) {
        Intent intent = new Intent(this, MedicineActivity.class);
        startActivity(intent);
    }
    public void openTransportActivity(View view) {
        Intent intent = new Intent(this, TransportActivity.class);
        startActivity(intent);
    }

}



