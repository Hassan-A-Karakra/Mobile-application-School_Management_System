package com.example.schoolmanagementsystem;

public class TeacherAssignment {

        private int id; // معرف الواجب، من نوع int
        private String title; // عنوان الواجب
        private String description; // وصف الواجب
        private String dueDate; // تاريخ استحقاق الواجب

        // المُنشئ (Constructor) لكلاس Assignment
        public TeacherAssignment(int id, String title, String description, String dueDate) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.dueDate = dueDate;
        }

        // دوال الجلب (Getters) للوصول إلى بيانات الواجب
        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getDueDate() {
            return dueDate;
        }

        // دوال التعديل (Setters) إذا أردت تغيير قيم الواجب بعد إنشائه
        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }
    }

