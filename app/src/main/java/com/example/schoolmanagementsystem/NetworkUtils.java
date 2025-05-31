package com.example.schoolmanagementsystem;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtils {
    private static final String BASE_URL = "http://10.0.2.2/student_system/";
    private static final String TAG = "NetworkUtils";
    private static RequestQueue requestQueue;

    public static void init(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public interface NetworkCallback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    public static void makeRequest(Context context, String endpoint, int method, JSONObject params, NetworkCallback callback) {
        if (requestQueue == null) {
            init(context);
        }

        String url = BASE_URL + endpoint;

        JsonObjectRequest request = new JsonObjectRequest(method, url, params,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            callback.onSuccess(response);
                        } else {
                            callback.onError(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        callback.onError("Invalid response format: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = "Network error occurred";
                    if (error.networkResponse != null) {
                        errorMessage += " (HTTP " + error.networkResponse.statusCode + ")";
                    }
                    callback.onError(errorMessage);
                });

        requestQueue.add(request);
    }

    public static void registerStudent(Context context, String name, String email, String password, int age, NetworkCallback callback) {
        try {
            JSONObject params = new JSONObject();
            params.put("name", name);
            params.put("email", email);
            params.put("password", password);
            params.put("age", age);

            makeRequest(context, "register_student.php", Request.Method.POST, params, callback);
        } catch (JSONException e) {
            callback.onError("Error creating JSON request");
        }
    }

    public static void loginStudent(Context context, String email, String password, NetworkCallback callback) {
        try {
            JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("password", password);

            makeRequest(context, "login_student.php", Request.Method.POST, params, callback);
        } catch (JSONException e) {
            callback.onError("Error creating JSON request");
        }
    }

    public static void getStudents(Context context, NetworkCallback callback) {
        makeRequest(context, "view_students.php", Request.Method.GET, null, callback);
    }

    public static void getAssignments(Context context, int studentId, NetworkCallback callback) {
        makeRequest(context, "get_assignments.php?student_id=" + studentId, Request.Method.GET, null, callback);
    }

    public static void submitAssignment(Context context, int studentId, int assignmentId, String content, NetworkCallback callback) {
        try {
            JSONObject params = new JSONObject();
            params.put("student_id", studentId);
            params.put("assignment_id", assignmentId);
            params.put("content", content);

            makeRequest(context, "submit_assignment.php", Request.Method.POST, params, callback);
        } catch (JSONException e) {
            callback.onError("Error creating JSON request");
        }
    }
}
