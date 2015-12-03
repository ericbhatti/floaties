package com.bezyapps.floatiesdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bezy.floatiesdemo.R;
import com.bezyapps.floatieslibrary.Floaty;
import com.bezyapps.floatieslibrary.FloatyOrientationListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Floaty floaty;
    Button button_start, button_stop;
    private static final int NOTIFICATION_ID = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_start = (Button) findViewById(R.id.button_start);
        button_stop = (Button) findViewById(R.id.button_stop);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = Floaty.createNotification(this, "Floaty Demo", "Service Running", R.drawable.float_icon, resultPendingIntent);
        View head = LayoutInflater.from(this).inflate(R.layout.float_head, null);
        View body = LayoutInflater.from(this).inflate(R.layout.float_body, null);
        ArrayList<String> strings = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
        final ListView listView = (ListView) body.findViewById(R.id.listViewTasks);
        listView.setAdapter(arrayAdapter);
        final EditText editText = (EditText) body.findViewById(R.id.editText_task);
        Button button = (Button) body.findViewById(R.id.button_save);
        floaty = Floaty.createInstance(this, head, body, NOTIFICATION_ID, notification, new FloatyOrientationListener() {
            @Override
            public void beforeOrientationChange(Floaty floaty) {
                Toast.makeText(MainActivity.this, "Orientation Change Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterOrientationChange(Floaty floaty) {
                Toast.makeText(MainActivity.this, "Orientation Change End", Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = editText.getText().toString().trim();
                if (!item.isEmpty()) {
                    editText.setText("");
                    arrayAdapter.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                arrayAdapter.remove(arrayAdapter.getItem(position));
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floaty.startService();
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floaty.stopService();
            }
        });
    }
}
