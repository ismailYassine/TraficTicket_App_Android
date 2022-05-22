package com.example.traficTicketapp_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    ArrayList<Offender> offenderList;
    ItemClickListener listener;

    public CustomAdapter(ArrayList<Offender> offenderList, ItemClickListener listener){
        this.offenderList = offenderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getOffenderName().setText(offenderList.get(position).firstName + " " + offenderList.get(position).lastName);
        holder.getOffenderSpeed().setText(String.valueOf(offenderList.get(position).offenderSpeed));
        holder.getfinedDate().setText(offenderList.get(position).fineDate);
        holder.getFinedAmount().setText(offenderList.get(position).fineAmount.toString()+"$");

//        holder.itemView.setOnClickListener(view -> {
//            listener.onItemClick(offenderList.get(position));
//        });
    }

    @Override
    public int getItemCount() {
        return offenderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView offenderName;
        TextView offenderSpeed;
        TextView fineddate;
        TextView finedAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            offenderName = itemView.findViewById(R.id.tvName);
            fineddate = itemView.findViewById(R.id.tvDate);
            finedAmount = itemView.findViewById(R.id.tvFinedAmount);
            offenderSpeed = itemView.findViewById(R.id.textView4foo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(offenderList.get(pos));
                }
            });
        }

        public TextView getOffenderName(){
            return offenderName;
        }
        public TextView getOffenderSpeed(){ return offenderSpeed; }
        public TextView getfinedDate(){
            return fineddate;
        }
        public TextView getFinedAmount(){ return finedAmount; }




    }
    public interface ItemClickListener{
        void onItemClick(Offender offender);
    }
}
