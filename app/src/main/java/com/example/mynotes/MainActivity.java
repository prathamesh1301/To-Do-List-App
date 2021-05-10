package com.example.mynotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    String task="";
    DatabaseHelper db;
    List<String> tasks;
    List<Integer> status;
    List<Integer> ids;
    RecyclerViewAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recylerView);
        floatingActionButton=findViewById(R.id.floatingButton);
        db=new DatabaseHelper(MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasks=new ArrayList<>();
        status=new ArrayList<>();
        ids=new ArrayList<>();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater=LayoutInflater.from(getApplicationContext());

                View view=layoutInflater.inflate(R.layout.dialog_box_bg,null,false);
                EditText addTaskInput=view.findViewById(R.id.addTaskInput);

                alertDialog.setView(view);
                alertDialog.setCancelable(true);
                alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();
                        task=addTaskInput.getText().toString();
                        if(!TextUtils.isEmpty(task)){
                            boolean result=db.addTask(task);
                            if(result){
                                Toast.makeText(MainActivity.this, "Task Added", Toast.LENGTH_SHORT).show();
                                getDataIntoArray();
                                recyclerViewAdapter=new RecyclerViewAdapter(tasks,status,MainActivity.this,ids);
                                recyclerView.setAdapter(recyclerViewAdapter);
                            }else{
                                Toast.makeText(MainActivity.this, "Task not added", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(MainActivity.this, "Add task did not have text to add", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();


            }
        });
        getDataIntoArray();
        recyclerViewAdapter=new RecyclerViewAdapter(tasks,status,MainActivity.this,ids);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
    public void getDataIntoArray() {
        tasks.clear();
        status.clear();
        ids.clear();
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            return;
        } else {
            res.moveToFirst();
            ids.add(res.getInt(0));
            tasks.add(res.getString(1));
            status.add(res.getInt(2));


            while (res.moveToNext()) {
                ids.add(res.getInt(0));
                tasks.add(res.getString(1));
                status.add(res.getInt(2));
            }
        }
    }
}