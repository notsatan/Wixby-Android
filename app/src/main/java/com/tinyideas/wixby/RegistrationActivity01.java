package com.tinyideas.wixby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class RegistrationActivity01 extends AppCompatActivity {

    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText place;
    private TextInputEditText dateOfBirth;

    /**
     * The point of entry for the program. Will be used to inflate the layout and assign attributes
     * to the widgets.
     *
     * @param savedInstanceState Usually null by default. If the app saves some data to the bundle
     *                           during onPause(), that data will be provided to the activity while recreating it over here.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activty_01);

        // Fetching components from the main screen and initialing the global variables from here.
        firstName = findViewById(R.id.activity01_firstName);
        lastName = findViewById(R.id.activity01_lastName);
        place = findViewById(R.id.activity01_location);
        dateOfBirth = findViewById(R.id.activity01_calendarInput);

        Button resetButton = findViewById(R.id.activity01_resetButton);
        final RadioGroup radioGroup = findViewById(R.id.activity01_radioGroup);

        // Attaching a click-event listener to `dateOfBirth` text field. Once the user clicks this
        // field, will display the calendar as a popup to the user.
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            /**
             * This method will be called when the user clicks in the widget. However, this method won't
             * be executed if the user changed their focus with the click. That is, if the user was focused
             * on some other widget and then clicked on this widget, this method won't be executed, instead
             * of this method `onFocusChange()` will be executed. This method will be executed when the user
             * has focus inside the widget and then taps inside the same widget again.
             *
             * @param view The view that the user tapped upon. In this case, it'll be this text edit widget.
             */
            @Override
            public void onClick(View view) {
                // Creating an object of the class that will display the popup and handle all the tasks
                // related to the popup
                final CustomDialog customDialog = new CustomDialog(RegistrationActivity01.this);
                customDialog.show();

                // Setting a dismiss listener to the dialog.
                customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        applyDate(customDialog.getDateSelected());
                    }
                });
            }
        });

        // Attaching behaviour to the reset button. Will manually clear all the text views and the
        // selected radio buttons.
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setText("");
                lastName.setText("");
                place.setText("");
                dateOfBirth.setText("");
                radioGroup.clearCheck();
            }
        });
    }

    /**
     * This method will be used to write data into the date field. The field by default isn't focusable
     * by the user, thus the user won't be able to make any changes into this field. When the user clicks
     * on the text input field, they will be displayed with the popup asking them to select the date.
     * Once this date is selected, the popup will close and call this function. This function will then
     * write the date selected by the user into the edit text that was selected by the user.
     *
     * @param selectedDate An instance of `Date` that will contain the date that is selected by the user.
     *                     Will be used to read the stored information from and then write date.
     */
    void applyDate(Date selectedDate) {
        // Creating a string that will be used to store the text that is to be written to the text field.
        String date = "";

        // The date selected will be displayed in the form of: 14 September, 2019 (example).
        date += selectedDate.getDate() + " ";

        // Since the month selected is 0-indexed, the values will lie between [0, 11]
        // Using switch-case to get the strings instead of importing another stupid library and
        // increasing the load :)
        switch (selectedDate.getMonthOfYear()) {
            case 0:
                date += "January, ";
                break;

            case 1:
                date += "February, ";
                break;

            case 2:
                date += "March, ";
                break;

            case 3:
                date += "April, ";
                break;

            case 4:
                date += "May, ";
                break;

            case 5:
                date += "June, ";
                break;

            case 6:
                date += "July, ";
                break;

            case 7:
                date += "August, ";
                break;

            case 8:
                date += "September, ";
                break;

            case 9:
                date += "October, ";
                break;

            case 10:
                date += "November, ";
                break;

            case 11:
                date += "December, ";
                break;
        }

        // Adding the year to the selected date string.
        date += selectedDate.getYear();

        // Now, using this finalized date string setting it as the main text of the edit text box.
        dateOfBirth.setText(date);
    }
}

class CustomDialog extends Dialog {
    private Date dateSelected;

    public CustomDialog(@NonNull Context context) {
        // Evoking the parent constructor. Auto-generated code stub.
        super(context);
    }

    /**
     * The main method that will be evoked to draw the dialog popup window on the screen. Will be used
     * customize the elements of the popup window and initialize the elements of the window.
     *
     * @param savedInstanceState Usually null by default. If the app saves some data to the bundle
     *                           during onPause(), that data will be provided to the activity while recreating it over here. The
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the popup dialog to not have any title bar for the window.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Inflating the XML file that will draw the XML view on the screen.
        setContentView(R.layout.calendar_popup);

        // Setting the background color of the dialog to be transparent. Without this, the edges of
        // the popup will be rough.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Getting the date-picker and attaching a change listener to the date-picker dialog.
        DatePicker datePicker = findViewById(R.id.calendarMain);

        // Fetching the submit button from the XML layout and setting a click listener to the same.
        // Clicking this button will close the popup window.
        Button submitButton = findViewById(R.id.popupSubmitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });

        // Setting the initial value for `dateSelected`. This will be used if the user selects the first
        // day visible as the value. Without using this initialization, the app will crash :(
        dateSelected = new Date(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                /**
                 * This method is the new version of the date changed listener, this method will be
                 * executed whenever the user selects a date in the date picker. The input parameters
                 * will contain details about the date that the user selects.
                 *
                 * @param view        The view that is being changed. For this method, this will be the id of the calendar.
                 * @param year        An integer representing the year that the user selects.
                 * @param monthOfYear An integer representing the month that the user has selects.
                 * @param dayOfMonth  An integer representing the date that the user has selects.
                 */
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // Storing the date selected by the user in the global variable
                    dateSelected = new Date(dayOfMonth, monthOfYear, year);
                }
            });
        } else {
            // Getting the initial values for the date that should be selected by default using `datePicker`
            // and attaching a date change listener to the pre-API 26 devices. Yes, this is one of the few
            // working methods for the old devices, so out of options.
            datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                    new DatePicker.OnDateChangedListener() {
                        /**
                         * This method is the older version of the date change listener that was used
                         * on pre-API 26 devices. The inner implementation of this method will be the
                         * exact same as the method for the newer version. The only difference will
                         * be the way in these listeners are set-up.
                         *
                         * @param view        The view that is currently focused upon by the user. For this method, will be the idea of the date picker.
                         * @param year        An integer representing the year that the user selects.
                         * @param monthOfYear An integer representing the month that the user selects.
                         * @param dayOfMonth  An integer representing the date that the user selects.
                         */
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // Storing the date selected by the user in the global variable
                            dateSelected = new Date(dayOfMonth, monthOfYear, year);
                        }
                    });
        }
    }

    /**
     * A simple getter that will be used to retrieve the date selected by the user
     *
     * @return An instance of `Date` object containing the date that is selected by the user.
     */
    public Date getDateSelected() {
        return dateSelected;
    }
}
