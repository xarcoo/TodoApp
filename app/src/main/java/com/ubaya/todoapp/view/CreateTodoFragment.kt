package com.ubaya.todoapp.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ubaya.todoapp.R
import com.ubaya.todoapp.databinding.FragmentCreateTodoBinding
import com.ubaya.todoapp.model.Todo
import com.ubaya.todoapp.util.NotificationHelper
import com.ubaya.todoapp.util.TodoWorker
import com.ubaya.todoapp.viewmodel.DetailTodoViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.min

class CreateTodoFragment : Fragment(), RadioClick, TodoEditClick, DateClickListener, TimeClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: FragmentCreateTodoBinding
    private lateinit var viewModel: DetailTodoViewModel

    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), NotificationHelper.REQUEST_NOTIF)
            }
        }

        binding.todo = Todo("", "", 3, 0, 0)
        binding.radioListener = this
        binding.addListener = this
        binding.dateListener = this
        binding.timeListener = this

        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NotificationHelper.REQUEST_NOTIF) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationHelper(requireContext()).createNotification("Todo Created", "Stay Focus!")
            }
        }
    }

    override fun onTodoEditClick(v: View) {
        val c = Calendar.getInstance()
        c.set(year, month, day, hour, minute, 0)
        val today = Calendar.getInstance()
        val diff = (c.timeInMillis/1000L) - (today.timeInMillis/1000L)

        binding.todo!!.todoDate = (c.timeInMillis/1000L).toInt()

        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInitialDelay(diff, java.util.concurrent.TimeUnit.SECONDS)
            .setInputData(
                androidx.work.workDataOf(
                    "title" to "Todo Created", "message" to "Stay Focus!"
                )
            )
            .build()

        WorkManager.getInstance(requireContext()).enqueue(workRequest)

//        var radio = view?.findViewById<RadioButton>(binding.radioGroupPriority.checkedRadioButtonId)
//        var todo = Todo(binding.txtTitle.text.toString(), binding.txtNotes.text.toString(), radio?.tag.toString().toInt(), 0)
//        2 line di atas gaperlu, karena udah ada di two-way binding, jadi tinggal masukin kek di bawah
        val list = listOf(binding.todo!!)
        viewModel.addTodo(list)
        Toast.makeText(view?.context, "Data Added", Toast.LENGTH_LONG).show()
        Navigation.findNavController(v).popBackStack()
    }

    override fun onRadioClick(v: View) {
        binding.todo?.priority = v.tag.toString().toInt()
    }

    override fun onDateClick(v: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), this, year, month, day).show()
//        activity.let { it1 -> DatePickerDialog(requireContext(), this, year, month, day).show() }
    }

    override fun onTimeClick(v: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        TimePickerDialog(requireContext(), this, hour, minute, false).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Calendar.getInstance().let {
            it.set(year, month, dayOfMonth)
            binding.txtDate.setText(dayOfMonth.toString().padStart(2, '0') + "-" + (month + 1).toString().padStart(2, '0') + "-" + year)
            this.year = year
            this.month = month
            this.day = dayOfMonth
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        binding.txtTime.setText(hourOfDay.toString().padStart(2,'0') + ":" + minute.toString().padStart(2, '0'))
        this.hour = hourOfDay
        this.minute = minute
    }
}