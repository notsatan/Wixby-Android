
# Wixby-Android
A template Android app involving a user registration process. Once the user is registered, the details that were entered by the user at the time of the registration are displayed to the user.

The details entered by the user are saved in an SQL database in the device. The registration process consists of two screens. There is an additional popup activity that is used to display the details entered back to the user.

### How do I use this?
- Download the APK file for the application from [here](/../../releases)
- Install the downloaded APK file and then run it.

> The APK version linked above is the release version and is sealed.

## Okay, it works, but what is under the hood?
It, uhh
- Uses a common XML layout for the first two activities.
- The layout file has a view-stub. Depending on the activity, the view-stub will be inflated with a layout.
- The calendar in the first activity is a Dialog that is used as a popup.
- The GPS icon in the second activity will use the GPS to get the location of the device in the background.
- Once the submit button is hit, the data is stored into an SQLite database in the background.
- The third activity extracts the last row of data from the database and displays the same as a popup 
- The third activity is an actual popup, the size of the window is shrunken and the background is set as transparent.

## How is an image stored in the database?
The image is received in the form of a Bitmap. In a simple sense, Bitmaps are a grid of cells (pixels) with each of these pixel containing values that define the color of that particular pixel.

The color of each pixel is defined either as hex code or as RGB (both being inter-related). Once this image is received by the application as a Bitmap, this Bitmap is converted into an array of bytes --->> `byte[]`.

This byte array is then used to store the image into the database as a BLOB (Binary Large OBject) datatype.

> Storing a Bitmap (or image) in a database is usually considered highly inefficient and is a bad practice. A better approach would be to store the image normally, and then add the location of that image to the database. When the image is required, it should be fetched directly from its location that is present in the database.


#### Why does this application store images in the database?
This app is made for users to use in their devices. In such a case, once the user selects an image to add to the database and then deletes the image from the device, then the path for that image present in the database now becomes invalid. To avoid such a scenario, the images are stored in the database as BLOB objects.

## Why not store the images as a base64 string?

> What is base64?
> 
> Similar to how this application is converting Bitmaps into an array of bytes, it is also possible to convert the Bitmap into a base64 string and then store it in the database.

**Both these approaches have their pros and cons. This section explains the Bitmaps are being converted to BLOB instead of base64 strings for storage**

Uhh,
- As a general case, a Bitmap when converted into a base64 string takes up roughly 33% more space than the same Bitmap converted into binary. [Source](https://www.davidbcalhoun.com/2011/when-to-base64-encode-images-and-when-not-to/)

- Base64 is stream-friendly, in the sense that it can be encoded or decoded on the fly without having to explicitly know the total size of data. Thus, base64 is more suitable transfer mechanism instead of storage.

Taking all of this into account, especially the size bloating that is caused if the image is stored as a Base64 string (as opposed to binary) and the fact that using a Base64 string provides no checksum or anything of similar value that can be used for storage, there really is no advantage of using Base64 over binary.

And using Binary can be more efficient on the memory thus, the Bitmap is being converted into a binary array.

## I want memes. Gib Memes.
Alright, here you go.

![Developer Meme](https://i.imgur.com/eWvunLu.jpg)

## Any external links for more insight?
- [A Guide To Bitmaps For Beginners](http://paulbourke.net/dataformats/bitmaps/)

- [Storing Image In A Database BLOB Vs Base64](https://stackoverflow.com/a/9723812)

- [Blob In Android](https://developer.android.com/reference/java/sql/Blob)

## Licensing
This project is licensed under [MIT License](/LICENSE)
