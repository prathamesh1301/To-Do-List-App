package com.example.mynotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    List<String> taskList;
    List<Integer> statusList;
    List<Integer> idList;
    Context context;
    DatabaseHelper db;

    public RecyclerViewAdapter(List<String> taskList, List<Integer> statusList, Context context,List<Integer> idList) {
        this.taskList = taskList;
        this.statusList = statusList;
        this.context = context;
        this.idList=idList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        db=new DatabaseHelper(context);
        holder.note.setText(taskList.get(position));
        if(statusList.get(position)==1){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("Delete Task");
                dialog.setMessage("Are you sure");
                dialog.setCancelable(true);
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db.deleteItem(idList.get(position));
                        idList.remove(position);
                        taskList.remove(position);
                        statusList.remove(position);
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                return true;
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isChecked()){
                    statusList.set(position,1);
                    db.updateStatus(taskList.get(position),statusList.get(position),idList.get(position));
                    notifyDataSetChanged();
                }else{
                    statusList.set(position,0);
                    db.updateStatus(taskList.get(position),statusList.get(position),idList.get(position));
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView note;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkbox);
            note=itemView.findViewById(R.id.note);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
