package org.localareadelivery.distributorapp.OrdersHomeDelivery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.ModelStats.DeliveryAddress;
import org.localareadelivery.distributorapp.ModelStats.OrderStats;
import org.localareadelivery.distributorapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
public class AdapterPackedOrders extends RecyclerView.Adapter<AdapterPackedOrders.ViewHolder>{


    List<Order> dataset = null;

    Map<Integer,Order> selectedOrders = new HashMap<>();

    Context context;

    NotificationReciever notifications;


    public AdapterPackedOrders(List<Order> dataset, Context context, NotificationReciever notifications) {
        this.dataset = dataset;
        this.context = context;
        this.notifications = notifications;


        selectedOrders.clear();
    }

    @Override
    public AdapterPackedOrders.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_packed,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPackedOrders.ViewHolder holder, int position) {

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
            holder.dateTimePlaced.setText("Placed : " + order.getDateTimePlaced().toString());


            holder.deliveryAddressName.setText(deliveryAddress.getName());

            holder.deliveryAddress.setText(deliveryAddress.getDeliveryAddress() + ",\n"
                                            + deliveryAddress.getCity() + " - " + deliveryAddress.getPincode());

            holder.deliveryAddressPhone.setText("Phone : " + deliveryAddress.getPhoneNumber());

            holder.numberOfItems.setText(orderStats.getItemCount() + " Items");
            holder.orderTotal.setText("| Total : " + (orderStats.getItemTotal() + order.getDeliveryCharges()));
            //holder.currentStatus.setText();


            if(selectedOrders.containsKey(order.getOrderID()))
            {
                holder.listItemOrdersPacked.setBackgroundResource(R.color.itemAvailable);
            }else
            {
                holder.listItemOrdersPacked.setBackgroundResource(R.color.listItemGrey);
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.list_item_order_packed)
        RelativeLayout listItemOrdersPacked;

        @Bind(R.id.order_id)
        TextView orderID;

        @Bind(R.id.dateTimePlaced)
        TextView dateTimePlaced;

        @Bind(R.id.deliveryAddressName)
        TextView deliveryAddressName;

        @Bind(R.id.deliveryAddress)
        TextView deliveryAddress;

        @Bind(R.id.deliveryAddressPhone)
        TextView deliveryAddressPhone;


        @Bind(R.id.numberOfItems)
        TextView numberOfItems;

        @Bind(R.id.orderTotal)
        TextView orderTotal;

        @Bind(R.id.currentStatus)
        TextView currentStatus;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);


        }


        @OnClick(R.id.list_item_order_packed)
        void onListItemClick(View view) {

            Order selected = dataset.get(getLayoutPosition());

            if (selectedOrders.containsKey(selected.getOrderID()))
            {
                selectedOrders.remove(selected.getOrderID());
            }
            else
            {
                selectedOrders.put(selected.getOrderID(),selected);
            }

            //notifyDataSetChanged();

            notifyItemChanged(getLayoutPosition());
        }


        void onClickConfirmButton(View view)
        {
            notifications.notifyConfirmOrder(dataset.get(getLayoutPosition()));
        }

    }


    public Map<Integer, Order> getSelectedOrders() {
        return selectedOrders;
    }

    public void setSelectedOrders(Map<Integer, Order> selectedOrders) {
        this.selectedOrders = selectedOrders;
    }




    public interface NotificationReciever{

        void notifyConfirmOrder(Order order);

    }

}
