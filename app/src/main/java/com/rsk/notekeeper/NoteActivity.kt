package com.rsk.notekeeper

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class NoteActivity : AppCompatActivity() {

    private val tag = this::class.simpleName
    private var notePosition = POSITION_NOT_SET
    private var noteColor: Int  = Color.TRANSPARENT



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        setSupportActionBar(findViewById(R.id.toolbar))

        val adapterCourses = ArrayAdapter<CourseInfo>(this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnercourse = findViewById<Spinner>(R.id.Spinnercourse)

        spinnercourse?.adapter = adapterCourses

        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET)?:intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)

        if(notePosition != POSITION_NOT_SET){
            displayNote()
        }else{
            createNewNote()
        }
        Log.d(tag,  "onCreate")

        statusBarColor()

        val colorSelector = findViewById<com.rsk.notekeeper.ColorSlider>(R.id.colorSelector)

        colorSelector.addListener{
            noteColor = it
        }
    }

    private fun statusBarColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.Status_bar_color)
    }

    private fun createNewNote() {
        DataManager.notes.add(NoteInfo())
        notePosition = DataManager.notes.lastIndex

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }

    private fun displayNote() {
        val editTextTitle = findViewById<EditText>(R.id.textNoteTitle)

        if(notePosition > DataManager.notes.lastIndex){
            Snackbar.make(editTextTitle, "Note not found", Snackbar.LENGTH_LONG).show()

            Log.e(tag, "Invalid note position $notePosition, max valid position ${DataManager.notes.lastIndex}")
            return
        }

        Log.i(tag, "Displaying note for position $notePosition")
        val note = DataManager.notes[notePosition]


        val editTextNote = findViewById<EditText>(R.id.textNoteText)

        editTextTitle.setText(note.title)
        editTextNote.setText(note.text)
        noteColor = note.color

        val colorSelector = findViewById<com.rsk.notekeeper.ColorSlider>(R.id.colorSelector)
        colorSelector.selectedColorValue = note.color

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        val spinnnerCourses = findViewById<Spinner>(R.id.Spinnercourse)

        spinnnerCourses.setSelection(coursePosition)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_settings -> true
            R.id.action_Next -> {
                moveNext()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(notePosition >= DataManager.notes.lastIndex){
            val menuItem = menu?.findItem(R.id.action_Next)
            if(menuItem != null){
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_24)
                menuItem.isEnabled = false

                //Don't show when a new note is created. Snackbar disappearing faster than intended
//                val textNoteTitle = findViewById<EditText>(R.id.textNoteTitle)
//                Snackbar.make(textNoteTitle, "No more notes", 10).show()

            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        saveNote()
        Log.d(tag, "onPause")
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]
        val textNoteTitle = findViewById<EditText>(R.id.textNoteTitle)
        note.title = textNoteTitle.text.toString()
        val textNoteText = findViewById<EditText>(R.id.textNoteText)
        note.text = textNoteText.text.toString()

        val spinnerCourse = findViewById<Spinner>(R.id.Spinnercourse)
        note.course = spinnerCourse.selectedItem as CourseInfo
        note.color = this.noteColor

    }


}
