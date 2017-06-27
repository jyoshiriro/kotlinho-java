package br.com.viradatecnologica.kotlinho.java;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import feign.Feign;
import feign.gson.GsonDecoder;

public class MainActivity extends AppCompatActivity {

    private Button btnSearch;
    private EditText edUserid;

    private TextView tvName;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvStreet;
    private TextView tvCity;
    private TextView tvLat;
    private TextView tvLng;

    private List<TextView> textViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configPolicy();

        initComponents();

        configBtnOnClick();
    }

    private void initComponents() {
        btnSearch = (Button) findViewById(R.id.btnSearch);
        edUserid = (EditText) findViewById(R.id.etUserid);

        tvName = (TextView) findViewById(R.id.tvName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvStreet = (TextView) findViewById(R.id.tvStreet);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLng = (TextView) findViewById(R.id.tvLng);

        textViews.addAll(Arrays.asList(
                tvName, tvUsername, tvEmail,
                tvStreet, tvCity, tvLat, tvLng));
    }

    private void configBtnOnClick() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new AsyncTask<Integer, Void, User>(){

                            @Override
                            protected User doInBackground(Integer... params) {
                                try {
                                    return Feign.builder()
                                            .decoder(new GsonDecoder())
                                            .target(UserRequets.class, "http://jsonplaceholder.typicode.com")
                                            .getUser(params[0]);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return null;
                                }

                            }

                        }.doInBackground(Integer.valueOf(edUserid.getText().toString()));

                if (user==null) {
                    Toast.makeText(
                            MainActivity.this,
                            "Usuário "+edUserid.getText()+" não encontrado!",
                            Toast.LENGTH_SHORT).show();

                    clearTextViews();
                } else {
                    setTextViews(user);
                }
            }
        });
    }

    private void setTextViews(User user) {
        tvName.setText(user.getName());
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());

        Address address = user.getAddress();
        tvStreet.setText(address.getStreet());
        tvCity.setText(address.getCity());

        if (address.getGeo() != null) {
            if (address.getGeo().getLat() != null)
                tvLat.setText(address.getGeo().getLat().toString());
            if (address.getGeo().getLng() != null)
                tvLng.setText(address.getGeo().getLng().toString());
        }
    }

    private void clearTextViews() {
        textViews.forEach( it -> it.setText("") );
    }

    private void configPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

}
