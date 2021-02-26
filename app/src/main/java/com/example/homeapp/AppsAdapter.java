package com.example.homeapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> implements Filterable {

    Context myContext;
    List<String> stringList;
//    private List<String> orig;
    private ArrayList<String> items;
    private final List<String> userList;

    private final List<String> filteredUserList;
    ApkInfoExtractor apkInfoExtractor;
    private UserFilter userFilter;

    public AppsAdapter(Context context, List<String> list) {

        myContext = context;
        this.stringList = list;
        this.filteredUserList = new ArrayList<>();
         apkInfoExtractor = new ApkInfoExtractor(myContext);
        items = new ArrayList<>();
       this.userList=apkInfoExtractor.GetAllInstalledApkName();
        Collections.sort(stringList, String::compareToIgnoreCase);
    }

    public void setFilter(List<String> filteredDataList) {
        stringList = filteredDataList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(userFilter == null)
            userFilter = new UserFilter(this, stringList);
        return userFilter;
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                final FilterResults oReturn = new FilterResults();
//                final List<String> results = new ArrayList<String>();
//                if (orig == null)
//                    orig  = items;
//                if (constraint != null){
//                    if(orig !=null & orig.size()>0 ){
//                        for ( final String g :orig) {
//                            if (g.contains(constraint.toString()))
//                                results.add(g);
//                        }
//                    }
//                    oReturn.values = results;
//                }
//                return oReturn;
            }

//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                items = (ArrayList<String>)results.values;
//                notifyDataSetChanged();
//
//            }





    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView imageView;
        public TextView textView_App_Name;
        public TextView textView_App_Package_Name;
        public TextView textView_App_MainActivity;
        public TextView textView_App_versionName;
        public TextView textView_App_VersionCode;


        public ViewHolder(View view) {

            super(view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            imageView = (ImageView) view.findViewById(R.id.imageview);
            textView_App_Name = (TextView) view.findViewById(R.id.Apk_Name);
            textView_App_Package_Name = (TextView) view.findViewById(R.id.Apk_Package_Name);
            textView_App_MainActivity = (TextView) view.findViewById(R.id.Apk_mainclass);
            textView_App_versionName = (TextView) view.findViewById(R.id.Apk_versionname);
            textView_App_VersionCode = (TextView) view.findViewById(R.id.Apk_versioncode);
        }
    }

    @Override
    public AppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view2 = LayoutInflater.from(myContext).inflate(R.layout.cardview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view2);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {



        final String ApplicationPackageName = (String) stringList.get(position);
        String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);

        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);
        String mainActivityName = apkInfoExtractor.GetMainActivity(ApplicationPackageName);
        String ApplicationVersionName = apkInfoExtractor.GetVersionName(ApplicationPackageName).versionName;
        String ApplicationVersionCode = String.valueOf(apkInfoExtractor.GetVersionName(ApplicationPackageName).versionCode);

        viewHolder.textView_App_Name.setText(ApplicationLabelName);

        viewHolder.textView_App_Package_Name.setText(ApplicationPackageName);

        viewHolder.imageView.setImageDrawable(drawable);
        viewHolder.textView_App_MainActivity.setText(mainActivityName);
        viewHolder.textView_App_versionName.setText(new StringBuilder().append("Version Name: ").append(ApplicationVersionName));
        viewHolder.textView_App_VersionCode.setText(new StringBuilder().append("Version Code: ").append(ApplicationVersionCode));

        //Adding click listener on CardView to open clicked application directly from here .
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = myContext.getPackageManager().getLaunchIntentForPackage(ApplicationPackageName);
                if (intent != null) {

                    myContext.startActivity(intent);

                } else {

                    Toast.makeText(myContext, ApplicationPackageName + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return stringList.size();
    }
    private static class UserFilter extends Filter {

        private final AppsAdapter adapter;

        private final List<String> originalList;

        private final List<String> filteredList;

        private UserFilter(AppsAdapter adapter, List<String> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final String s : originalList) {
                    if (s.endsWith(filterPattern)) {
                        filteredList.add(s);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.stringList.clear();
            adapter.stringList.addAll((ArrayList<String>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}



