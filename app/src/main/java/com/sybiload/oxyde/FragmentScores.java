package com.sybiload.oxyde;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentScores extends Fragment
{
    TextView textViewLevel;
    TextView textViewEnemy;
    TextView textViewDeath;
    TextView textViewShoot;
    TextView textViewSnap;
    TextView textViewGame;
    RelativeLayout llPlayed;
    RelativeLayout llNeverPlayed;

    private SharedPreferences mainPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_scores, container, false);

        mainPref = getActivity().getSharedPreferences("main", 0);

        textViewLevel = (TextView) view.findViewById(R.id.textViewNumberLevel);
        textViewEnemy = (TextView) view.findViewById(R.id.textViewNumberEnemy);
        textViewDeath = (TextView) view.findViewById(R.id.textViewNumberDeath);
        textViewShoot = (TextView) view.findViewById(R.id.textViewNumberShoot);
        textViewSnap = (TextView) view.findViewById(R.id.textViewNumberSnap);
        textViewGame = (TextView) view.findViewById(R.id.textViewNumberGame);
        llPlayed = (RelativeLayout) view.findViewById(R.id.llPlayed);
        llNeverPlayed = (RelativeLayout) view.findViewById(R.id.llNeverPlayed);


        String username = mainPref.getString("username", "username");
        String password = mainPref.getString("password", "password");

        if (Misc.isConnected(getActivity()))
        {
            new loginAsync().execute(username, password);
        }
        else
        {
            Toast.makeText(getActivity(), "Aucune connexion internet", Toast.LENGTH_LONG).show();
        }


        return view;
    }

    public class loginAsync extends AsyncTask<String, Void, Void>
    {
        String username;
        String password;

        boolean success = false;

        protected ProgressDialog Dialog;

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(getActivity());
            Dialog.setMessage("Connexion..");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(String... params)
        {
            success = Misc.loginUser(params[0], params[1]);
            username = params[0];
            password = params[1];
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Dialog.dismiss();

            if (success)
            {
                Static.username = username;
                Static.password = password;

                if (Static.hasPlayedMultiplayer)
                {
                    llNeverPlayed.setVisibility(View.GONE);
                    llPlayed.setVisibility(View.VISIBLE);

                    if (Static.enemy > 100 && Static.shoot > 200 && Static.game > 20)
                        textViewLevel.setText("3");
                    else if (Static.enemy > 40 && Static.shoot > 100 && Static.game > 10)
                        textViewLevel.setText("2");
                    else if (Static.enemy > 20 && Static.shoot > 40 && Static.game > 2)
                        textViewLevel.setText("1");
                    else
                        textViewLevel.setText("0");

                    textViewEnemy.setText(Integer.toString(Static.enemy));
                    textViewDeath.setText(Integer.toString(Static.death));
                    textViewShoot.setText(Integer.toString(Static.shoot));
                    textViewSnap.setText(Integer.toString(Static.snap));
                    textViewGame.setText(Integer.toString(Static.game));
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Erreur d'identification", Toast.LENGTH_LONG).show();
            }
        }
    }
}