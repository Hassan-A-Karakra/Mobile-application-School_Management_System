package com.example.school_management_system1

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.school_management_system1.databinding.ActivityTeacherAssignmentsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import android.content.Intent

class TeacherAssignmentsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityTeacherAssignmentsBinding
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherAssignmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupDatePicker()
        setupClickListeners()
        setupNavigationDrawer()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        binding.assignmentsRecyclerView.layoutManager = LinearLayoutManager(this)
        // TODO: Initialize adapter and set it to RecyclerView
    }

    private fun setupDatePicker() {
        binding.dueDateInput.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupClickListeners() {
        binding.createAssignmentButton.setOnClickListener {
            createNewAssignment()
        }

        binding.fabAddAssignment.setOnClickListener {
            showCreateAssignmentForm()
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                binding.dueDateInput.setText(dateFormatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun createNewAssignment() {
        val title = binding.assignmentTitleInput.text.toString()
        val description = binding.assignmentDescriptionInput.text.toString()
        val dueDate = binding.dueDateInput.text.toString()

        if (title.isBlank() || description.isBlank() || dueDate.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO: Save assignment to database
        Toast.makeText(this, "Assignment created successfully", Toast.LENGTH_SHORT).show()
        clearInputFields()
    }

    private fun showCreateAssignmentForm() {
        // Show the form card if it's hidden
        binding.root.findViewById<androidx.cardview.widget.CardView>(R.id.createAssignmentCard)?.visibility =
            android.view.View.VISIBLE
    }

    private fun clearInputFields() {
        binding.assignmentTitleInput.text?.clear()
        binding.assignmentDescriptionInput.text?.clear()
        binding.dueDateInput.text?.clear()
    }

    private fun setupNavigationDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_header_desc,
            R.string.nav_header_desc
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_inbox -> {
                Toast.makeText(this, "Inbox clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_outbox -> {
                Toast.makeText(this, "Outbox clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_trash -> {
                Toast.makeText(this, "Trash clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_spam -> {
                Toast.makeText(this, "Spam clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_profile -> {
                val intent = Intent(this, TeacherProfileActivity::class.java)
                startActivity(intent)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
} 