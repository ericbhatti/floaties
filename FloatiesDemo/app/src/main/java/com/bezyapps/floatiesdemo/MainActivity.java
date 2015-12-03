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

        // Inflate the Views that are to be used as HEAD and BODY of The Window
        View head = LayoutInflater.from(this).inflate(R.layout.float_head, null);
        // You should not add click listeners to head as it will be overridden, but the purpose of not making head just
        // an ImageView is so you can add multiple views in it, and show and hide the relevant views to notify user etc.
        View body = LayoutInflater.from(this).inflate(R.layout.float_body, null);

        // Access child views of body
        final ListView listView = (ListView) body.findViewById(R.id.listViewTasks);
        final EditText editText = (EditText) body.findViewById(R.id.editText_task);
        Button button = (Button) body.findViewById(R.id.button_save);

        ArrayList<String> strings = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);

        listView.setAdapter(arrayAdapter);

        // Get the instance of the Floaty
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

        // Set your domain specific logic to body's children
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
