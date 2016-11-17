package org.localareadelivery.distributorapp.Home;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.SSEExample.SSEExample;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by sumeet on 17/11/16.
 */

public class SSEIntentService extends IntentService{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SSEIntentService(String name) {
        super(name);
    }

    public SSEIntentService() {
        super("service");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Client client = ClientBuilder.newBuilder()
                .register(SseFeature.class).build();

        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/Order/Notifications/1";

        WebTarget target = client.target(url);


        EventInput eventInput = target.request().get(EventInput.class);


        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                // connection has been closed
                break;
            }

            System.out.println(inboundEvent.getName() + "; "
                    + inboundEvent.readData(String.class));

            String message = inboundEvent.getName() + "; " + inboundEvent.readData(String.class);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(SSEIntentService.this)
                            .setContentTitle("SSE Notification")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("server"))
                            .setContentText(message)
                            .setSmallIcon(R.drawable.ic_details_black_48px)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));



            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(2, mBuilder.build());
        }

    }



}
