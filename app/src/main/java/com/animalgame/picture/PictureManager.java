package com.animalgame.picture;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.animalgame.theanimalgame.AnimalController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class PictureManager {

    public final static int SELECT_PHOTO = 12345;
    private static final String FOLDER_NAME = "animalGame";
    private static final String TAG = "PictureManager";
    private static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME + "/";
    public static void setImageViewListener(final Activity activity, final ImageView imageView) {
        if (imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");

//                    photoPickerIntent.putExtra("imageViewId", imageView.getId());
//                    photoPickerIntent.putExtra("animalName", animalName);
//                    activity.setResult(RESULT_OK, photoPickerIntent);
                   // activity.finish();
                    AnimalController.setImageViewId(imageView.getId());

                    activity.startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                }
            });
        }
    }

    public static void setImageViewBitmap(View v, String imagePath, int imageViewId) {
        if (imageViewId > 0 && imagePath != null && !imagePath.isEmpty()) {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

                if (bitmap != null) {
                    ImageView imageView = v.findViewById(imageViewId);
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                Log.e(TAG, "Permission denied to access phone storage.");

            }
        }
    }

    public static String createImageName(String animalName) {
        return ROOT + animalName + ".png";
    }

    public static void saveImageToDirectory(Context context, String oldFilename, String newFilename) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            File createDir = new File(ROOT);
            boolean result = true;
            if (!createDir.exists()) {
                result = createDir.mkdir();
            }
            if (!result) {
                Log.e(TAG, "Failed to create directory.");
            }
            //if old filename is empty then there is no file selected
            if (!oldFilename.isEmpty()) {
                if (!oldFilename.equalsIgnoreCase(newFilename)) {
                    PictureManager.transferFileContents(context, oldFilename, newFilename);
                } else {
                    //create temp file so that you create new file but still delete old file
                    String tempFilename = newFilename + System.currentTimeMillis()/1000;
                    PictureManager.transferFileContents(context, oldFilename, tempFilename);
                    PictureManager.transferFileContents(context, tempFilename, newFilename);
                }
            }
        }
    }

    public static void deleteImage(String filename) {
        if (filename != null && !filename.isEmpty()) {
            PictureManager.deleteFile(new File(filename));
        }
    }
    private static void deleteFile(File file) {
        try {
            if (file.delete()) {
                Log.i(TAG, "Deleted file: " + file.getName());
            } else {
                Log.e(TAG, "Did not delete file: " + file.getName());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static void transferFileContents(Context context, String oldFilename, String newFilename) {
        File sourceFile = new File(oldFilename);
        File destFile = new File(newFilename);
        //check that there isn't already a file case-insensitive ex. aaaaa.png vs aaAaa.png
        if (destFile.exists()) {
            if (destFile.delete()) {
                destFile = new File(newFilename);
            }
        }

        try {
            if (destFile.createNewFile()) {

                FileChannel source = null;
                FileChannel destination = null;

                try {
                    source = new FileInputStream(sourceFile).getChannel();
                    destination = new FileOutputStream(destFile).getChannel();
                    destination.transferFrom(source, 0, source.size());
                } finally {
                    if (source != null) {
                        source.close();
                    }
                    if (destination != null) {
                        destination.close();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(context, "Failed to transfer file contents.", Toast.LENGTH_SHORT).show();
        }

        //if previous filename is in picture directory, delete it
        if (oldFilename.startsWith(ROOT)) {
            PictureManager.deleteFile(sourceFile);
        }
    }
}
