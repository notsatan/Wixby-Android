package com.tinyideas.wixby;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * A class made to deal with images. As the images cannot be inserted into the database directly, they
 * need to be converted into an array of bytes to be inserted in the database, and while displaying
 * the same to the user, the image needs to be converted back into an actual image from an array of
 * bytes. This class will be dealing with the conversion as and when required by the program. Methods
 * inside this class will be used to convert an image into an array of bytes or vice-versa.
 */
public class ImageHandler {

    /**
     * This method will take the image in the form of a bitmap, convert the image into a byte array
     * and return the same as an output.
     *
     * @param imageBitmap The image selected by the user. Should be supplied as a `Bitmap`.
     * @return An array of `byte` that represent the image given to the method
     */
    public static byte[] getBytes(Bitmap imageBitmap) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, arrayOutputStream);

        return arrayOutputStream.toByteArray();
    }

    /**
     * This method will take the image in the form of an array of bytes, convert the data into a bitmap
     * containing the image and return the same as an output.
     *
     * @param imageBytes The image in the form of an array of bytes.
     * @return The bitmap of the image that was supplied as bytes to the method.
     */
    public static Bitmap getImage(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
