package com.example.newassignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import com.google.mlkit.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import java.util.List;


public class FaceView extends View {
    private Bitmap mBitmap;
    private List<Face> mFaces;

    public FaceView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    /**
     * Sets the bitmap background and the associated face detections.
     */
    void setContent(Bitmap bitmap, List<Face> faces) {
        Log.v("**draw***", "setContent");
        mBitmap = bitmap;
        mFaces = faces;
        invalidate();
    }

    /**
     * Draws the bitmap background and the associated face landmarks.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            Log.v("**draw***", " " + scale);
//            drawFaceRectangle(canvas, scale);
            drawFaceAnnotations(canvas, scale);
        }
    }

    /**
     * Draws the bitmap background, scaled to the device size.  Returns the scale for future use in
     * positioning the facial landmark graphics.
     */
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    /**
     * Draws a rectangle around each detected face
     */
//    private void drawFaceRectangle(Canvas canvas, double scale) {
//        Paint paint = new Paint();
//        paint.setColor(Color.GREEN);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(5);
//
////        for (int i = 0; i < mFaces.size(); ++i) {
////            Face face = mFaces.valueAt(i);
////            canvas.drawRect((float)(face.getPosition().x * scale),
////                    (float)(face.getPosition().y * scale),
////                    (float)((face.getPosition().x + face.getWidth()) * scale),
////                    (float)((face.getPosition().y + face.getHeight()) * scale),
////                    paint);
////        }
//    }

    /**
     * Draws a small circle for each detected landmark, centered at the detected landmark position.
     * <p>
     *
     * Note that eye landmarks are defined to be the midpoint between the detected eye corner
     * positions, which tends to place the eye landmarks at the lower eyelid rather than at the
     * pupil position.
     */
    private void drawFaceAnnotations(Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);


        for (Face face: mFaces) {
            Rect bounds = face.getBoundingBox();
            int left = (int)(bounds.left * scale);
            int right = (int)(bounds.right * scale);
            int top = (int)(bounds.top * scale);
            int bottom = (int)(bounds.bottom * scale);

            bounds.set(left, top, right, bottom);
            canvas.drawRect(bounds, paint);
        }
    }


    /**
     * Detects characteristics of a face
     */
//    private void detectFaceCharacteristics(Canvas canvas, double scale) {
//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(1);
//        paint.setTextSize(25.0f);
//
//        for (int i = 0; i < mFaces.size(); ++i) {
//            Face face = mFaces.valueAt(i);
//            float cx = (float)(face.getPosition().x * scale);
//            float cy = (float) (face.getPosition().y * scale);
//            canvas.drawText(String.valueOf(face.getIsSmilingProbability()), cx, cy + 10.0f, paint);
//        }
//    }

}
