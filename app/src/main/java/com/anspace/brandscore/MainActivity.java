package com.anspace.brandscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.anspace.brandscore.Adapter.CategoryAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<CategoryCard> categoryList;
    CategoryAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference reference;
    public  String id;
    Dialog welcome;
    Dialog share;
    CollapsingToolbarLayout toolbarLayout;
    String name;
    TextView indian_percent;
    TextView foreign_percent;
    TextView indian_score;
    TextView foreign_score;
    TextView ind_per;
    TextView frn_per;
    TextView ind_score;
    TextView frn_score;
    public  ArrayList<String> catList  = new ArrayList<>();
    AppBarLayout collapsingToolbarLayout;
    Uri uri;
    RelativeLayout share_layout;
    String package_name = "";
    Dialog instructions;
    public static String link;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onStart() {
        super.onStart();

        if (!isOnline()) {
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please Connect to Internet and try again")
                    .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else{

                boolean isFirstRun = getSharedPreferences("FirstRun", MODE_PRIVATE).getBoolean("isFirstRun", true);
                if (isFirstRun){
                    showPopup();
                    getSharedPreferences("FirstRun", MODE_PRIVATE)
                            .edit()
                            .putBoolean("isFirstRun", false)
                            .apply();
                }
                super.onStart();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryList = new ArrayList<>();
        welcome = new Dialog(this);
        share = new Dialog(this);
        toolbarLayout = findViewById(R.id.coll_toolbar);
        indian_percent = findViewById(R.id.indian_percent);
        indian_score = findViewById(R.id.indian_score);
        foreign_percent = findViewById(R.id.foreign_percent);
        foreign_score = findViewById(R.id.foreign_score);
        ind_per = findViewById(R.id.ind_per);
        ind_score = findViewById(R.id.ind_score);
        frn_score = findViewById(R.id.frn_score);
        frn_per = findViewById(R.id.frn_per);
        instructions = new Dialog(this);
        share_layout = findViewById(R.id.share_view);
        collapsingToolbarLayout = findViewById(R.id.appBarLayout);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);

        toolbarLayout.setExpandedTitleColor(Color.parseColor("#FFFFFF"));
        toolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        reference = FirebaseDatabase.getInstance().getReference("Categories");
        Query query = reference.orderByChild("r");

        shimmerFrameLayout.startShimmer();


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Category category = snapshot.getValue(Category.class);
                        assert category != null;
                        categoryList.add(new CategoryCard(category.getN(),"0/0"));
                        catList.add(category.getN());
                        }

                adapter = new CategoryAdapter(MainActivity.this, R.layout.layout_category, categoryList);
                recyclerView = findViewById(R.id.recycler_view);

                LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                setScore(catList);
                recyclerView.setAdapter(adapter);

                recyclerView.setVisibility(View.VISIBLE);

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareView(); }
        });
    }

    private void setScore(ArrayList<String> catList) {
        ArrayList<Integer> indianBrandsSelected = new ArrayList<>();
        ArrayList<Integer> foreignBrandsSelected = new ArrayList<>();
        float ind = 0;
        float frn = 0;
        int z = 0;

            for(int i = 0;i<catList.size();i++) {
                SharedPreferences sharedPref = this.getSharedPreferences(catList.get(i), Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPref.getString("savedData","");
                SavedData savedData;
                savedData = gson.fromJson(json,SavedData.class);
                if (!(savedData == null)){
                indianBrandsSelected.add(savedData.getBrandIdListI().size());
                foreignBrandsSelected.add(savedData.getBrandIdListF().size());}
                z=z+1;
            }


            for (int i:indianBrandsSelected){
                ind = ind + i;
            }
            for (int f:foreignBrandsSelected){
                frn = frn + f;
            }

            indian_score.setText(""+(int)ind);
            foreign_score.setText(""+(int)frn);
            ind_score.setText(""+(int)ind);
            frn_score.setText(""+(int)frn);
            int iPer = (int) (ind/(ind+frn)*100);
            int fper = (int) (frn/(ind+frn)*100);
            indian_percent.setText(""+iPer+"%");
            foreign_percent.setText(""+fper+"%");
            ind_per.setText(""+iPer+"%");
            frn_per.setText(""+fper+"%");
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        FirebaseDatabase.getInstance().getReference("Permissions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Permissions permissions = dataSnapshot.getValue(Permissions.class);
                if (permissions.getCategory().equals("y")){
                    getMenuInflater().inflate(R.menu.add_menu, menu);
                    MenuItem searchItem = menu.findItem(R.id.add);
                }
                link = permissions.getLink();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        getMenuInflater().inflate(R.menu.options_menu,menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add: {
                Button add;
                welcome.setContentView(R.layout.popup_welcome);
                add = welcome.findViewById(R.id.addCat);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText cat;
                        cat = welcome.findViewById(R.id.category_name);
                        name = cat.getText().toString();
                        if (TextUtils.isEmpty(name))
                            Toast.makeText(MainActivity.this, "Name is Required", Toast.LENGTH_SHORT).show();
                        else
                            addCat(name);
                        welcome.dismiss();
                    }
                });
                welcome.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                welcome.show();
            }break;
            case R.id.purpose: {
                startActivity(new Intent(MainActivity.this,PurposeActivity.class));
            }break;
            case R.id.share: {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Support India by using Indian Brands\nCheck your Brand Score at:\n https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                startActivity(Intent.createChooser(intent, "Share App using"));
            }break;
            case R.id.contact: {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:nichiketpatil550@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }break;
        }

        return true;
    }

    private void shareView() {
        ImageView imageView;
        final String link = "Defeat Chinese and other foreign brands by using Indian Brands. Get to know about them\nDownload the app: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

        share.setContentView(R.layout.layout_share);
        final ShareView shareView = new ShareView();
        uri = shareView.shareView(share_layout,getApplicationContext(),MainActivity.this,"");
        imageView = share.findViewById(R.id.shared_image);
        imageView.setImageURI(uri);
        share.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        share.show();

        final Intent emailIntent1 = new Intent(Intent.ACTION_SEND);
        emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent1.putExtra(Intent.EXTRA_EMAIL, new String[]{});

        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "[" + "BRAND_SCORE" + "]");
        emailIntent1.putExtra(Intent.EXTRA_TEXT, link);
        emailIntent1.setType("image/jpg");

        share.findViewById(R.id.whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                package_name = "com.whatsapp";
                Uri uri2 = shareView.shareView(share.findViewById(R.id.share_card),getApplicationContext(),MainActivity.this,link);
                emailIntent1.putExtra(Intent.EXTRA_STREAM, uri2);
                emailIntent1.setPackage(package_name);
                startActivity(Intent.createChooser(emailIntent1, "Share Brand Score using"));
            }
        });

        share.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                package_name = "com.facebook.katana";
                Uri uri2 = shareView.shareView(share.findViewById(R.id.share_card),getApplicationContext(),MainActivity.this,link);
                emailIntent1.putExtra(Intent.EXTRA_STREAM, uri2);
                emailIntent1.setPackage(package_name);
                startActivity(Intent.createChooser(emailIntent1, "Share Brand Score using"));
            }
        });

        share.findViewById(R.id.instagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                package_name = "com.instagram.android";
                Uri uri2 = shareView.shareView(share.findViewById(R.id.share_card),getApplicationContext(),MainActivity.this,link);
                emailIntent1.putExtra(Intent.EXTRA_STREAM, uri2);
                emailIntent1.setPackage(package_name);
                startActivity(Intent.createChooser(emailIntent1, "Share Brand Score using"));
            }
        });

    }

    private  void  addCat(String name){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("n",name);
        hashMap.put("r",0);
        reference.child(name).setValue(hashMap);
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        setScore(catList);
        if (!(adapter==null)){
        adapter.notifyDataSetChanged();
        runLayoutAnimation(recyclerView);}
        super.onResume();
        //Refresh your stuff here
    }


    private void showPopup() {
        instructions.setContentView(R.layout.layout_instructions);
        instructions.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        instructions.show();
        TextView close = instructions.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructions.dismiss();
            }
        });
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }


}