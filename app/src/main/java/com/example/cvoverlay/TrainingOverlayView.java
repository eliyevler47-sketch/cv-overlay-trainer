package com.example.cvoverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TrainingOverlayView extends View {

    public static class Box {
        public final RectF rect;
        public final String label;
        public final float confidence;
        public Box(RectF rect, String label, float confidence) {
            this.rect = rect; this.label = label; this.confidence = confidence;
        }
    }

    private final List<Box> boxes = new ArrayList<>();
    private final Paint boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint bgPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public TrainingOverlayView(Context c) {
        super(c);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(dp(2f));
        boxPaint.setColor(0xFF00E5FF);

        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(0xCC000000);

        txtPaint.setColor(0xFFFFFFFF);
        txtPaint.setTextSize(dp(12f));
        txtPaint.setFakeBoldText(true);
    }

    private float dp(float v) { return v * getResources().getDisplayMetrics().density; }

    public void setBoxes(List<Box> newBoxes) {
        boxes.clear();
        if (newBoxes != null) boxes.addAll(newBoxes);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Box b : boxes) {
            canvas.drawRoundRect(b.rect, dp(6), dp(6), boxPaint);

            String text = b.label + "  " + Math.round(b.confidence * 100) + "%";
            float pad = dp(4);
            float tw = txtPaint.measureText(text);
            float th = txtPaint.getTextSize();
            RectF bg = new RectF(b.rect.left, b.rect.top - th - pad*2,
                                 b.rect.left + tw + pad*2, b.rect.top);
            canvas.drawRoundRect(bg, dp(4), dp(4), bgPaint);
            canvas.drawText(text, bg.left + pad, bg.bottom - pad, txtPaint);
        }
    }
}
