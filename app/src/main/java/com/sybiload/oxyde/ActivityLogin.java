package com.sybiload.oxyde;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity
{
    ImageView imageViewLogo;
    RelativeLayout llLogin;
    FloatingActionButton fabLogin;
    EditText editTextUsername;
    EditText editTextPassword;

    private SharedPreferences mainPref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainPref = this.getSharedPreferences("main", 0);

        imageViewLogo = (ImageView) findViewById(R.id.imageViewLogo);
        llLogin = (RelativeLayout) findViewById(R.id.llLogin);
        fabLogin = (FloatingActionButton) findViewById(R.id.fabLogin);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        Animation translateAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        imageViewLogo.startAnimation(translateAnim);

        Animation fadeInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        llLogin.startAnimation(fadeInAnim);
        fabLogin.startAnimation(fadeInAnim);

        fabLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Misc.isConnected(getApplicationContext()))
                {
                    String username = editTextUsername.getText().toString();
                    String password = editTextPassword.getText().toString();

                    new loginAsync().execute(username, password);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Aucune connexion internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class loginAsync extends AsyncTask<String, Void, Void>
    {
        String username;
        String password;

        boolean success = false;

        protected ProgressDialog Dialog;

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(ActivityLogin.this);
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

                mainPref.edit().putString("username", username).commit();
                mainPref.edit().putString("password", password).commit();

                Intent intent = new Intent(getApplication(), ActivityMain.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Erreur d'identification", Toast.LENGTH_LONG).show();
            }
        }
    }
}