package com.example.excercise_lab10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DeadlineActivity extends AppCompatActivity {

    DanhSachDeadline arrayDeadline= new DanhSachDeadline();

    String TrangThai="",MonHoc="";
    Spinner snMonHoc,snTrangThai;
    customAdapter adapter;
    RecyclerView rvDeadline;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> arrayMonHoc;
    private  int slgDeadline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline);
        control();
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference();
        init();
        createSN();
        rvDeadline.setLayoutManager(new LinearLayoutManager(DeadlineActivity.this));

        adapter= new customAdapter(DeadlineActivity.this,arrayDeadline);
        rvDeadline.setAdapter(adapter);





    }
    void ThongBao( String hannop) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar currentTime = Calendar.getInstance();
        Calendar han = Calendar.getInstance();
        Date dateHan = null;
        try {
            dateHan = formatter.parse(hannop);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        han.setTime(dateHan);
        if (han.getTime().getDate()== currentTime.getTime().getDate()) {
          slgDeadline+=1;


        }
    }
    void DialogThongBao(int i)
    {
        //T???o ?????i t?????ng
    AlertDialog.Builder b = new AlertDialog.Builder(this);
    //Thi???t l???p ti??u ?????
            b.setTitle("TH??NG B??O");
            b.setMessage("H??M NAY B???N C?? "+i+" DEALINE");

            b.setNegativeButton(" OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    });
    AlertDialog al = b.create();
            al.show();
    }


    void inItDeadline()
    {
        DatabaseReference myDeadlne = database.getReference("DanhSachDeadline");
        myDeadlne.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if(arrayDeadline!=null||!arrayDeadline.listDL.isEmpty())
                {
                    slgDeadline=0;
                    if( TrangThai =="T???t C???" &&(MonHoc==arrayMonHoc.get(0)))
                    {
                        arrayDeadline.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            deadline dl= dataSnapshot.getValue(deadline.class);
                            arrayDeadline.add(dl);
                            ThongBao(dl.getHanNop());


                        }
                        if(slgDeadline>0)
                        {
                            DialogThongBao(slgDeadline);
                        }

                        adapter.notifyDataSetChanged();

                    }
                    if( TrangThai =="T???t C???" &&!(MonHoc.equals(arrayMonHoc.get(0))))
                    {

                        arrayDeadline.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            deadline dl= dataSnapshot.getValue(deadline.class);
                            if(dl.getMonHoc().equals(MonHoc)) {
                                arrayDeadline.add(dl);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if( (TrangThai!= "T???t C???")&& (MonHoc.equals(arrayMonHoc.get(0))))
                    {

                        arrayDeadline.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            deadline dl= dataSnapshot.getValue(deadline.class);
                            if(dl.getTrangThai().equals(TrangThai))
                                arrayDeadline.add(dl);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if( !(TrangThai=="T???t C???") && !(MonHoc.equals(arrayMonHoc.get(0))))
                    {

                        arrayDeadline.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            deadline dl= dataSnapshot.getValue(deadline.class);
                            if (dl.getMonHoc().equals(MonHoc)&& dl.getTrangThai().equals(TrangThai))
                                arrayDeadline.add(dl);
                        }
                        adapter.notifyDataSetChanged();
                    }



                }


            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case  R.id.iconThem:

                Intent i = new Intent(DeadlineActivity.this,AddDeadlineActivity.class);
                startActivityForResult(i,123);

        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
         AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 123:
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Calendar currentTime  = Calendar.getInstance();
                Calendar han  = Calendar.getInstance();

                Date dateHan = null;
                try {
                    dateHan = formatter.parse(arrayDeadline.get(item.getGroupId()).getHanNop().toString());
                    han.setTime(dateHan);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (currentTime.after(han)) {
                    Toast.makeText(this, "tr??? h???n", Toast.LENGTH_SHORT).show();
                    arrayDeadline.get(item.getGroupId()).setTrangThai("Tr??? h???n");
                    adapter.notifyDataSetChanged();
                    myRef.child("DanhSachDeadline").child(String.valueOf(arrayDeadline.get(item.getGroupId()).getStt())).setValue(arrayDeadline.get(item.getGroupId()));
                } else {
                    Toast.makeText(this, "????ng h???n", Toast.LENGTH_SHORT).show();
                    arrayDeadline.get(item.getGroupId()).setTrangThai("????ng h???n");
                    adapter.notifyDataSetChanged();
                    myRef.child("DanhSachDeadline").child(String.valueOf(arrayDeadline.get(item.getGroupId()).getStt())).setValue(arrayDeadline.get(item.getGroupId()));
                }
                break;

            case 456:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeadlineActivity.this);
                alertDialogBuilder.setMessage("B??n c?? mu???n x??a s???n ph???m n??y!");
                alertDialogBuilder.setPositiveButton("C??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.getReference().child("DanhSachDeadline").child(String.valueOf(arrayDeadline.get(item.getGroupId()).getStt())).removeValue();

                    }
                });
                alertDialogBuilder.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //kh??ng l??m g??
                    }
                });
                alertDialogBuilder.show();
                Toast.makeText(this,"X??a th??nh c??ng",Toast.LENGTH_LONG).show();


                break;
            case 789:
                Intent i = new Intent(DeadlineActivity.this,EditDeadlineActivity.class);
                i.putExtra("deadline",arrayDeadline.get(item.getGroupId()));
                startActivityForResult(i,456);
                break;

        }
        return true;
    }


    void init()
    {
        rvDeadline=findViewById(R.id.rvdeadline);
        snMonHoc= findViewById(R.id.snMonHoc);
        snTrangThai=findViewById(R.id.snTrangThai);
    }

    void createSN()
    {
        ArrayList<String> arrayTrangThai= new ArrayList<String>();
        arrayTrangThai.add("T???t C???");
        arrayTrangThai.add("??ang th???c hi???n");
        arrayTrangThai.add("????ng h???n");
        arrayTrangThai.add("Tr??? h???n");
        ArrayAdapter arrayAdapterTrangThai= new ArrayAdapter(this, android.R.layout.simple_spinner_item,arrayTrangThai);
        arrayAdapterTrangThai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snTrangThai.setAdapter(arrayAdapterTrangThai);
        snTrangThai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TrangThai= arrayTrangThai.get(position);
                inItDeadline();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         arrayMonHoc= new ArrayList<String>();
        myRef.child("MonHoc").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
              if (arrayMonHoc != null || arrayMonHoc.isEmpty()) {
                  arrayMonHoc.clear();
                  for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                      String m = dataSnapshot.getValue(String.class);
                      arrayMonHoc.add(m);
                  }

                  ArrayAdapter arrayAdapter = new ArrayAdapter(DeadlineActivity.this, android.R.layout.simple_spinner_item, arrayMonHoc);
                  arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                  snMonHoc.setAdapter(arrayAdapter);

              }
          }

          @Override
          public void onCancelled(@NonNull @NotNull DatabaseError error) {

          }
      });


        snMonHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MonHoc= arrayMonHoc.get(position);
                inItDeadline();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    void control()
    {
        // ??nh x??? menubottomNAV
        BottomNavigationView bottomNavigationView= findViewById(R.id.bvmain);
        // m???c ?????nh ch???n
        bottomNavigationView.setSelectedItemId(R.id.itemList);
        // s??? ki???n ch???n item nav
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.itemAdd:
                        Intent i= new Intent(getApplicationContext(),AddDeadlineActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.itemList:
                        return true;
                    case R.id.itemfilter:
                        Intent z = new Intent(getApplicationContext(),FilterActivity.class);

                        startActivity(z);
                        overridePendingTransition(0,0);
                        return true;

                }


                return false;
            }
        });

    }


}