package com.pagupa.notiapp.servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pagupa.notiapp.MainActivity;
import com.pagupa.notiapp.R;
import com.pagupa.notiapp.clases.Server;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService  {
    private static final String CHANNEL_ID = "Canal 1";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Server.guardarTokenFcm(getApplicationContext(), token);
        Log.e("fcm java", "new token "+token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> params = message.getData();
        String titulo = params.get("titulo");
        String mensaje = params.get("body");
        String accion = params.get("accion");
        int codigo = Integer.parseInt(params.get("codigo"));

        Log.e("SERVICE", "FireBase -> JSON: " + titulo+"    "+mensaje);
        Log.e("SERVICE", "codigo "+codigo+"  accion "+accion);

        Intent resultIntent = null;

        if(!accion.equals("")) {
            resultIntent = new Intent(this, MainActivity.class);
            resultIntent.putExtra("accion", accion);
            resultIntent.putExtra("codigo", codigo);
        }

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE);

        Bitmap logo= BitmapFactory.decodeResource(getResources(), R.drawable.pagupa_logo);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.megafono)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setTicker(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(logo))
                .setContentIntent(resultPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence _name = "Canal 1";
            String _description = "Servicios";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, _name, importance);
            channel.setDescription(_description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(658, builder.build());
        } else {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(658, builder.build());
        }
    }
}
