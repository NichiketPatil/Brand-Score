package com.anspace.brandscore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.anspace.brandscore.BrandsActivity;
import com.anspace.brandscore.CategoryCard;
import com.anspace.brandscore.R;
import com.anspace.brandscore.SavedData;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<CategoryCard> objects;
    int resource = 0;
    public boolean isShimmer = true;

    public CategoryAdapter(Context context, int resource, ArrayList<CategoryCard> objects) {
        this.mContext = context;
        this.objects = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        //get the persons information
        int ind = 0;
        int frn = 0;
        final String title = objects.get(position).getTitle();
        holder.title.setText(title);

        SharedPreferences sharedPref = mContext.getSharedPreferences(title, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("savedData", "");
        SavedData savedData;
        savedData = gson.fromJson(json, SavedData.class);
        if (!(savedData==null)){
            ind = savedData.getBrandIdListI().size();
            frn = savedData.getBrandIdListF().size();}

        holder.catScore.setText(""+ind+"/"+frn);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BrandsActivity.class);
                intent.putExtra("title", title);
//                    Pair[] pair = new Pair[1];
//                    pair[0] = new Pair<View, String>(holder.view, "shared_view");
//                    ActivityOptions options = ActivityOptions
//                            .makeSceneTransitionAnimation((Activity) mContext,pair);
//                    mContext.startActivity(intent,options.
                mContext.startActivity(intent);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView catScore;
        CardView card_view;
        View view;
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category);
            card_view = itemView.findViewById(R.id.card_category);
            view = itemView.findViewById(R.id.view);
            catScore = itemView.findViewById(R.id.cat_score);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
        }
    }


}
