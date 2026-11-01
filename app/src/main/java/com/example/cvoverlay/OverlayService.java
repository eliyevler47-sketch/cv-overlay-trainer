package com.example.cvoverlay;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.List;

public class OverlayService extends Service {

    private WindowManager wm;
    private TrainingOverlayView view;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, buildNotification());
        if (view == null) attachOverlay();
        return START_STICKY;
    }

    private Notification buildNotification() {
        String ch = "cv_overlay";
        NotificationManager nm = getSystemService(NotificationManager.class);
        if (nm.getNotificationChannel(ch) == null) {
            nm.createNotificationChannel(
                new NotificationChannel(ch, "Overlay", NotificationManager.IMPORTANCE_LOW));
        }
        return new Notification.Builder(this, ch)
                .setContentTitle("CV Overlay Trainer running")
                .setContentText("Static training image overlay")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .build();
    }

    private void attachOverlay() {
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        view = new TrainingOverlayView(this);

        WindowManager.LayoutParams p = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        p.gravity = Gravity.TOP | Gravity.START;

        wm.addView(view, p);

        // Statik nümunə qutular (ekran ölçüsünə görə tənzimlə)
        List<TrainingOverlayView.Box> demo = Arrays.asList(
                new TrainingOverlayView.Box(new RectF(200, 500, 380, 640), "ammo", 0.92f),
                new TrainingOverlayView.Box(new RectF(420, 520, 560, 660), "crew", 0.87f)
        );
        view.setBoxes(demo);
    }

    @Override
    public void onDestroy() {
        if (view != null && wm != null) wm.removeView(view);
        view = null;
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent i) { return null; }
}
