package com.anspace.brandscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddingActivity extends AppCompatActivity {

    DatabaseReference reference;
    String title;
    TextView category;
    EditText brand;
    String country;
    String china;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);

        title = getIntent().getStringExtra("title");
        brand  = findViewById(R.id.brand);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        china = "n";
        category = findViewById(R.id.category);
        category.setText(title);

        ((RadioGroup)findViewById(R.id.country)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.indian_percent:findViewById(R.id.china).setVisibility(View.GONE);country = "i";break;
                        case R.id.foreign_percent:findViewById(R.id.china).setVisibility(View.VISIBLE);country = "f";break;
                    }
            }
        });


        ((RadioGroup)findViewById(R.id.chinese)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.no:china = "n";break;
                        case R.id.yes:china = "y";break;
                    }
            }
        });



        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String brandName = brand.getText().toString();

                if (TextUtils.isEmpty(brandName)||TextUtils.isEmpty(country)){
                    Toast.makeText(AddingActivity.this,"All fields are reqired",Toast.LENGTH_SHORT).show();}
                else if (country.equals("f")&&TextUtils.isEmpty(china)){
                    Toast.makeText(AddingActivity.this, "Select Chinese or Not", Toast.LENGTH_SHORT).show(); }
                else
                    add(brandName,china);

            }
        });
    }

    private  void add(String brandName,String china){
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Brands").child(title);
        HashMap<String,Object> hashMap = new HashMap<>();
        DatabaseReference newRef = reference.child(country).child(brandName);
        hashMap.put("t",brandName);
        hashMap.put("c",china);
        hashMap.put("n","0");

        newRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    finish();
                }
                else{
                    Toast.makeText(AddingActivity.this,"Please Contact Developer\n Maybe Duplicate Values",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}