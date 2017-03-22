package org.nearbyshops.shopkeeperapp.Notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.nearbyshops.shopkeeperapp.MyApplication;
import org.nearbyshops.shopkeeperapp.R;
import org.nearbyshops.shopkeeperapp.Utility.UtilityGeneral;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by sumeet on 17/11/16.
 */

public class SSEIntentService extends IntentService{

    public static final String SHOP_ID = "SHOP_ID";

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


    void logMessage(String message)
    {
        Log.d("notification_log",message);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

//        System.out.println("Inside Notification Intent Service");

        logMessage("Inside Notification Intent Service !");



        while (true)
        {
            try{

                handleNotification(intent);
            }
            catch (Exception ex)
            {
                System.out.println("Exception : " + ex.toString());
            }
        }



    }



    void handleNotification(Intent intent)
    {
        Client client = ClientBuilder.newBuilder()
                .register(SseFeature.class).build();

        int shopID = -1;

//        System.out.println("Inside Before Shop Fetch!");
        logMessage("Inside Before Shop Fetch !");

        if (intent != null) {
            shopID = intent.getIntExtra(SHOP_ID,-1);
        }

        if(shopID==-1)
        {
            return;
        }


//        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/Order/ShopStaff/Notifications/" + String.valueOf(shopID);

        String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/Order/Notifications/" + String.valueOf(shopID);



        System.out.println("URL : " + url);
        logMessage("URL : " + url);


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

            String eventName = inboundEvent.getName();
            String message = inboundEvent.readData(String.class);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(SSEIntentService.this)
                            .setContentTitle(eventName)
                            .setContentText(message)
                            .setSmallIcon(R.drawable.fab_add)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


//            .setStyle(new NotificationCompat.BigTextStyle().bigText("Order Received !"))


            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//            Intent notificationIntent = new Intent(getApplicationContext(), OrderHistoryPFS.class);

//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//            PendingIntent intentPending = PendingIntent.getActivity(getApplicationContext(), 0,
//                    notificationIntent, 0);

//            Notification notification = mBuilder.build();

//            notification.set(getApplicationContext(),"Title", message, intent);
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            // mId allows you to update the notification later on.
            mNotificationManager.notify(2, mBuilder.build());
        }
    }


}
