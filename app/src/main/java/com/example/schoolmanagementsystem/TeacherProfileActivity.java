package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;

public class TeacherProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // ثابت لطلب اختيار الصورة

    // Updated: Added editSubjectsTaught and editClassesAssigned
    ImageView profileImage; // لعرض صورة البروفايل
    EditText editName, editEmail, editPhone, editSubjectsTaught, editClassesAssigned; // لحقول إدخال الاسم، البريد الإلكتروني، والهاتف، والمواد التدريسية، والفصول المعينة
    Button buttonSave, buttonChangePhoto; // لزر الحفظ وتغيير الصورة

    Uri imageUri; // لتخزين مسار الصورة المختارة

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile); // ربط النشاط بملف التصميم activity_teacher_profile.xml

        // تهيئة شريط الأدوات (Toolbar)
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // إظهار زر الرجوع (السهم الخلفي)
            getSupportActionBar().setTitle("Teacher Profile"); // تعيين عنوان لشريط الأدوات
        }

        // ربط المتغيرات بعناصر الواجهة في ملف الـ XML
        profileImage = findViewById(R.id.profileImage); // NEW: Initialized ImageView
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editSubjectsTaught = findViewById(R.id.editSubjectsTaught); // NEW: Initialized new EditText
        editClassesAssigned = findViewById(R.id.editClassesAssigned); // NEW: Initialized new EditText
        editPhone = findViewById(R.id.editPhone);
        buttonSave = findViewById(R.id.buttonSave);
        buttonChangePhoto = findViewById(R.id.buttonChangePhoto);

        // تعيين مستمع النقر لزر "Change Photo" لفتح معرض الصور
        buttonChangePhoto.setOnClickListener(v -> openImageChooser());

        // تعيين مستمع النقر لزر "Save"
        buttonSave.setOnClickListener(v -> {
            // هنا يمكنك إضافة منطق حفظ بيانات البروفايل إلى قاعدة بيانات أو Shared Preferences
            String name = editName.getText().toString();
            String email = editEmail.getText().toString();
            String subjects = editSubjectsTaught.getText().toString(); // NEW: Get text from new field
            String classes = editClassesAssigned.getText().toString(); // NEW: Get text from new field
            String phone = editPhone.getText().toString();

            // For now, just show a toast with the collected data
            String profileData = "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Subjects: " + subjects + "\n" + // NEW: Include in toast
                    "Classes: " + classes + "\n" +  // NEW: Include in toast
                    "Phone: " + phone;
            Toast.makeText(TeacherProfileActivity.this, "Profile saved!\n" + profileData, Toast.LENGTH_LONG).show();
            // يمكنك أيضاً إغلاق النشاط هنا إذا أردت العودة بعد الحفظ
            // finish();
        });
    }

    // دالة لفتح معرض الصور لاختيار صورة
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*"); // تحديد نوع المحتوى المراد اختياره كصورة
        intent.setAction(Intent.ACTION_GET_CONTENT); // تحديد الإجراء المطلوب (الحصول على محتوى)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); // بدء النشاط لطلب الصورة
    }

    // دالة لاستقبال نتيجة اختيار الصورة من معرض الصور
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // الحصول على URI للصورة المختارة
            try {
                // تحويل URI إلى Bitmap وعرضها في ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace(); // طباعة تتبع الخطأ في حال وجود مشكلة
            }
        }
    }

    // دالة للتعامل مع نقرات عناصر شريط الأدوات (خاصة بزر الرجوع)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // إذا تم النقر على زر الرجوع (السهم الخلفي)
            finish(); // إغلاق النشاط الحالي والعودة إلى النشاط السابق
            return true;
        }
        return super.onOptionsItemSelected(item); // استدعاء التنفيذ الافتراضي للعناصر الأخرى
    }
}