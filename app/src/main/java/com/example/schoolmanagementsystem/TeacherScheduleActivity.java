package com.example.schoolmanagementsystem;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class TeacherScheduleActivity extends AppCompatActivity {

    private static final String TAG = "TeacherScheduleActivity";
    private LinearLayout scheduleContainer;
    // تأكد أن هذا العنوان يشير إلى مجلد مشروعك على الخادم المحلي (XAMPP/WAMPP)
    // 10.0.2.2 هو العنوان الخاص بالمحاكي للوصول إلى localhost على جهاز الكمبيوتر
    private static final String BASE_URL = "http://10.0.2.2/student_system/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);

        scheduleContainer = findViewById(R.id.scheduleContainer);

        // جلب معرف المعلم (teacher_id) من النشاط السابق
        // تأكد أنك تمرر هذا الـ ID بشكل صحيح من النشاط الذي يفتح هذا النشاط
        int teacherId = getIntent().getIntExtra("teacher_id", -1);
        if (teacherId == -1) {
            showError("Invalid teacher ID. Please provide a valid teacher ID.");
            return;
        }

        // استدعاء الدالة لجلب جدول المعلم
        fetchTeacherSchedule(teacherId);
    }

    private void fetchTeacherSchedule(int teacherId) {
        // بناء الرابط الكامل لطلب الـ API
        String url = BASE_URL + "teacher_get_teacher_schedule.php?teacher_id=" + teacherId;

        // إنشاء طلب JSON Object باستخدام Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // التحقق من حالة الاستجابة (إذا كانت "success")
                        if ("success".equals(response.optString("status"))) {
                            // جلب مصفوفة "schedule" من استجابة JSON
                            JSONArray schedule = response.getJSONArray("schedule");
                            // مسح أي view موجودة مسبقًا لتجنب التكرار
                            scheduleContainer.removeAllViews();

                            // تكرار على كل عنصر في مصفوفة الجدول لإنشاء بطاقة عرض
                            for (int i = 0; i < schedule.length(); i++) {
                                JSONObject row = schedule.getJSONObject(i);
                                String subject = row.getString("subject");
                                String day = row.getString("day");
                                String time = row.getString("time");
                                String grade = row.getString("grade");

                                // إنشاء LinearLayout كبطاقة لعرض بيانات الدرس
                                LinearLayout card = new LinearLayout(this);
                                card.setOrientation(LinearLayout.VERTICAL);
                                card.setPadding(32, 24, 32, 24);
                                card.setBackgroundResource(R.drawable.card_background); // استخدام خلفية البطاقة التي أنشأتها

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                layoutParams.setMargins(0, 0, 0, 24); // إضافة هامش سفلي بين البطاقات
                                card.setLayoutParams(layoutParams);

                                // إضافة TextView للمادة واليوم
                                TextView tvSubject = new TextView(this);
                                tvSubject.setText(day + ": " + subject);
                                tvSubject.setTextSize(16);
                                tvSubject.setTypeface(null, Typeface.BOLD);
                                tvSubject.setTextColor(0xFF000000); // لون أسود
                                card.addView(tvSubject);

                                // إضافة TextView للوقت
                                TextView tvTime = new TextView(this);
                                tvTime.setText("Time: " + time);
                                tvTime.setTextSize(14);
                                tvTime.setTextColor(0xFF555555); // لون رمادي غامق
                                card.addView(tvTime);

                                // إضافة TextView للصف الدراسي
                                TextView tvGrade = new TextView(this);
                                tvGrade.setText("Grade: " + grade);
                                tvGrade.setTextSize(14);
                                tvGrade.setTextColor(0xFF555555); // لون رمادي غامق
                                card.addView(tvGrade);

                                // إضافة البطاقة إلى الـ scheduleContainer
                                scheduleContainer.addView(card);
                            }
                        } else {
                            // إذا لم يتم العثور على جدول
                            showError(response.optString("message", "No schedule found."));
                        }
                    } catch (Exception e) {
                        // التعامل مع أخطاء تحليل JSON
                        showError("Error processing schedule data.");
                        Log.e(TAG, "Parse error: ", e);
                    }
                },
                error -> {
                    // التعامل مع أخطاء شبكة Volley
                    showError("Failed to load schedule. Check your internet connection or server.");
                    Log.e(TAG, "Volley error: ", error);
                });

        // إضافة الطلب إلى قائمة انتظار Volley
        Volley.newRequestQueue(this).add(request);
    }

    // دالة مساعدة لعرض رسائل Toast
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show(); // عرض الرسالة لمدة أطول
    }
}