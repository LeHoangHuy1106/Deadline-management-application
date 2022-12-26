package com.example.excercise_lab10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    TextView tvTong, tvTreHan, tvDungHan,tvDangThucHien;
    private int slgTong = 0, slgTreHan=0, slgDungHan=0, slgDangThucHien=0;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        control();
        init();
        getSoLuong();



    }
    void createChar()
    {

        PieChart mChart;

        mChart = (PieChart) findViewById(R.id.piechart);
        mChart.setRotationEnabled(true);

        mChart.setHoleRadius(35f);
        mChart.setTransparentCircleAlpha(0);
        mChart.setCenterText(""+slgTong);
        mChart.setCenterTextSize(10);
        mChart.setDrawEntryLabels(true);
        addDataSet(mChart);




    }

    private void print() {
        tvTreHan.setText(slgTreHan+"");
        tvDangThucHien.setText(slgDangThucHien+"");
        tvDungHan.setText(""+slgDungHan);
        tvTong.setText(""+slgTong);

    }
     void addDataSet(PieChart pieChart) {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        int[] yData = {slgDangThucHien,slgDungHan,slgTreHan };
        String[] xData = { "Đang Thực hiện", "Đúng hạn", "Trễ hạn" };

        for (int i = 0; i < yData.length;i++){
            yEntrys.add(new PieEntry(yData[i],i));
        }
        for (int i = 0; i < xData.length;i++){
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet=new PieDataSet(yEntrys,"Employee Sales");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.YELLOW);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);

        pieDataSet.setColors(colors);

        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }



    void getSoLuong()
    {
        DatabaseReference myDeadlne = database.getReference("DanhSachDeadline");
        myDeadlne.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                slgDangThucHien=0;
                slgDungHan=0;
                slgTreHan=0;
                slgTong=0;

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    deadline dl= dataSnapshot.getValue(deadline.class);
                    if(dl.getTrangThai().equals("Đang thực hiện"))
                    {
                        slgDangThucHien+=1;
                    }
                    if(dl.getTrangThai().equals("Đúng hạn"))
                    {
                        slgDungHan+=1;
                    }
                    if(dl.getTrangThai().equals("Trễ hạn"))
                    {
                        slgTreHan+=1;
                    }
                    slgTong+=1;


                }
                createChar();
                print();

            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

    }
    void init()
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        tvTong = findViewById(R.id.tvTong);
        tvDungHan= findViewById(R.id.tvTKDungHan);
        tvDangThucHien= findViewById(R.id.tvTKDangThucHien);
        tvTreHan= findViewById(R.id.tvTKTreHan);
    }
    void control() {
        // ánh xạ menubottomNAV
        BottomNavigationView bottomNavigationView = findViewById(R.id.bvfiler);
        // mặc định chọn
        bottomNavigationView.setSelectedItemId(R.id.itemfilter);
        // sự kiện chọn item nav
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemAdd:
                        Intent z = new Intent(getApplicationContext(), AddDeadlineActivity.class);

                        startActivity(z);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.itemList:
                        Intent i = new Intent(getApplicationContext(), DeadlineActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.itemfilter:
                        return true;

                }


                return false;
            }
        });
    }



}