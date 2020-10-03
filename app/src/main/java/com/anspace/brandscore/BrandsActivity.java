package com.anspace.brandscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import com.anspace.brandscore.Adapter.BrandAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;

public class BrandsActivity extends AppCompatActivity {

    public static ArrayList<BrandCard> brandListI;
    public static ArrayList<BrandCard> brandListF;
    BrandAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    public String title;
    DatabaseReference referenceI;
    DatabaseReference reference;
    DatabaseReference referenceF;
    Query queryI;
    Query queryF;
    FloatingActionButton add;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);
        title = getIntent().getStringExtra("title");
        add = findViewById(R.id.add);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        reference = FirebaseDatabase.getInstance().getReference("Brands");
        referenceI = reference.child(title).child("i");
        referenceF = reference.child(title).child("f");
//        queryI = referenceI.orderByChild("n");
//        queryF = referenceI.orderByChild("n");
        brandListI = new ArrayList<>();
        brandListF = new ArrayList<>();

        shimmerFrameLayout.startShimmer();

        referenceI.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Brand brand = snapshot.getValue(Brand.class);
                    assert brand != null;
                    brandListI.add(new BrandCard(brand.getT(),brand.getN(),brand.getC()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        referenceF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Brand brand = snapshot.getValue(Brand.class);
                    assert brand != null;
                    brandListF.add(new BrandCard(brand.getT(),brand.getN(),brand.getC()));
                }
                adapter = new BrandAdapter(BrandsActivity.this, R.layout.brand_card, brandListI,brandListF,title);
                recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(BrandsActivity.this,2));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
                shimmerFrameLayout.stopShimmer();
                recyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        FirebaseDatabase.getInstance().getReference("Permissions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Permissions permissions = dataSnapshot.getValue(Permissions.class);
                        if (permissions.getBrand().equals("y"))
                            add.setVisibility(View.VISIBLE);
                        else
                            add.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrandsActivity.this,AddingActivity.class);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation2);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }
}