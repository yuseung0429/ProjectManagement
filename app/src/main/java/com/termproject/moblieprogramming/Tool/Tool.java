package com.termproject.moblieprogramming.Tool;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.termproject.moblieprogramming.Data.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Tool {
    static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference dr = database.getReference();
    static final FirebaseStorage storage = FirebaseStorage.getInstance("gs://moblieprogramming-bac07.appspot.com");
    public static final StorageReference sdr = storage.getReference();

    public static final String regex_id = "^[a-zA-Z0-9]{6,12}$";
    public static final String regex_pw = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
    public static final String regex_name = "^[가-힣]*$";
    public static final String regex_email = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    public static final String regex_phone = "^[0-9]{11}$";
    public static final String regex_num = "^[0-9]*$";

    public static Fragment alarmfragment;
    public static Fragment chatfragment;
    public static Fragment friendfragment;
    public static Fragment projectfragment;

    public static User current_user;

    static public String hashConverter(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(str.getBytes("utf8"));
            return String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static public String hashConverter(String str1, String str2) {
        String str = (str1.compareTo(str2) < 0) ? str1 + str2 : str2 + str1;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(str.getBytes("utf8"));
            return String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrenttime()
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return (new SimpleDateFormat("yyyyMMddhhmmss").format(date));
    }

    public static String getChangetime(String update)
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        String current = new SimpleDateFormat("yyyyMMddhhmmss").format(date);
        long ld = Long.parseLong(update);
        long cd = Long.parseLong(current);
        long diff = cd - ld;
        if(diff < 1000000)
            return "오늘";
        else if(diff < 2000000)
            return "어제";
        else
            return getChangeymdbartime(update);
    }
    public static String getChangeymdbartime(String date)
    {
        return date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8);
    }
    public static int getPercentbycurrent(String startdate, String enddate)
    {
        try {
            Date start = new SimpleDateFormat("yyyyMMdd").parse(startdate);
            Date end = new SimpleDateFormat("yyyyMMdd").parse(enddate);
            Date current = new Date();
            int percent = (int) ((float)(current.getTime() - start.getTime()) / (end.getTime() - start.getTime())*100);
            if (percent >= 100)
                return 100;
            else
                return percent;
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getPhoneformat(String number)
    {
        return number.substring(0,3) + "-" + number.substring(3,7) + "-" + number.substring(7,11);
    }
    public static void refreshfragment(Fragment fragment)
    {
        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
        ft.detach(fragment).attach(fragment).commitAllowingStateLoss();
    }
    public static String getChangeymdtime(String date)
    {
        return date.substring(0,4)+ date.substring(4,6)+date.substring(6,8);
    }

}
