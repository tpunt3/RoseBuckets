package edu.rosehulman.punttj.rosebuckets;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alangavr on 2/5/2017.
 */

public class PhotoUtils {
    public static Uri getOutputMediaUri(String folder) {
        File storageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                folder);

        Log.d(Constants.PHOTO_TAG, "Media to be stored at " + storageDir.getPath());

        // Create the storage directory if it does not exist
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.d(Constants.PHOTO_TAG, "Failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(new Date());
        File mediaFile = new File(storageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        // Return the URI of the file.
        return Uri.fromFile(mediaFile);
    }

}
