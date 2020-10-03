package com.anspace.brandscore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareView {
    Uri uri;

    public  Uri shareView(final View view, final Context baseContext, final Context activityContextOnly, final String textToMail) {

//        view.post(new Runnable() {
//            @Override
//            public void run() {
                int heightG = view.getHeight();
                int widthG = view.getWidth();
                 uri = sendViewViaMail(view, baseContext, activityContextOnly, widthG, heightG, textToMail);
//            }
//        });
        return uri;
    }

    private static Uri sendViewViaMail(View view, final Context baseContext, Context activityContextOnly, int widthG, int heightG, String textToMail) {
        Bitmap bitmap = createViewBitmap(view, widthG, heightG);
        Uri imageUri = null;
        File file = null;
        FileOutputStream fos1 = null;
        try {
            File folder = new File(activityContextOnly.getCacheDir() + File.separator + "My Temp Files");

            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }

            String filename = "img.jpg";
            file = new File(folder.getPath(), filename);

            fos1 = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos1);
            imageUri = FileProvider.getUriForFile(activityContextOnly, activityContextOnly.getPackageName() + ".my.package.name.provider", file);
        } catch (Exception ex) {
            Toast.makeText(baseContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                fos1.close();
            } catch (IOException e) {
                Toast.makeText(baseContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


//        final Intent emailIntent1 = new Intent(Intent.ACTION_SEND);
//        emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        emailIntent1.putExtra(Intent.EXTRA_EMAIL, new String[]{});
//        emailIntent1.putExtra(Intent.EXTRA_STREAM, imageUri);
//        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "[" + "COMPANY_HEADER" + "]");
//        emailIntent1.putExtra(Intent.EXTRA_TEXT, textToMail);
//        emailIntent1.setPackage("com.whatsapp");
////        emailIntent1.setData(Uri.parse("mailto:" + "mail@gmail.com")); // or just "mailto:" for blank
//        emailIntent1.setType("image/jpg");
//        activityContextOnly.startActivity(Intent.createChooser(emailIntent1, "Send email using"));
        return imageUri;
    }

    public static Bitmap createViewBitmap(View view, int widthG, int heightG) {
        Bitmap viewBitmap = Bitmap.createBitmap(widthG, heightG, Bitmap.Config.RGB_565);//rgb565
        Canvas viewCanvas = new Canvas(viewBitmap);
        Drawable backgroundDrawable = view.getBackground();

        if(backgroundDrawable!=null){
            backgroundDrawable.draw(viewCanvas);
        }
        else{
            viewCanvas.drawColor(Color.WHITE);
//            viewCanvas.drawPicture();
        }
        view.draw(viewCanvas);
        return viewBitmap;
    }
}
