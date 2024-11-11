package com.example.a1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etExpenseTitle, etAmount, etMessage;
    private Spinner spinnerCategory;
    private Button btnSave;
    private TextView tvDate;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        etExpenseTitle = findViewById(R.id.et_expense_title);
        etAmount = findViewById(R.id.et_amount);
        etMessage = findViewById(R.id.et_message);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnSave = findViewById(R.id.btn_save);
        tvDate = findViewById(R.id.tv_date);
        ImageView ivDatePicker = findViewById(R.id.iv_date_picker);
        ImageView btnBack = findViewById(R.id.btn_back);

        calendar = Calendar.getInstance();

        // Lấy dữ liệu chỉnh sửa nếu có
        Intent intent = getIntent();
        boolean isEditing = intent.hasExtra("position");
        if (isEditing) {
            String expenseTitle = intent.getStringExtra("expense_title");
            String expenseAmount = intent.getStringExtra("expense_amount");
            String expenseDate = intent.getStringExtra("expense_date");
            int expenseImageResId = intent.getIntExtra("expense_image_res_id", R.drawable.food);
            int position = intent.getIntExtra("position", -1);

            // Cập nhật giao diện với dữ liệu hiện tại
            etExpenseTitle.setText(expenseTitle);
            etAmount.setText(expenseAmount);
            tvDate.setText(expenseDate);

            // Đặt ngày cho Calendar
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
                calendar.setTime(sdf.parse(expenseDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Thiết lập bộ chọn ngày
        ivDatePicker.setOnClickListener(v -> {
            new DatePickerDialog(AddExpenseActivity.this, (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Thiết lập sự kiện cho nút lưu
        btnSave.setOnClickListener(v -> {
            String expenseTitle = etExpenseTitle.getText().toString();
            double expenseAmount = Double.parseDouble(etAmount.getText().toString());
            String expenseDate = tvDate.getText().toString();

            // Insert the data into the database
            dbHelper.addExpense(expenseDate, expenseAmount, expenseTitle, etMessage.getText().toString());

            // Return to the FoodActivity with the data
            Intent resultIntent = new Intent();
            resultIntent.putExtra("expense_title", expenseTitle);
            resultIntent.putExtra("expense_amount", String.format(Locale.US, "%.2f", expenseAmount));
            resultIntent.putExtra("expense_date", expenseDate);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Thiết lập sự kiện cho nút quay lại
        btnBack.setOnClickListener(v -> finish());
    }


    private void updateDateLabel() {
        String myFormat = "MMMM dd, yyyy"; // Định dạng ngày
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDate.setText(sdf.format(calendar.getTime()));
    }
}