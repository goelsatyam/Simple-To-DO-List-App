package com.example.codeml.to_do_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.inject.Singleton;

public class Todo extends AppCompatActivity {

    private static final int REQ_CODE_ADD = 1234;
    private static final String TAG = "test" ;
    private static int check = -1;
    private static ArrayList<String> list;
    private static ArrayAdapter<String> adapter;
    private static ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo);
        listview = (ListView) findViewById(R.id.allTasks);
        printTasks();

    }

    public void addActivity(View view) throws InterruptedException {
        Intent add = new Intent(this, addActivity.class);
        startActivityForResult(add, REQ_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_ADD) {
            printTasks();
        }
    }

    void printTasks() {

        Log.d(TAG, "RUN");
        TextView textView = (TextView) findViewById(R.id.empty);
        textView.setText("");

        try {
            Scanner file = new Scanner(openFileInput("future.txt"));
            list = new ArrayList<String>();

            int flag = 0;

            while (file.hasNext()) {
                flag = 1;
                String line = file.nextLine();
                list.add(line);
            }

            if (flag == 0) {
                textView.setText("Nothing To show");
            }
            else {
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d(TAG, String.valueOf(i));
                        if(check == i){
                            check = -1;
                            String item = list.get(i);
                            list.remove(i);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(Todo.this,"Removed Activity :" + item , Toast.LENGTH_SHORT).show();
                        }
                        else{
                            check = i;
                        }
                    }
                });

            }


        } catch (Exception e) {
            // do nothing
          }
    }

    @Override
    protected void onPause() {
        super.onPause();

        PrintStream file = null;
        try {
            file = new PrintStream(openFileOutput("future.txt", MODE_PRIVATE));
            file.print("");
            for(String s: list){
                file.println(s);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
