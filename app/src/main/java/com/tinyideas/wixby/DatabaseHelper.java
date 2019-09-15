package com.tinyideas.wixby;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Users.db";
    public static final String TABLE_NAME = "IndexTable";
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
                COLUMN_PLACE, COLUMN_STATE, COLUMN_COUNTRY, COLUMN_PIN, COLUMN_DOB, COLUMN_GENDER
        );

        Log.d("DEBUG", sqlQuery);

        // Running the SQL Query created above.
        sqLiteDatabase.execSQL(sqlQuery);
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
     *
     * @return If the insertion is successful, the row ID of the newly inserted row will be returned
     * and -1 will be returned in case of error.
     */
    public long registerUser(String firstName, String lastName, String password, String place,
                             String state, String country, String pin, String dob, String gender) {
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
        contentValues.put(COLUMN_PLACE, place);
        contentValues.put(COLUMN_STATE, state);
        contentValues.put(COLUMN_COUNTRY, country);
        contentValues.put(COLUMN_PIN, pin);
        contentValues.put(COLUMN_DOB, dob);
        contentValues.put(COLUMN_GENDER, gender);

        // Once all the values that are to be added to a row of the table have been placed inside this
        // object, adding this object to the table. If the insertion is successful, `result` will be
        // the row ID of the newly inserted row (-1 in case of error).
        long result = database.insert(TABLE_NAME, null, contentValues);

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

        Log.d("DEBUG", "Data saved successfully: " + contentValues.toString());

        // Once the row is added to the database, closing the database and returning result.
        database.close();
        return result;
    }
}
