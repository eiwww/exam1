package com.sd.exam_1;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.*;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvc;
    EditText tuser,topass,tnpass,trpass;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvc = findViewById(R.id.lvc);
        tuser = findViewById(R.id.user);
        topass = findViewById(R.id.pw);
        tnpass = findViewById(R.id.npw);
        trpass = findViewById(R.id.rpw);
        bt = findViewById(R.id.btn);
        new mydata().execute("");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Login().execute("");
            }
        });

    }

    private class Login extends AsyncTask<String,String,String>{

        String name = tuser.getText().toString();
        String pass = topass.getText().toString();
        String npw = tnpass.getText().toString();
        String rpw = trpass.getText().toString();

        Boolean success = false;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Connection c = DBConnect.getConnection();
                String sql = "select * from tb1 where username = '" + name.toString() + "' and password = '" + pass.toString() + "'";
                ResultSet rs = c.createStatement().executeQuery(sql);
                if(rs.next()){
                    success = true;
                    c.close();
                }else{
                    success = false;
                }

            }catch (Exception ex){
                success = false;
                ex.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            String c1 = npw;
            String c2 = rpw;
            if(success){
                if(c1.equals(c2)){
                    new updateData().execute("");
                    tuser.setText("");
                    topass.setText("");
                    tnpass.setText("");
                    trpass.setText("");
                    tvc.setText("Connection COMPLETE");
                    Toast.makeText(getApplicationContext(), "Update COMPLETE", Toast.LENGTH_SHORT).show();
                }else{
                    tnpass.setText("");
                    trpass.setText("");
                    Toast.makeText(getApplicationContext(), "FAIL to update", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "FAIL data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class mydata extends AsyncTask<String,String,String> {

        String smg="";

        @Override
        protected void onPreExecute() {
            tvc.setText("Connecting...");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection c = DBConnect.getConnection();
                if(c==null){
                    smg="Connection fail...";
                }else{
                    smg="Connection COMPLETE";
                }
            }catch (Exception ex){
                smg="Connection Fail";
                ex.printStackTrace();
            }
            return smg;
        }

        @Override
        protected void onPostExecute(String s) {
            tvc.setText(s);
        }
    }

    private class updateData extends AsyncTask<String,String,String>{

        String smg="";
        String us = tuser.getText().toString();
        String pw = tnpass.getText().toString();

        @Override
        protected void onPreExecute() {
            tvc.setText("Updating Data...");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection c = DBConnect.getConnection();
                String sql = "update tb1 set password = '"+ pw +"' where username = '" + us + "'";
                PreparedStatement stm = c.prepareStatement(sql);
                int r = stm.executeUpdate();
                if(r>0){
                    smg = "Update COMPLETE";
                }else{
                    smg = "Update Fail";
                }
            }catch (Exception ex){
                smg = "Update Fail";
                ex.printStackTrace();
            }
            return smg;
        }

        @Override
        protected void onPostExecute(String s) {
            tvc.setText(s);
        }
    }
}