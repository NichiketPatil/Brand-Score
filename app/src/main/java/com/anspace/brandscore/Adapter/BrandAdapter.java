package com.anspace.brandscore.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anspace.brandscore.Brand;
import com.anspace.brandscore.BrandCard;
import com.anspace.brandscore.Permissions;
import com.anspace.brandscore.R;
import com.anspace.brandscore.SavedData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<BrandCard> objects;
    private ArrayList<BrandCard> objectsF;
    int resource= 0;
    public String titleF;
    String usersNo;
    String usersNoF;
    public String title;
    String chineseF;
    String catId;
    ArrayList<String> checkedBrandListI = new ArrayList<>();
    ArrayList<String> checkedBrandListF = new ArrayList<>();
    String checkedCat;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Brands");
    TextView cntry;

    public  BrandAdapter(Context context, int resource, ArrayList<BrandCard> objects,ArrayList<BrandCard> objectsF,String catId){
        this.mContext = context;
        this.objects = objects;
        this.objectsF = objectsF;
        this.resource = resource;
        this.catId = catId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resource,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (objects.size()>objectsF.size())
            return 2*objects.size();
        else
            return 2*objectsF.size();
    }

    @Override
    public long getItemId(int position) {
        return position*2;
    }

    @Override
    public int getItemViewType(int position) {
        return position*2;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //get the personal information

         SavedData savedData = new SavedData();
        savedData = loadData(catId);
        cntry = holder.country;

        try {
            title = "%";
            titleF = "%";
            chineseF = "";
            if (objects.size()>position/2){
                title = objects.get(position/2).getT();
                usersNo = objects.get(position/2).getN();
            }

            if (objectsF.size()>position/2){
                titleF = objectsF.get(position/2).getT();
                usersNoF = objectsF.get(position/2).getN();
                chineseF = objectsF.get(position/2).getC();
            }

            if (position % 2 == 0){
                holder.title.setText(title);
                holder.usersNo.setText(usersNo);
                holder.country.setText("INDIA");
                holder.country.setTextColor(Color.parseColor("#00B109"));
                if (savedData.getBrandIdListI().contains(title)){
                    holder.check.setChecked(true);
                }
                else
                    holder.check.setChecked(false);
               }
            else{
                holder.title.setText(titleF);
                holder.usersNo.setText(usersNoF);
                if (chineseF.equals("y")){
                    holder.country.setText("CHINA");
                    holder.country.setTextColor(Color.parseColor("#DF0000"));}
                else if (chineseF.equals("n")){
                    holder.country.setText("");
                }
                else{
                    holder.country.setTextColor(Color.parseColor("#000000"));
                    holder.country.setText(chineseF);
                }
                if (savedData.getBrandIdListF().contains(titleF))
                    holder.check.setChecked(true);
                else
                    holder.check.setChecked(false);
            }

            holder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = holder.title.getText().toString();

                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.card_description);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    final TextView desc = dialog.findViewById(R.id.desc);
                    final TextView country = dialog.findViewById(R.id.country);
                    final TextView brand = dialog.findViewById(R.id.brand);
                    final TextView edit = dialog.findViewById(R.id.edit);
                    final TextView save = dialog.findViewById(R.id.save);
                    final ProgressBar progress = dialog.findViewById(R.id.progress);
                    final TextView cancel = dialog.findViewById(R.id.cancel);
                    final EditText editCountry = dialog.findViewById(R.id.edit_country);
                    final EditText editDesc = dialog.findViewById(R.id.edit_desc);
                    final LinearLayout saveLayout = dialog.findViewById(R.id.save_layout);
                    desc.setMovementMethod(new ScrollingMovementMethod());
                    String countryType;
                    if (position%2==0)
                        countryType = "i";
                    else
                        countryType = "f";

                    final DatabaseReference reference2 = reference.child(catId).child(countryType).child(title);
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Brand brand1  = dataSnapshot.getValue(Brand.class);
                            try {
                                desc.setText(brand1.getD());
                                country.setText(brand1.getC());
                                editDesc.setText(brand1.getD());
                                editCountry.setText(brand1.getC());}
                            catch (Exception ignored) {}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Permissions");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Permissions permissions = dataSnapshot.getValue(Permissions.class);
                            if (permissions.getDesc().equals("y"))
                                edit.setVisibility(View.VISIBLE);
                            else
                                edit.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });


                    brand.setText(title);

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            desc.setVisibility(View.GONE);
                            country.setVisibility(View.GONE);
                            editDesc.setVisibility(View.VISIBLE);
                            editCountry.setVisibility(View.VISIBLE);
                            saveLayout.setVisibility(View.VISIBLE);
                            edit.setVisibility(View.GONE);
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progress.setVisibility(View.VISIBLE);
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("c",editCountry.getText().toString());
                            hashMap.put("d",editDesc.getText().toString());
                            reference2.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progress.setVisibility(View.GONE);
                                        Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
                                        holder.country.setText(editCountry.getText().toString());
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }

        catch (Exception ignored){}

