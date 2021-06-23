package com.example.fdas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fdas.Model.FarmModel;

import java.util.List;

public class GetAllFarmsAdapter extends RecyclerView.Adapter<GetAllFarmsAdapter.FarmsVH>{

Context context;
List<FarmModel> farms;

    public GetAllFarmsAdapter(Context context, List<FarmModel> farms) {
        this.context = context;
        this.farms = farms;
    }

    @NonNull
    @Override
    public FarmsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_list_layout,null);
        return new FarmsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmsVH holder, int position) {
        holder.nameTv.setText(farms.get(position).getFarm_owner());
        holder.mailTv.setText(farms.get(position).getFarm_contact());
    }

    @Override
    public int getItemCount() {
        return farms.size();
    }

    class FarmsVH extends RecyclerView.ViewHolder{
        TextView nameTv,mailTv;
        public FarmsVH(@NonNull View itemView) {
            super(itemView);

            nameTv=itemView.findViewById(R.id.nameTv);
            mailTv=itemView.findViewById(R.id.mailTv);
        }
    }
}
