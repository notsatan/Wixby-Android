package com.tinyideas.wixby;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

public class SignUp02 extends AppCompatActivity {

    // An integer with a random value. Will be used to request the user for permissions during runtime.
    private static final int PERMISSION_REQUEST_CODE = 15;
    private ImageView gpsIcon;

    // Creating these strings here as they are to be accessed from inner classes for reading and writing.
    // Thus, they either need to be global or final :(
    private static String firstName = "";
    private static String lastName = "";
    private static String password = "";
    private static String dateOfBirth = "";
    private static String gender = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting a return result for the activity by default. Thus, if the user presses the back button
        // they will go back to the previous activity and not directly thrown out of the app.
        setResult(Activity.RESULT_OK);

        // Getting the view stub and inflating it with the required layout.
        ViewStub viewStub = findViewById(R.id.mainViewStub);
        viewStub.setLayoutResource(R.layout.card_view02);
        viewStub.inflate();

        // Trying to get the data that should be sent to this activity from the previous activity.
        // If this data extraction process fails, the user will be informed of the same using a SnackBar
        // and then the app will be closed.
        try {
            Bundle passedData = getIntent().getExtras();
            ArrayList<String> resultArray = Objects.requireNonNull(passedData).getStringArrayList(getResources().getString(R.string.intentTag));

            // Getting the results one by one from the list. They are to be retrieved in the order
            // they were inserted in the list.
            assert resultArray != null;
            firstName = resultArray.get(0);
            lastName = resultArray.get(1);
            password = resultArray.get(2);
            dateOfBirth = resultArray.get(3);
            gender = resultArray.get(4);
        } catch (Exception e) {
            viewStub.setClickable(false);

            // Creating a SnackBar to inform the user of the crash. The SnackBar will also have a button
            // once the user clicks this button, the app will close.
            Snackbar.make(findViewById(R.id.activity_main_layout),
                    "Something went wrong. The app will have to shut down",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // In order to force close the application, using the `finish()` method
                            // to force close the current activity. Before doing that, using the
                            // `setResult()` method to set the result code as cancelled. Once this
                            // activity is closed, the previous activity will get the result in
                            // `startActivityForResult()`. On seeing this code, that activity too will
                            // force close itself. Thus, closing the entire application.
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }
                    }).show();
        }

        // Getting the country spinner from the XML
        final Spinner countrySpinner = findViewById(R.id.countrySpinner);

        // Getting the state spinner and the text view from the XML layout. Both of these are hidden
        // by default. Depending on the country selected by the user, one of the two will be displayed
        // and the other will remain hidden.
        final TextInputEditText stateTextView = findViewById(R.id.stateTextView);
        final Spinner stateSpinner = findViewById(R.id.stateSpinner);

        // Getting the container layout for the state textView and the state spinner. Instead of hiding
        // the individual, these container layouts will be hidden from the screen as required.
        final TextInputLayout stateTextParentView = findViewById(R.id.stateParentLayout);
        final RelativeLayout stateSpinnerLayout = findViewById(R.id.stateSpinnerLayout);

        final TextInputEditText locationField = findViewById(R.id.locationTextView);
        final TextInputEditText pinCode = findViewById(R.id.pinTextView);

        final CheckBox submitCheckBox = findViewById(R.id.conditionsCheckbox);

        final Button resetButton = findViewById(R.id.main_resetButton);
        final Button submitButton = findViewById(R.id.main_submitButton);

        // Changing the text of the submit button.
        submitButton.setText("Submit");

        // Getting the GPS icon, when the user clicks this icon, the position of the device will be
        // taken using GPS and will be filled into the location text field.
        gpsIcon = findViewById(R.id.gpsIcon);

        // Getting a list of all the country names and storing them in an ArrayList, this list will
        // then be used to populate the country spinner.
        String[] isoCountryCodes = Locale.getISOCountries();
        final ArrayList<String> countries = new ArrayList<>();

        for (String countryCode : isoCountryCodes) {
            Locale locale = new Locale("", countryCode);
            String countryName = locale.getDisplayName().trim();
            countries.add(countryName);
        }

        // In order to sort the entries of the ArrayList in alphabetical order, using a custom
        // comparator to do the same.
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        // Once the array list containing all countries is prepared, using the same to populate the
        // spinner too. To do so, creating an ArrayAdapter that will populate data from this list and
        // then attaching the adapter to the spinner. `spinner_item` is a simple XML file containing
        // a single text view. This TextView will be used by the adapter to create the list for the spinner
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, countries);
        countryAdapter.setDropDownViewResource(R.layout.spinner_item);
        countrySpinner.setAdapter(countryAdapter);

        // Similarly, populating the state list array too. Starting by creating an ArrayList containing
        // a list of all the states in India. This list will be populated using a string-array that is
        // present in `strings.xml`
        ArrayList<String> states = new ArrayList<>();
        Collections.addAll(states, getResources().getStringArray(R.array.statesList));

        // Using the same method as above to populate data from the list into the spinner.
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                states);
        stateAdapter.setDropDownViewResource(R.layout.spinner_item);
        stateSpinner.setAdapter(stateAdapter);

        // Attaching an item-selected listener to the country spinner. Thus, when the user changes their
        // country this listener will check the new country selected by the user. If the country is India,
        // then the state spinner will be displayed to the user, if not, then the textEdit will be displayed.
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG", countrySpinner.getSelectedItem().toString());
                String selection = countrySpinner.getSelectedItem().toString();

                // If `selection` is 'India', will display the state spinner as it is populated with
                // just Indian states. If `selection` is something else, will display the state text view.
                if (selection.equalsIgnoreCase("India")) {
                    // Hiding the state text view
                    stateTextParentView.setVisibility(View.GONE);

                    // Displaying the spinner
                    stateSpinnerLayout.setVisibility(View.VISIBLE);
                } else {
                    // Displaying the text view
                    stateTextParentView.setVisibility(View.VISIBLE);

                    // Hiding the spinner.
                    stateSpinnerLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // The flow-of-control will reach this part if nothing is selected by the user. This
                // will be something that's impossible to do, since the spinner will have a value selected
                // by default. However, in the off-hand chance that this changes in the future Android
                // versions, simply hiding the state spinner and text view from the display in here.
                stateSpinnerLayout.setVisibility(View.GONE);
                stateTextParentView.setVisibility(View.GONE);
            }
        });

        // Attaching a click listener to the GPS Icon ImageView. When the user clicks this icon, their
        // position is to be taken using the GPS and filled in the location text box.
        gpsIcon.setOnClickListener(new View.OnClickListener() {
            // Creating a location manager. In case the user wants to display their location using the GPS,
            // this manager will be used to do the same.
            private LocationManager locationManager;
            private boolean isReceivingUpdates = false;
            private CustomLocationListener customLocationListener;

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                // When the user clicks on the auto-locate button, the app will fetch the location of
                // the device using the `LocationManager`. In order to use this service, the app needs
                // to have the GPS permission granted. If the app tries to use this service without \
                // the permission, it could lead to a crash. Thus, beginning with checking if the
                // permissions are granted or not.
                if (checkForPermissions()) {
                    // If the app already has the required permissions, then directly trying to get
                    // the users location using the LocationManager.
                    if (locationManager == null || !isReceivingUpdates) {
                        // Creating an instance of the location manager service and setting it up to listen
                        // for updates in the location of the device after a fixed time or distance interval.
                        customLocationListener = new CustomLocationListener(locationField);
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                2_000, 2, customLocationListener);

                        // Now that the location manager is receiving updates, changing the image for the
                        // GPS icon and updating the boolean too.
                        gpsIcon.setImageDrawable(getDrawable(R.drawable.icon_gps_active));
                        isReceivingUpdates = true;
                    } else {
                        // If the app was already listening for updates, then in here, disabling the
                        // updates. And resetting the boolean and changing the image for the GPS icon.
                        locationManager.removeUpdates(customLocationListener);

                        isReceivingUpdates = false;
                        gpsIcon.setImageDrawable(getDrawable(R.drawable.icon_gps));
                    }
                } else {
                    // If the permissions have not been granted, requesting the permissions at runtime.
                    requestPermissions();
                }
            }
        });

        // Attaching a click event listener to the reset button.
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the user presses the reset button, simply removing the text from the text edits
                // un-selecting the checkbox, and selecting the first item in both the spinners.
                locationField.setText("");
                pinCode.setText("");
                countrySpinner.setSelection(0);
                stateSpinner.setSelection(0);
                stateTextView.setText("");
                submitCheckBox.setChecked(false);
            }
        });

        // Attaching a click event listener to the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inside here, checking if the user has filled all the fields that are required, if
                // not, then displaying an error message to the user.
                String location = Objects.requireNonNull(locationField.getText()).toString().trim();
                String pin = Objects.requireNonNull(pinCode.getText()).toString().trim();
                String country = countrySpinner.getSelectedItem().toString().trim();

                // Creating a blank string for the state now. Will populate it later.
                String state = "";

                // Checking if any of these values are null. Not checking for `country` as one item
                // from the spinner will always be selected.
                if (location.length() == 0 || pin.length() == 0) {
                    Snackbar.make(findViewById(R.id.activity_main_layout),
                            "Please fill all the required details.", Snackbar.LENGTH_LONG).show();

                    // Returning the flow of control from here.
                    return;
                }

                // Just like how an element of the country spinner is always selected, similarly an
                // element from the state spinner will always be selected. Thus, instead of directly
                // taking its value as the result, first checking if the country selected is India.
                if (country.equalsIgnoreCase("India")) {
                    // If the country is India, then the user will chose their state from the state
                    // spinner. Thus, getting the value from spinner.
                    state = stateSpinner.getSelectedItem().toString().trim();
                } else {
                    // If the country selected by the user is not India, then they will have to enter
                    // their state manually. Thus, taking data from the text edit and checking if this
                    // is valid data or not.
                    state = Objects.requireNonNull(stateTextView.getText()).toString().trim();

                    // Creating a temporary variable just for fun :p
                    String tempState = state.toLowerCase();

                    // Checking if the user entered some data or not
                    if (state.length() == 0) {
                        Snackbar.make(findViewById(R.id.activity_main_layout),
                                "Please enter the state where you reside", Snackbar.LENGTH_LONG).show();

                        // Returning the flow of control
                        return;
                    } else if (tempState.equals("solid") || tempState.equals("liquid") || tempState.equals("gas")) {
                        Snackbar.make(findViewById(R.id.activity_main_layout),
                                "Are you that eager to die\t\t-___-", Snackbar.LENGTH_LONG).show();

                        // Returning the flow of control.
                        return;
                    } else if (tempState.equals("plasma") || tempState.equals("bec")) {
                        Snackbar.make(findViewById(R.id.activity_main_layout),
                                "You sir, earn a pass for being literate ;)", Snackbar.LENGTH_LONG).show();

                        // Not returning the flow of control ;)
                    }
                }

                // Checking if the checkbox is clicked or not.
                if (!submitCheckBox.isChecked()) {
                    // If the checkbox is not checked (oh the irony), then displaying an error message to the user
                    Snackbar.make(findViewById(R.id.activity_main_layout),
                            "Please agree to the Terms And Conditions to proceed", Snackbar.LENGTH_LONG).show();

                    // Returning the flow of control back.
                    return;
                }

                // If the flow-of-control reaches this point, then the user has entered all the data
                // in the required format. So, creating an instance of the database helper class to
                // insert data into the database.
                DatabaseHelper databaseHelper = new DatabaseHelper(SignUp02.this);
                databaseHelper.registerUser(firstName, lastName, password, location, state,
                        country, pin, dateOfBirth, gender);
            }
        });
    }

    /**
     * This method will be used to check for the required permissions during runtime. In the older
     * versions of Android, users had to grant all the permission before starting the app. However,
     * as of now, the apps have to request users for permissions during runtime. Thus, this method will
     * be used to check if the app has the required permissions or not. If the app tries to the GPS
     * without the appropriate permissions, then the app will crash.
     *
     * @return A boolean indicating whether the app has the required permissions or not. Will be true
     * if the permissions are granted, false otherwise.
     */
    private boolean checkForPermissions() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * If the user has not granted the required permissions to the app. Then during runtime, the app
     * will request the user to granted the required permissions to the app. This method will be used
     * to request the user for the permissions.
     */
    private void requestPermissions() {

        // Forming a string array of the permissions that are required from the user.
        String[] permissionsRequired = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};

        // Directly requesting the user for the permissions, using the array created above.
        requestPermissions(permissionsRequired, PERMISSION_REQUEST_CODE);
    }

    /**
     * This method is valid only Android 6 and above. Whenever the application requests the user for
     * permission during runtime, the result of the permission will be returned to the application
     * inside this method. The result can either be ok, if the user granted the permission that was
     * required. Or otherwise, in case the user denied the permission.
     *
     * @param requestCode  An integer. Will be the request code that was used while requesting the permission.
     * @param permissions  A string array containing the permissions that were requested.
     * @param grantResults An integer array. Will contain the same number of entries as `permissions`.
     *                     For each permission in `permission` the corresponding index of this array
     *                     will be the result of that permission, i.e. if the user denied that particular
     *                     permission or accepted it.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Checking to make sure that the permissions being granted are the ones that the app requested
        // for during runtime.
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // If the flow-of-control reaches this part, it means that the method is returning the
            // result for the GPS location permission. Checking if the user has granted the
            // permission or denied it. Since the request was made for two permissions, thus,
            // `grantResults` will have two entries, each indicating whether the corresponding
            // permission was granted by the user or not.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted. The user was asked for the permissions when they clicked
                // on the GPS icon. Thus, now that the permission has been granted, implicitly
                // performing a click on the same icon.
                gpsIcon.performClick();
            } else {
                // Permissions denied.
                Snackbar.make(findViewById(R.id.activity_main_layout),
                        "Y u do this to me?\n\t\t\t\tI cri\t\t ಥ_ಥ", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}

class CustomLocationListener implements LocationListener {

    private TextInputEditText locationUpdateFeed;

    /**
     * The class constructor.
     *
     * @param locationUpdateFeed The text field in which the location needs to be written.
     */
    public CustomLocationListener(TextInputEditText locationUpdateFeed) {
        this.locationUpdateFeed = locationUpdateFeed;
    }

    /**
     * This method will be automatically executed whenever the location of the device changes. The
     * location object received will contain the coordinates for the new location of the device.
     *
     * @param location A location object. Among other details, will also contain the latitude and
     *                 longitude for the new location of the device.
     */
    @Override
    public void onLocationChanged(Location location) {
        locationUpdateFeed.setText(String.format("(%f, %f)", location.getLatitude(), location.getLongitude()));
    }

    /**
     * Deprecated in API 29. Should not be used. Never invoked since API 29 onwards.
     *
     * @param provider String containing the name of the provider.
     * @param status   An integer containing the status code.
     * @param extras   A bundle.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        /* Deprecated method. Should not be used */
    }

    /**
     * Default method, executed when the provider is enabled by the user.
     *
     * @param provider A string containing the name of the provider that is enabled.
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Default method, called when the user disables the provider.
     *
     * @param provider A string containing the name of the provider that was disabled by the user.
     */
    @Override
    public void onProviderDisabled(String provider) {

    }
}
