package practice.android.searchmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Stella on 7/10/17.
 */

public class ImageUtility {
    public static final String TAG = "ImageUtility";
    public static final String TYPE = "/image";
    public static final int DEFAULT_POS = 0;

    Context mContext;

    public ImageUtility(Context context) {
        mContext = context;
    }

    public void saveImage(Bitmap image) {
        String path = mContext.getFilesDir() + TYPE;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            String newPath = path + DEFAULT_POS;
            File myCaptureFile = new File(newPath);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            Log.d(TAG, newPath);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getImage(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public void saveImageToSDCard(Bitmap bitmap, String name) {
        File fileSD = Environment.getExternalStorageDirectory();
        File imageFile = new File(fileSD, name + ".png");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
