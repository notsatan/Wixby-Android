package com.tinyideas.wixby;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Users.db";

    public static final String TABLE_NAME = "IndexTable";
    public static final String IMAGE_TABLE = "ImageTable";

    public static final String COLUMN_INDEX = "AutoIndex";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_PLACE = "Location";
    public static final String COLUMN_STATE = "State";
    public static final String COLUMN_COUNTRY = "Country";
    public static final String COLUMN_PIN = "PinCode";
    public static final String COLUMN_DOB = "DateOfBirth";
    public static final String COLUMN_GENDER = "Gender";
    public static final String COLUMN_IMAGE = "Image";

    /**
     * The constructor of the class. Will be used to fetch the context from the calling activity into
     * the object of this class. Will then internally initialize the database table using the parent
     * constructor and supplying the relevant information required to the parent constructor. Inside
     * the constructor, the name of the database that will be used by the application is passed to parent.
     * If a database with the same name doesn't already exist, then the `onCreate()` method will be
     * executed which will then create the database. If a database of the same name does exist, then
     * the `onCreate()` method will be skipped.
     *
     * @param context The context from the calling activity. Will be required to load attributes using
     *                the database.
     */
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Will be the main point of entry for the object. Once the class constructor is finished, this
     * method will be executed. However, unlike the normal `onCreate()` methods, this method will be
     * executed only if the database doesn't exist already. If the database does exist, then this
     * method will be skipped. Thus, the main part of creating the database if it doesn't already
     * exist will be handled by this method.
     *
     * @param sqLiteDatabase An instance of the main database object. Will be used to create the table
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // The only scenario when the flow-of-control reaches here will be when the database hasn't
        // been created before. So, creating the database in here using SQL

        // Creating a string that will be used to generate the database command. This string will then
        // be fed to `sqLiteDatabase` to create a table as required.
        String sqlQuery = String.format(
                "CREATE TABLE %s (%s integer PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                TABLE_NAME, COLUMN_INDEX, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_PASSWORD,
                COLUMN_DOB, COLUMN_GENDER, COLUMN_PLACE, COLUMN_PIN, COLUMN_STATE, COLUMN_COUNTRY
        );

        // Creating another table that will be used specifically to store images in the database
        String imageQuery = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s BLOB);", IMAGE_TABLE, COLUMN_INDEX, COLUMN_IMAGE);

        // Running the SQL Query(s) created above.
        sqLiteDatabase.execSQL(sqlQuery);
        sqLiteDatabase.execSQL(imageQuery);
    }

    /**
     * This method is executed whenever the database is to be upgraded, i.e. this method is executed
     * whenever the state or schema of the database is modified. Thus, if the entire structure of the
     * database is to be changed, new tables are to be added, the previous ones are to be removed, etc,
     * this method will be executed.
     * <p>
     * This method is a single transaction method, i.e. while processing any query in this method, if the
     * operation fails, the entire database will be reverted back to its previous state.
     *
     * @param sqLiteDatabase The SQLiteDatabase object on which the commands are to be executed.
     * @param oldVersion     An integer containing the old version of the database that is to be outdated
     * @param newVersion     An integer containing the new version of the database that will be used to
     *                       replace the old one.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Normally, the program isn't supposed to reach this part of the code. However, in some
        // scenario if the flow-of-control does reach this, deleting the existing table and creating a new one.
        String sqlQuery = String.format("Drop table if exists %s", TABLE_NAME);

        // Executing the above query.
        sqLiteDatabase.execSQL(sqlQuery);

        // Once the table has been dropped, forcefully recreating the same table by directly executing
        // the `onCreate()`.
        onCreate(sqLiteDatabase);
    }

    /**
     * This method will be used to add data to the SQLite database. This method will take the required
     * data as its arguments and then add a row in the table with the data for that row being the
     * arguments for the method.
     *
     * @param firstName A string containing the first name of the user.
     * @param lastName  A string containing the last name of the user.
     * @param password  A string containing the password of the user
     * @param place     A string containing the location of the user.
     * @param state     A string containing the state of the user.
     * @param country   A string containing the country of the user
     * @param pin       A string containing the PIN Code entered.
     * @param dob       A string containing the date of birth.
     * @param gender    A string containing the gender.
     * @param image     A bitmap containing the image that the user wants to add to the database.
     * @return If the insertion is successful, the row ID of the newly inserted row will be returned
     * and -1 will be returned in case of error.
     */
    public long registerUser(String firstName, String lastName, String password, String place,
                             String state, String country, String pin, String dob, String gender, Bitmap image) {
        // Starting by getting an instance of the database that will be used to write the values into.
        SQLiteDatabase database = this.getWritableDatabase();

        // Creating a new instance of `ContentValues`, think of this as an object to which all the
        // data that should be added to a single row of the database will be added and then this object
        // will be added to the database.
        ContentValues contentValues = new ContentValues();

        // The values are to be added to this object in the form of key-value pair. With `key` being
        // the name of the column to which this data is to be added and `value` being the data to be added.
        contentValues.put(COLUMN_FIRST_NAME, firstName);
        contentValues.put(COLUMN_LAST_NAME, lastName);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_DOB, dob);
        contentValues.put(COLUMN_GENDER, gender);
        contentValues.put(COLUMN_PLACE, place);
        contentValues.put(COLUMN_PIN, pin);
        contentValues.put(COLUMN_STATE, state);
        contentValues.put(COLUMN_COUNTRY, country);

        // Once all the values that are to be added to a row of the table have been placed inside this
        // object, adding this object to the table. If the insertion is successful, `result` will be
        // the row ID of the newly inserted row (-1 in case of error).
        long result = database.insert(TABLE_NAME, null, contentValues);

        // Overwriting the same `contentValues` now to insert image into the database.
        contentValues = new ContentValues();

        // Using `ImageHandler` class to convert the supplied image into an array of bytes. And
        // adding the same to the ImageTable of the database.
        contentValues.put(COLUMN_IMAGE, ImageHandler.getBytes(image));

        long resultNew = database.insert(IMAGE_TABLE, null, contentValues);

        /*
         * nullColumnHack -->> Normally, a completely empty (null) row cannot be added to SQLite
         * database without explicitly indicating name of the column to which the null entry should be
         * added. Thus, in case if all the entries that have been added to `contentValues` are null,
         * the row cannot be added to the database without signifying which column the null
         * value should be added to. In such a scenario, the column name that is provided as value for
         * `nullColumnHack` is used and this null value is added to that column. However, if instead
         * of a column name `null` is provided as the value for `nullColumnHack` (like in this case)
         * then a completely empty row can't be added to the database and `result` will be -1.
         */

        // Once the row is added to the database, closing the database and returning result.
        database.close();

        // Since negative value implies an error in insertion, thus sending negative value (if present).
        return (result < 0) ? result : resultNew;
    }


    /**
     * This method will be used to get the last row form the database. Will return the data as an object
     * of `Data` that will contain the details about the user who registered.
     *
     * @return An object containing the data of the user who last registered in the database.
     */
    public Data getLastRegisteredUser() {
        // Getting a readable instance of the database.
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        // Writing the SQL Query that will be used to get the last row from the database.
        String query = String.format("SELECT * FROM %s WHERE %s = (SELECT MAX(%s) FROM %s)",
                TABLE_NAME, COLUMN_INDEX, COLUMN_INDEX, TABLE_NAME);

        String imageQuery = String.format("SELECT * FROM %s WHERE %s = (SELECT MAX(%s) FROM %s)",
                IMAGE_TABLE, COLUMN_INDEX, COLUMN_INDEX, IMAGE_TABLE);

        // Creating a new cursor. This cursor will get location of the row when the following SQL
        // query is executed. Getting the image from the image table first.
        Cursor cursor = sqLiteDatabase.rawQuery(imageQuery, null);

        // If the cursor is not null, then moving it to the first row of the resultant query.
        if (cursor != null)
            cursor.moveToFirst();

        byte[] imageBlob = cursor.getBlob(1);

        // Next, getting data from the rest of the table using the other SQL query.
        cursor = sqLiteDatabase.rawQuery(query, null);

        // If the database had no such data, then the cursor will be null. If the cursor isn't null,
        // then moving the cursor to the row where we found the required data.
        if (cursor != null)
            cursor.moveToFirst();

        // Using the cursor to create a `Data` object by reading values from each column one by one.
        Data data = new Data(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(6),
                cursor.getString(8), cursor.getString(9), cursor.getString(7),
                cursor.getString(4), cursor.getString(5), imageBlob);

        // Once the work with the cursor is done, closing it.
        cursor.close();
        return data;
    }
}

