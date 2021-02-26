package com.example.homeapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ApkInfoExtractor {

    Context context1;

    public ApkInfoExtractor(Context context2) {

        context1 = context2;
    }

    public List<String> GetAllInstalledApkInfo() {

        List<String> ApkPackageName = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        List<ResolveInfo> resolveInfoList = context1.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {

            ActivityInfo activityInfo = resolveInfo.activityInfo;

            if (!isSystemPackage(resolveInfo)) {

                ApkPackageName.add(activityInfo.applicationInfo.packageName);
            }
        }
        return ApkPackageName;

    }

    public List<String> GetAllInstalledApkName() {

        List<String> ApkName = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        List<ResolveInfo> resolveInfoList = context1.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {

            ActivityInfo activityInfo = resolveInfo.activityInfo;

            if (!isSystemPackage(resolveInfo)) {
                ApplicationInfo applicationInfo;
                PackageManager packageManager = context1.getPackageManager();

                try {

                    applicationInfo = packageManager.getApplicationInfo(activityInfo.applicationInfo.packageName, 0);

                    if (applicationInfo != null) {

                        String Name = (String) packageManager.getApplicationLabel(applicationInfo);
                        ApkName.add(Name);
                    }

                } catch (PackageManager.NameNotFoundException e) {

                    e.printStackTrace();
                }
            }
        }
        return ApkName;
    }

    public boolean isSystemPackage(ResolveInfo resolveInfo) {

        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public Drawable getAppIconByPackageName(String ApkTempPackageName) {

        Drawable drawable;

        try {
            drawable = context1.getPackageManager().getApplicationIcon(ApkTempPackageName);

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            drawable = ContextCompat.getDrawable(context1, R.mipmap.ic_launcher);
        }
        return drawable;
    }

    public String GetAppName(String ApkPackageName) {

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = context1.getPackageManager();

        try {

            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);

            if (applicationInfo != null) {

                Name = (String) packageManager.getApplicationLabel(applicationInfo);
            }

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return Name;
    }

    public PackageInfo GetVersionName(String ApkPackageName) {
        PackageInfo packageInfo = null;

        try {
            packageInfo = context1.getPackageManager().getPackageInfo(ApkPackageName, 0);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    public String GetMainActivity(String ApkPackageName) {
        String className = null;
        try {
            Intent launchIntent = context1.getPackageManager().getLaunchIntentForPackage(ApkPackageName);
            className = launchIntent.getComponent().getClassName();
            return className;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return className;
    }
}