//        holder.title.setText(title);
 //       holder.usersNo.setText(no);

        if (holder.title.getText().toString().equals("%")){
            holder.check.setVisibility(View.INVISIBLE);
            holder.usersNo.setVisibility(View.INVISIBLE);
            holder.title.setVisibility(View.INVISIBLE);
            holder.userText.setVisibility(View.INVISIBLE);
            holder.country.setVisibility(View.INVISIBLE);
        }

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String country = "";
                String title1 = "";
                title1 = holder.title.getText().toString();
                String no  = holder.usersNo.getText().toString();

                if (position%2==0)
                    country = "i";
                else
                    country = "f";
                try {
                for(String cat : loadData(catId).getBrandIdListI()) {
                    if (!checkedBrandListI.contains(cat))
                        checkedBrandListI.add(cat);
                }

                for(String cat : loadData(catId).getBrandIdListF()) {
                    if (!checkedBrandListF.contains(cat))
                        checkedBrandListF.add(cat);
                }
                }
                catch (Exception ignored){}

                DatabaseReference reference1=reference.child(catId).child(country).child(title1);
                String updatedNo ;

                checkedCat = catId;

                if (position%2==0){
                        if (isChecked){
                            updatedNo = Integer.toString (Integer.parseInt(no)+1);
                            if (!checkedBrandListI.contains(title1))
                                    checkedBrandListI.add(title1);
                        }
                        else {
                            checkedBrandListI.remove(title1);
                            updatedNo = Integer.toString(Integer.parseInt(no)-1);
                        }
                }
                else {
                    if (isChecked){
                        updatedNo = Integer.toString (Integer.parseInt(no)+1);
                        if (!checkedBrandListF.contains(title1))
                            checkedBrandListF.add(title1);
                    }
                    else{
                        updatedNo = Integer.toString(Integer.parseInt(no)-1);
                        checkedBrandListF.remove(title1);}
                }
                saveData(checkedBrandListI,checkedBrandListF,catId);

                holder.usersNo.setText(updatedNo);

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("n",updatedNo);
                reference1.updateChildren(hashMap);
            }

        });


    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView title;
        TextView usersNo;
        TextView userText;
        CardView card_view;
        CheckBox check;
        TextView country;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            userText = itemView.findViewById(R.id.text_user);
            usersNo = itemView.findViewById(R.id.users_no);
            check = itemView.findViewById(R.id.check);
            card_view = itemView.findViewById(R.id.card_view);
            country = itemView.findViewById(R.id.country);
        }
    }

    private void saveData(ArrayList<String> checkedBrandListI,ArrayList<String> checkedBrandListF, String catId) {

        SharedPreferences sharedPref = mContext.getSharedPreferences(catId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        SavedData savedData = new SavedData();
        savedData.setBrandIdListI(checkedBrandListI);
        savedData.setBrandIdListF(checkedBrandListF);
        savedData.setCategory(catId);

        Gson gson = new Gson();
        String json = gson.toJson(savedData);
        editor.putString("savedData",json);
        editor.apply();
    }

    private SavedData loadData(String catId){
        SharedPreferences sharedPref = mContext.getSharedPreferences(catId, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("savedData","");
        SavedData savedData;
        savedData = gson.fromJson(json,SavedData.class);
        return savedData;
    }
}
