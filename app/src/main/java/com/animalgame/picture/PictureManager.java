package com.animalgame.picture;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.animalgame.theanimalgame.AnimalController;

public class PictureManager {

    public final static int SELECT_PHOTO = 12345;
    private static final String TAG = "PictureManager";
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

                ImageView imageView = v.findViewById(imageViewId);
                imageView.setImageBitmap(bitmap);
            } else {
                Log.e(TAG, "Permission denied to access phone storage.");

            }
        }
    }
}
