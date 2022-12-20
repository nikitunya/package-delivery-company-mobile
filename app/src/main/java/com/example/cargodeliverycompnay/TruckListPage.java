package com.example.cargodeliverycompnay;

import static com.example.cargodeliverycompnay.Constants.ALL_TRUCKS_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cargodeliverycompnay.model.Truck;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TruckListPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_list);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try{
                String response = Rest.sendGet(ALL_TRUCKS_URL);
                handler.post(()->{
                    if (!response.equals("")){
                        // Jei yra data, data reikia serializuotuti

                        // Dar reikalingas json builder
                        // Jei yra deserializatoriai, reikia registuruoti, GsonBuilder().
                        Gson builder = new GsonBuilder().create();

                        Type truckType = new TypeToken<List<Truck>>(){}.getType();

                        final List<Truck> truckListFromJson = builder.fromJson(response, truckType);

                        ListView truckListView = findViewById(R.id.truckList);

                        ArrayAdapter<Truck> arrayAdapter = new ArrayAdapter<>(TruckListPage.this, android.R.layout.simple_list_item_1, truckListFromJson);
                        Log.d("nikita", String.valueOf(arrayAdapter));
                        truckListView.setAdapter(arrayAdapter);

                        truckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Toast.makeText(TruckListPage.this, "Selected Truck: " + truckListFromJson.get(i).getId(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(TruckListPage.this, DetailedTruck.class);
                                intent.putExtra("Selected Truck", truckListFromJson.get(i).getId());
                                startActivity(intent);
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void navigateToMainPage(View view) {
        Intent intent = new Intent(TruckListPage.this, navigation_page.class);
//        intent.putExtra("User", finalResponse);
        startActivity(intent);
    }
}