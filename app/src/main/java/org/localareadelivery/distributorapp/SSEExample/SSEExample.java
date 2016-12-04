package org.localareadelivery.distributorapp.SSEExample;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.localareadelivery.distributorapp.HomeDistributor.SSEIntentService;
import org.localareadelivery.distributorapp.MyApplication;
import org.localareadelivery.distributorapp.R;
import org.localareadelivery.distributorapp.Utility.UtilityGeneral;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SSEExample extends AppCompatActivity {


    @Bind(R.id.send_button)
    TextView sendButton;

    @Bind(R.id.result)
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sseexample2);
        ButterKnife.bind(this);
    }




    @OnClick(R.id.send_button)
    void sendClick()
    {

        Intent intent = new Intent(this, SSEIntentService.class);
        intent.putExtra("extra","Extra string");
        startService(intent);

//        new SseTask().execute(null,null);
    }





    class SseTask extends AsyncTask<Integer,String,String>
    {


        void connectSSE()
        {

        }

        @Override
        protected String doInBackground(Integer... params) {


            Client client = ClientBuilder.newBuilder()
                    .register(SseFeature.class).build();

            String url = UtilityGeneral.getServiceURL(MyApplication.getAppContext()) + "/api/broadcast";

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

                publishProgress(inboundEvent.getName() + "; "
                        + inboundEvent.readData(String.class));

            }

            return "finished";
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Toast.makeText(SSEExample.this,values[0],Toast.LENGTH_SHORT).show();


/*

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(SSEExample.this)
                            .setContentTitle("SSE Notification")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(values[0]))
                            .setContentText(values[0])
                            .setSmallIcon(R.drawable.ic_details_black_48px);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(2, mBuilder.build());*/

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(SSEExample.this,s,Toast.LENGTH_SHORT).show();

        }
    }



}
