package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterTeacherListActivity extends AppCompatActivity {

    ListView teacherListView;
    ArrayList<String> teacherList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    String url = "http://10.0.2.2/student_system/register_view_teachers.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher_list);

        teacherListView = findViewById(R.id.teacherListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teacherList);
        teacherListView.setAdapter(adapter);

        loadTeachers();
    }

    private void loadTeachers() {
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("ServerResponse", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equals("success")) {
                            JSONArray teachers = jsonObject.getJSONArray("teachers");

                            for (int i = 0; i < teachers.length(); i++) {
                                JSONObject teacher = teachers.getJSONObject(i);
                                String name = teacher.getString("name");
                                String email = teacher.getString("email");
                                String subject = teacher.getString("subject");

                                teacherList.add("Name: " + name + "\nEmail: " + email + "\nSubject: " + subject);
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No teachers found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Parse Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}
