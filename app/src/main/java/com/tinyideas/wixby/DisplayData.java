package com.tinyideas.wixby;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        // This window will be displayed to the user as a popup window. Thus, the dimensions of the
        // window will be shrunken here.
        DisplayMetrics displayMetrics = new DisplayMetrics();

        // Populating this variable with the display metrics of the screen window.
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // This object now has the dimensions of the current window in the form of pixels. Thus, using
        // these pixels to calculate the dimensions for the new window.
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // Using this data, resizing the current window to have dimensions that are a fraction of
        // the original dimensions.
        getWindow().setLayout((int) (width * 0.82), (int) (height * 0.68));

        // Creating an instance of the `DataBaseHelper` this will be used to extract the last row
        // from the database.
        DatabaseHelper databaseHelper = new DatabaseHelper(DisplayData.this);

        // Getting the last row of the SQL Database.
        final Data userData = databaseHelper.getLastRegisteredUser();

        // Once the popup window is done, getting started with setting up the UI elements.
        final TextView userCounter = findViewById(R.id.displayUserCounter);
        final TextView name = findViewById(R.id.displayName);
        final TextView password = findViewById(R.id.displayPassordField);
        final TextView dateOfBirth = findViewById(R.id.displayDob);
        final TextView gender = findViewById(R.id.displayGender);
        final TextView location = findViewById(R.id.displayLocation);
        final TextView pin = findViewById(R.id.displayPin);
        final TextView country = findViewById(R.id.displayCountry);

        // Also, getting the ImageView that will be used to show or hide the password.
        final ImageView showPassword = findViewById(R.id.displayShowPassword);

        final ImageView displayImage = findViewById(R.id.displayImage);

        // Populating the text view with data from the database.
        userCounter.setText(String.format("User #%s", userData.getUserIndex()));
        name.setText(String.format("Name:  %s %s", userData.getFirstName(), userData.getLastName()));
        password.setText(String.format("Password:  %s", userData.getPassword()));
        dateOfBirth.setText(String.format("Date Of Birth:  %s", userData.getDateOfBirth()));
        gender.setText(String.format("Gender:  %s", userData.getGender()));
        location.setText(String.format("Location:  %s", userData.getLocation()));
        pin.setText(String.format("PIN Code: %s", userData.getPin()));
        country.setText(String.format("Miscellaneous:  %s, %s", userData.getState(), userData.getCountry()));
        displayImage.setImageBitmap(userData.getImage());

        // Attaching an on-click listener to the show password image view.
        showPassword.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordDisplayed = true;

            @Override
            public void onClick(View v) {
                // Getting the password that is already being displayed. If this password is in hidden
                // then displaying the same, else, hiding the password.
                if (isPasswordDisplayed) {
                    // Creating a string made of stars that is as long as the password.
                    StringBuilder tempPassword = new StringBuilder();
                    for (int i = 0; i < userData.getPassword().length(); i++)
                        tempPassword.append("*");

                    // Once the star-marked string is generated, setting this as the text for the view.
                    // And changing the value of the parent boolean.
                    password.setText(String.format("Password:  %s", tempPassword));
                    isPasswordDisplayed = false;
                } else {
                    // If the password isn't already being displayed, then displaying the password and
                    // changing the value of the boolean.

                    password.setText(String.format("Password: %s", userData.getPassword()));
                    isPasswordDisplayed = true;
                }
            }
        });

        // Since the password is being displayed to the user by default, thus performing a click
        // on the show password button to hide it in the first run :)
        showPassword.performClick();
    }
}
