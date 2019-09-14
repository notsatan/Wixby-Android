package com.tinyideas.wixby;

public class Date {
    private int date;
    private int monthOfYear;
    private int year;

    /**
     * This class will be a simple POJO object that will be used to store dates inside the object
     * and then retrieve the information when required. All the variables will have their own getter
     * and a global constructor instead of setters.
     *
     * @param date        An integer that will be the date selected.
     * @param monthOfYear An integer that will be the month selected.
     * @param year        An integer that will be the year selected.
     */
    public Date(int date, int monthOfYear, int year) {
        this.date = date;
        this.monthOfYear = monthOfYear;
        this.year = year;
    }

    /**
     * Getter that will return the integer stored as the date.
     *
     * @return An integer stored as the date.
     */
    public int getDate() {
        return date;
    }

    /**
     * Getter that will return an integer that will be the month of the year stored. Will be 0
     * indexed, need to be careful regarding the same.
     *
     * @return An integer representing the month stored.
     */
    public int getMonthOfYear() {
        return monthOfYear;
    }

    /**
     * Getter that will return the integer stored as the year.
     *
     * @return An integer stored as the year.
     */
    public int getYear() {
        return year;
    }
}