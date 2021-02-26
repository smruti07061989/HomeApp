package com.example.homeapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    Context myContext;
    List<String> stringList;

    public AppsAdapter(Context context, List<String> list) {

        myContext = context;
        this.stringList = list;
        Collections.sort(stringList,String::compareToIgnoreCase);
    }

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

        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(myContext);

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
}
