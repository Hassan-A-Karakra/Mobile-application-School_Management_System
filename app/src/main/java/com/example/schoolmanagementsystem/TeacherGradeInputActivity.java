package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherGradeInputActivity extends AppCompatActivity {

    private Spinner spinnerClass, spinnerSubject;
    private RecyclerView recyclerViewStudents;
    private Button buttonSaveGrades, buttonPublishGrades;
    private List<Student> studentList;
    private StudentGradeAdapter studentGradeAdapter;

    private RequestQueue requestQueue;
    private static final String BASE_URL = "http://192.168.1.12/student_system/"; // غير هذا إلى عنوان IP الخاص بك!

    // يمكنك تخزين teacher_id هنا بعد تسجيل دخول المعلم
    // حالياً نستخدم قيمة افتراضية، ولكن يجب جلبها ديناميكياً
    private int teacherId = 1; // **تأكد من أن هذا الـ ID صحيح للمعلم الذي يسجل الدخول**


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_grade_input);

        requestQueue = Volley.newRequestQueue(this);

        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        buttonSaveGrades = findViewById(R.id.buttonSaveGrades);
        buttonPublishGrades = findViewById(R.id.buttonPublishGrades);

        studentList = new ArrayList<>();
        studentGradeAdapter = new StudentGradeAdapter(studentList);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStudents.setAdapter(studentGradeAdapter);

        // جلب الصفوف عند بدء النشاط
        fetchClasses();
        // جلب المواد عند بدء النشاط
        fetchSubjects();

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // عند اختيار صف، نجلب الطلاب إذا تم اختيار مادة أيضاً
                if (spinnerSubject.getSelectedItem() != null) {
                    fetchStudents();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // لا شيء يحدث
            }
        });

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // عند اختيار مادة، نجلب الطلاب إذا تم اختيار صف أيضاً
                if (spinnerClass.getSelectedItem() != null) {
                    fetchStudents();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // لا شيء يحدث
            }
        });


        buttonSaveGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGrades(false); // حفظ بدون نشر
            }
        });

        buttonPublishGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGrades(true); // حفظ مع النشر
            }
        });
    }

    // *************************************************************************
    // دوال جلب البيانات من PHP باستخدام Volley
    // *************************************************************************

    private void fetchClasses() {
        String url = BASE_URL + "get_grades.php"; // نستخدم get_grades.php كما قمت بتعديله ليجلب الصفوف

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> classes = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                classes.add(response.getString(i));
                            } catch (JSONException e) {
                                Log.e("Volley", "Error parsing class: " + e.getMessage());
                            }
                        }
                        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(TeacherGradeInputActivity.this,
                                android.R.layout.simple_spinner_item, classes);
                        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerClass.setAdapter(classAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error fetching classes: " + error.getMessage());
                        Toast.makeText(TeacherGradeInputActivity.this, "فشل في جلب الصفوف: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void fetchSubjects() {
        String url = BASE_URL + "get_subjects.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> subjects = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                subjects.add(response.getString(i));
                            } catch (JSONException e) {
                                Log.e("Volley", "Error parsing subject: " + e.getMessage());
                            }
                        }
                        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(TeacherGradeInputActivity.this,
                                android.R.layout.simple_spinner_item, subjects);
                        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerSubject.setAdapter(subjectAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error fetching subjects: " + error.getMessage());
                        Toast.makeText(TeacherGradeInputActivity.this, "فشل في جلب المواد: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void fetchStudents() {
        String selectedClass = spinnerClass.getSelectedItem().toString();
        String url = BASE_URL + "get_students_by_class_and_subject.php?class_name=" + selectedClass;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                studentList.clear(); // مسح القائمة الحالية
                                JSONArray studentsJson = response.getJSONArray("students");
                                for (int i = 0; i < studentsJson.length(); i++) {
                                    JSONObject studentJson = studentsJson.getJSONObject(i);
                                    int id = studentJson.getInt("id");
                                    String name = studentJson.getString("name");
                                    studentList.add(new Student(id, name));
                                }
                                studentGradeAdapter.notifyDataSetChanged(); // تحديث الـ RecyclerView
                            } else {
                                Toast.makeText(TeacherGradeInputActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                studentList.clear(); // مسح القائمة إذا لم يكن هناك طلاب
                                studentGradeAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Log.e("Volley", "Error parsing students JSON: " + e.getMessage());
                            Toast.makeText(TeacherGradeInputActivity.this, "خطأ في معالجة بيانات الطلاب.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error fetching students: " + error.getMessage());
                        Toast.makeText(TeacherGradeInputActivity.this, "فشل في جلب الطلاب: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        studentList.clear(); // مسح القائمة في حالة الخطأ
                        studentGradeAdapter.notifyDataSetChanged();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    // *************************************************************************
    // دالة حفظ العلامات إلى PHP
    // *************************************************************************

    private void saveGrades(boolean publish) {
        String selectedSubject = spinnerSubject.getSelectedItem().toString();
        if (selectedSubject == null || selectedSubject.isEmpty()) {
            Toast.makeText(this, "الرجاء اختيار مادة.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray gradesArray = new JSONArray();
        boolean allGradesFilled = true;

        for (Student student : studentGradeAdapter.getStudentList()) {
            String grade = String.valueOf(student.getGrade());
            if (grade.isEmpty() || grade.trim().equals("")) {
                allGradesFilled = false;
                break; // نخرج من الحلقة بمجرد العثور على علامة فارغة
            }
            try {
                JSONObject studentGradeJson = new JSONObject();
                studentGradeJson.put("student_id", student.getId());
                studentGradeJson.put("grade", grade);
                gradesArray.put(studentGradeJson);
            } catch (JSONException e) {
                Log.e("JSON Error", "Error creating JSON for student grade: " + e.getMessage());
                Toast.makeText(this, "خطأ في إعداد بيانات العلامات.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!allGradesFilled) {
            Toast.makeText(this, "يجب ملء جميع حقول العلامات، ولو بعلامة صفر.", Toast.LENGTH_LONG).show();
            return;
        }

        // إرسال البيانات إلى الخادم
        String url = BASE_URL + "save_grades.php";

        JSONObject postData = new JSONObject();
        try {
            postData.put("grades", gradesArray);
            postData.put("subject_name", selectedSubject);
            postData.put("publish", publish ? 1 : 0); // 1 للنشر، 0 لعدم النشر
            postData.put("teacher_id", teacherId); // إرسال teacher_id
        } catch (JSONException e) {
            Log.e("JSON Error", "Error creating POST data: " + e.getMessage());
            Toast.makeText(this, "خطأ في إعداد بيانات الإرسال.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(TeacherGradeInputActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                // ربما تريد مسح حقول العلامات أو تحديث القائمة بعد الحفظ
                                // studentList.clear();
                                // studentGradeAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(TeacherGradeInputActivity.this, "فشل الحفظ: " + response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Volley", "Error parsing save grades response: " + e.getMessage());
                            Toast.makeText(TeacherGradeInputActivity.this, "خطأ في معالجة رد الخادم.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error saving grades: " + error.getMessage());
                        Toast.makeText(TeacherGradeInputActivity.this, "فشل في التواصل مع الخادم: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        // عرض تفاصيل الخطأ إذا أمكن
                        if (error.networkResponse != null) {
                            Log.e("Volley", "Error Response code: " + error.networkResponse.statusCode);
                            Log.e("Volley", "Error Response data: " + new String(error.networkResponse.data));
                        }
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}