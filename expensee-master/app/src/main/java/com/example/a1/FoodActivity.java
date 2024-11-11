package com.example.a1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;

public class FoodActivity extends AppCompatActivity {

    private LinearLayout expensesListLayout;
    private static final int ADD_EXPENSE_REQUEST_CODE = 1;
    private static final int EDIT_EXPENSE_REQUEST_CODE = 2;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Reference to the expenses list layout
        expensesListLayout = findViewById(R.id.expensesListLayout);
        expensesListLayout.removeAllViews();

        // Load and display expenses from the database
        loadExpenses();

        // Set up the Add Expense button
        Button addExpenseButton = findViewById(R.id.addExpenseButton);
        addExpenseButton.setOnClickListener(v -> {
            Intent intent = new Intent(FoodActivity.this, AddExpenseActivity.class);
            startActivityForResult(intent, ADD_EXPENSE_REQUEST_CODE);
        });

        // Set up the Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(FoodActivity.this, CategoriesActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to load expenses from the database and add them to the layout
    private void loadExpenses() {
        Cursor cursor = dbHelper.getReadableDatabase().query("expenses", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int titleIndex = cursor.getColumnIndex("title");
                int amountIndex = cursor.getColumnIndex("amount");
                int dateIndex = cursor.getColumnIndex("date");

                // Handle possible -1 return from getColumnIndex
                if (titleIndex != -1 && amountIndex != -1 && dateIndex != -1) {
                    String title = cursor.getString(titleIndex);
                    double amount = cursor.getDouble(amountIndex);
                    String date = cursor.getString(dateIndex);

                    addExpenseItem(title, String.format(Locale.US, "-$%.2f", amount), date, R.drawable.food);
                } else {
                    // Handle the error if any column index is -1
                    throw new IllegalStateException("One of the columns (title, amount, date) does not exist in the cursor.");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }


    // Method to handle the result from AddExpenseActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String expenseTitle = data.getStringExtra("expense_title");
            String expenseAmount = data.getStringExtra("expense_amount");
            String expenseDate = data.getStringExtra("expense_date");

            if (requestCode == ADD_EXPENSE_REQUEST_CODE) {
                // Add the new expense item to the list
                addExpenseItem(expenseTitle, expenseAmount, expenseDate, R.drawable.food);
            }
        }
    }

    // Method to dynamically add an expense item to the layout
    private void addExpenseItem(String title, String amount, String date, int imageResId) {
        LayoutInflater inflater = LayoutInflater.from(this);

        // Inflate the expense item layout
        View expenseItemView = inflater.inflate(R.layout.expense_item, expensesListLayout, false);

        // Set the data for the expense item
        TextView expenseTitleTextView = expenseItemView.findViewById(R.id.expense_title);
        TextView expenseAmountTextView = expenseItemView.findViewById(R.id.expense_amount);
        TextView expenseDateTextView = expenseItemView.findViewById(R.id.expense_date);
        ImageView expenseImageView = expenseItemView.findViewById(R.id.expense_image);

        expenseTitleTextView.setText(title);
        expenseAmountTextView.setText(amount);
        expenseDateTextView.setText(date);
        expenseImageView.setImageResource(imageResId);

        // Add the expense item to the expenses list layout
        expensesListLayout.addView(expenseItemView);

        // Set up the click event for the expense item to allow editing or deleting
        expenseItemView.setOnClickListener(v -> showEditDeleteDialog(expenseItemView, title, amount, date, imageResId, expensesListLayout.indexOfChild(expenseItemView)));
    }

    // Method to show a dialog for editing or deleting an expense item
    private void showEditDeleteDialog(View expenseItemView, String title, String amount, String date, int imageResId, int position) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(FoodActivity.this);
        builder.setTitle("Chỉnh sửa hoặc Xóa");

        builder.setItems(new CharSequence[]{"Chỉnh sửa", "Xóa"}, (dialog, which) -> {
            switch (which) {
                case 0: // Edit
                    Intent editIntent = new Intent(FoodActivity.this, AddExpenseActivity.class);
                    editIntent.putExtra("expense_title", title);
                    editIntent.putExtra("expense_amount", amount);
                    editIntent.putExtra("expense_date", date);
                    editIntent.putExtra("expense_image_res_id", imageResId);
                    editIntent.putExtra("position", position); // Pass the position for editing
                    startActivityForResult(editIntent, EDIT_EXPENSE_REQUEST_CODE);
                    break;
                case 1: // Delete
                    expensesListLayout.removeView(expenseItemView);
                    // Optionally: Remove the item from the database
                    break;
            }
        });

        builder.show();
    }
}
