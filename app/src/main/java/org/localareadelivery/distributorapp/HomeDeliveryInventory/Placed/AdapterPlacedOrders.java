package org.localareadelivery.distributorapp.HomeDeliveryInventory.Placed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.localareadelivery.distributorapp.Model.Order;
import org.localareadelivery.distributorapp.Model.DeliveryAddress;
import org.localareadelivery.distributorapp.ModelStats.OrderStats;
import org.localareadelivery.distributorapp.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class AdapterPlacedOrders extends RecyclerView.Adapter<AdapterPlacedOrders.ViewHolder>{

    private List<Order> dataset = null;
    private NotifyConfirmOrder notifyConfirmOrder;

    AdapterPlacedOrders(List<Order> dataset, NotifyConfirmOrder notifyConfirmOrder) {
        this.dataset = dataset;
        this.notifyConfirmOrder = notifyConfirmOrder;
    }

    @Override
    public AdapterPlacedOrders.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_placed,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPlacedOrders.ViewHolder holder, int position) {

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
            holder.dateTimePlaced.setText("" + order.getDateTimePlaced().toLocaleString());


            holder.deliveryAddressName.setText(deliveryAddress.getName());

            holder.deliveryAddress.setText(deliveryAddress.getDeliveryAddress() + ",\n"
                                            + deliveryAddress.getCity() + " - " + deliveryAddress.getPincode());

            holder.deliveryAddressPhone.setText("Phone : " + deliveryAddress.getPhoneNumber());

            holder.numberOfItems.setText(orderStats.getItemCount() + " Items");
            holder.orderTotal.setText("| Total : " + (orderStats.getItemTotal() + order.getDeliveryCharges()));
            //holder.currentStatus.setText();


        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{


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

        @Bind(R.id.confirmOrderButton)
        TextView confirmOrderButton;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);


        }


        @OnClick(R.id.confirmOrderButton)
        void onClickConfirmButton(View view)
        {
            notifyConfirmOrder.notifyConfirmOrder(dataset.get(getLayoutPosition()));
        }


        @OnClick(R.id.close_button)
        void closeButton(View view)
        {
            notifyConfirmOrder.notifyCancelOrder(dataset.get(getLayoutPosition()));
        }
    }




    interface NotifyConfirmOrder{
        void notifyConfirmOrder(Order order);
        void notifyCancelOrder(Order order);
    }

}
