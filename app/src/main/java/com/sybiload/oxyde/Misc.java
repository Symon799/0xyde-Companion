package com.sybiload.oxyde;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Misc
{
    public static boolean loginUser(String username, String password)
    {
        try
        {
            String str = "";
            String s = "http://0xyde.sybiload.com/json/login.php?&login=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
            URL url = new URL(s);

            Scanner scan = new Scanner(url.openStream());

            while (scan.hasNext())
            {
                str += scan.nextLine();
            }

            scan.close();

            // build a JSON object
            JSONObject obj = new JSONObject(str);

            if (obj.getBoolean("log"))
            {
                Static.access = obj.getString("access");
                Static.hasPlayedMultiplayer = obj.getInt("game") != 0;

                if (Static.hasPlayedMultiplayer)
                {
                    Static.enemy = obj.getInt("enemy");
                    Static.death = obj.getInt("death");
                    Static.shoot = obj.getInt("shoot");
                    Static.snap = obj.getInt("snap");
                    Static.game = obj.getInt("game");
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isConnected(Context ctx)
    {
        NetworkInfo info = ((ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null ? info.isConnected() : false;
    }
}

