//package com.example.brandscore.Adapter;
//
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.example.brandscore.BrandCard;
//import com.example.brandscore.R;
//
//import java.util.List;
//
//public class ListAdapter extends ArrayAdapter<BrandCard> {
//
//    private int resourceLayout;
//    private Context mContext;
//
//    public ListAdapter(Context context, int resource, List<BrandCard> items) {
//        super(context, resource, items);
//        this.resourceLayout = resource;
//        this.mContext = context;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View v = convertView;
//
//        if (v == null) {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(mContext);
//            v = vi.inflate(resourceLayout, null);
//        }
//
//        BrandCard p = getItem(position);
//
//        if (p != null) {
//            TextView tt1 = v.findViewById(R.id.title);
//            TextView tt2 = v.findViewById(R.id.users_no);
//
//            if (tt1 != null) {
//                tt1.setText(p.getTitle());
//            }
//
//            if (tt2 != null) {
//                tt2.setText(p.getUserNo());
//            }
//        }
//
//        return v;
//    }
//
//}