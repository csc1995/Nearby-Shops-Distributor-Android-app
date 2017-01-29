package org.nearbyshops.shopkeeperapp.DeliveryGuyDashboard.OutForDelivery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.nearbyshops.shopkeeperapp.Model.Order;
import org.nearbyshops.shopkeeperapp.Model.DeliveryAddress;
import org.nearbyshops.shopkeeperapp.ModelStats.OrderStats;
import org.nearbyshops.shopkeeperapp.OrderHistoryHD.Utility.UtilityOrderStatus;
import org.nearbyshops.shopkeeperapp.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
public class AdapterOutForDelivery extends RecyclerView.Adapter<AdapterOutForDelivery.ViewHolder>{


    private List<Order> dataset = null;
    private NotifyHandoverToUser notifications;


    public AdapterOutForDelivery(List<Order> dataset, NotifyHandoverToUser notifications) {
        this.dataset = dataset;
//        this.context = context;
        this.notifications = notifications;

    }

    @Override
    public AdapterOutForDelivery.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_pending_handover_driver_dashboard,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterOutForDelivery.ViewHolder holder, int position) {

        if(dataset!=null)
        {
            if(dataset.size() <= position)
            {
                return;
            }

            Order order = dataset.get(position);
            DeliveryAddress deliveryAddress = order.getDeliveryAddress();
            OrderStats orderStats = order.getOrderStats();

            holder.orderID.setText("Order ID : " + order.getOrderID());
            holder.dateTimePlaced.setText("Placed : " + order.getDateTimePlaced().toLocaleString());


            holder.deliveryAddressName.setText(deliveryAddress.getName());

            holder.deliveryAddress.setText(deliveryAddress.getDeliveryAddress() + ",\n"
                                            + deliveryAddress.getCity() + " - " + deliveryAddress.getPincode());

            holder.deliveryAddressPhone.setText("Phone : " + deliveryAddress.getPhoneNumber());

            holder.numberOfItems.setText(orderStats.getItemCount() + " Items");
            holder.orderTotal.setText("| Total : " + (orderStats.getItemTotal() + order.getDeliveryCharges()));
            holder.currentStatus.setText("Status : " + UtilityOrderStatus.getStatus(order.getStatusHomeDelivery(),false,false));

            holder.distance.setText(String.format("%.2f", deliveryAddress.getRt_distance()) + " Km");


        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.order_id) TextView orderID;
        @Bind(R.id.dateTimePlaced) TextView dateTimePlaced;
        @Bind(R.id.deliveryAddressName) TextView deliveryAddressName;
        @Bind(R.id.deliveryAddress) TextView deliveryAddress;
        @Bind(R.id.deliveryAddressPhone) TextView deliveryAddressPhone;
        @Bind(R.id.numberOfItems) TextView numberOfItems;
        @Bind(R.id.orderTotal) TextView orderTotal;
        @Bind(R.id.currentStatus) TextView currentStatus;
        @Bind(R.id.acceptHandoverButton) TextView cancelHandoverButton;
        @Bind(R.id.return_package) TextView returnPackage;
        @Bind(R.id.distance) TextView distance;
        @Bind(R.id.get_directions) TextView getDirections;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.acceptHandoverButton)
        void onClickConfirmButton(View view)
        {
            notifications.notifyHandoverToUser(dataset.get(getLayoutPosition()));
        }


        @OnClick(R.id.return_package)
        void onClickReturnPackage(View view)
        {
            notifications.notifyReturnPackage(dataset.get(getLayoutPosition()));
        }


        @OnClick(R.id.get_directions)
        void getDirections()
        {
            Order order = dataset.get(getLayoutPosition());
            notifications.notifyGetDirections(order.getDeliveryAddress());
        }

    }


    interface NotifyHandoverToUser {
        void notifyHandoverToUser(Order order);
        void notifyReturnPackage(Order order);
        void notifyGetDirections(DeliveryAddress deliveryAddress);
    }

}
