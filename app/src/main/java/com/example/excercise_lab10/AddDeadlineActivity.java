package com.example.excercise_lab10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class AddDeadlineActivity extends AppCompatActivity {

    Spinner spinnerMonHoc;
    TextView edtNgayGiao,edtHanNop;
    EditText edtNoiDung;
    Button btnTOk,btnHuy;
    ImageButton imgbtnMonHoc;
    String textMonhoc= "";
    FirebaseDatabase database;
    DatabaseReference myRef;
    deadline deadline;
    int maxid;
    static ArrayList<String> arrayMonHoc = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_activtity);
        init();
        empty();
        control();
        CreateSpinner();
        addMonHoc();
        getMaxid();
        chonNgay(edtNgayGiao);
        chonNgay(edtHanNop);
        btnTOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadline= new deadline(textMonhoc,edtNgayGiao.getText().toString(),
                        edtHanNop.getText().toString(),
                        edtNoiDung.getText().toString(),
                        "Đang thực hiện",maxid+1);
                try {
                    passProductAsResult(deadline);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } ;


        });

    }
    private void empty()
    {
            spinnerMonHoc.setSelection(0);
            edtNgayGiao.setText("01/01/0001");
            edtHanNop.setText( "01/01/0001");
            edtNoiDung.setText("");
    }

    void getMaxid()
    {
        DatabaseReference myMaxID = database.getReference("maxid");
        myMaxID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                maxid= snapshot.getValue(int.class);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    void passProductAsResult(deadline dl) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateGiao = formatter.parse(edtNgayGiao.getText().toString());
        Date dateHan= formatter.parse(edtHanNop.getText().toString());

        if(textMonhoc==arrayMonHoc.get(0))
        {
            Toast.makeText(this,"Bạn chưa nhập môn học",Toast.LENGTH_SHORT).show();
        }
        else if(edtNgayGiao.getText()=="01/01/0001")
        {
            Toast.makeText(this,"Bạn chưa nhập ngày giao",Toast.LENGTH_SHORT).show();
        }
        else if(edtHanNop.getText()=="01/01/0001")
        {
            Toast.makeText(this,"Bạn chưa nhập hạn nộp",Toast.LENGTH_SHORT).show();
        }
        else  if(edtNoiDung.getText().length()==0)
        {
            Toast.makeText(this,"Bạn chưa nhập nội dung",Toast.LENGTH_SHORT).show();
        }
        ///    else if (calenderGiao?.time!! > calenderHan?.time )
        else if ((dateGiao.after(dateHan)))
        {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(dateGiao);
            cal2.setTime(dateHan);
            Toast.makeText(this,"Ngày giao phải trước ngày nhân",Toast.LENGTH_SHORT).show();
        }
        else {
            database.getReference().child("maxid").setValue(maxid+1);
            myRef = database.getReference("DanhSachDeadline");
            myRef.child(String.valueOf(maxid+1)).setValue(dl);
            Toast.makeText(this,"Thêm thành công!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void addMonHoc() {
        imgbtnMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(AddDeadlineActivity.this);
                dialog.setTitle("Môn Học bạn muốn thêm là:");
                final EditText input = new EditText(AddDeadlineActivity.this);
                dialog.setView(input);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myRef.push().setValue(input.getText().toString());
                        Toast.makeText(AddDeadlineActivity.this,input.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();


            }
        });
    }
    void chonNgay( TextView edt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        edt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(AddDeadlineActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        c.set(year, month,dayOfMonth);
                        edt.setText(simpleDateFormat.format(c.getTime()));
                    } ;
                }, year, month, day);
                dpd.show();
            }

        });
    }
    void CreateSpinner()
    {
        myRef = database.getReference("MonHoc");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (arrayMonHoc != null || arrayMonHoc.isEmpty()) {
                    arrayMonHoc.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String m = dataSnapshot.getValue(String.class);
                        arrayMonHoc.add(m);

                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(AddDeadlineActivity.this, android.R.layout.simple_spinner_item, arrayMonHoc);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMonHoc.setAdapter(arrayAdapter);
                    spinnerMonHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            textMonhoc= arrayMonHoc.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    void control() {
        // ánh xạ menubottomNAV
        BottomNavigationView bottomNavigationView = findViewById(R.id.bvmain);
        // mặc định chọn
        bottomNavigationView.setSelectedItemId(R.id.itemAdd);
        // sự kiện chọn item nav
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemAdd:
                        return true;

                    case R.id.itemList:
                        Intent i = new Intent(getApplicationContext(), DeadlineActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.itemfilter:
                        Intent z = new Intent(getApplicationContext(), FilterActivity.class);

                        startActivity(z);
                        overridePendingTransition(0, 0);
                        return true;
                }


                return false;
            }
        });
    }






    void init(){

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        spinnerMonHoc= findViewById(R.id.spinnerMonHoc);
        edtNgayGiao=findViewById(R.id.edtNgayGiao);
        edtHanNop= findViewById(R.id.edtHanNop);
        edtNoiDung= findViewById(R.id.edtNoiDung);
        btnTOk= findViewById(R.id.btnThem);
        btnHuy= findViewById(R.id.btnhuy);
        imgbtnMonHoc= findViewById(R.id.imgbtnMonHoc);

    }
}