/**
 * This will be a simple POJO class. Will be used to store and transfer the data regarding a user
 * from one section of the application to another. Will consist of a constructor that will be used
 * as the setter and will have individual getters for all the variables.
 */
class Data {
    private int userIndex;
    private String firstName;
    private String lastName;
    private String password;
    private String dateOfBirth;
    private String gender;
    private String location;
    private String pin;
    private String state;
    private String country;
    private Bitmap image;

    /**
     * The class constructor. Will be used as the only setter in the entire class.
     *
     * @param userIndex   An integer containing the index position for the row in which the user is present
     * @param firstName   The first name of the user.
     * @param lastName    The last name of the user.
     * @param password    The password that the user selects.
     * @param dateOfBirth The date of birth for the user.
     * @param gender      The gender of the user.
     * @param location    The location for the user.
     * @param pin         The PIN code that the user entered.
     * @param state       The state in which the user resides.
     * @param country     The country of residence for the user.
     * @param imageByte   The image as an array of bytes.
     */
    public Data(int userIndex, String firstName, String lastName, String password, String dateOfBirth,
                String gender, String location, String pin, String state, String country, byte[] imageByte) {
        this.userIndex = userIndex;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.location = location;
        this.pin = pin;
        this.state = state;
        this.country = country;
        this.image = ImageHandler.getImage(imageByte);
    }

    public int getUserIndex() {
        return userIndex;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getPin() {
        return pin;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public Bitmap getImage() {
        return image;
    }
}
