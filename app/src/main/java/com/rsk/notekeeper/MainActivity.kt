package com.rsk.notekeeper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.rsk.notekeeper.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle : ActionBarDrawerToggle

    private val notesLayoutManager by lazy {
        LinearLayoutManager(this)
    }
    private val noteRecyclerAdapter by lazy {
        NoteRecyclerAdapter(this, DataManager.notes)
    }
    private val courseLayoutManager by lazy{
        GridLayoutManager(this, 1)
    }
    private val courseRecyclerAdapter by lazy{
        CourseRecyclerAdapter(this, DataManager.courses.values.toList())
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this)[ItemsActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)

        if(savedInstanceState != null){
            viewModel.restoreState(savedInstanceState)
        }

        val drawerLayout :DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        handleDisplaySelection(viewModel.navDrawerDisplaySelection)

        navView.setNavigationItemSelectedListener {item ->
            when(item.itemId){
                R.id.nav_courses,
                R.id.nav_notes -> {
                    handleDisplaySelection(item.itemId)
                    viewModel.navDrawerDisplaySelection =item.itemId
                }
                R.id.nav_rate_us -> Toast.makeText(applicationContext, "Rate us clicked", Toast.LENGTH_LONG).show()
                R.id.nav_share -> Toast.makeText(applicationContext, "Share clicked", Toast.LENGTH_LONG).show()

            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }

        setStatusBarColor()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(outState != null)
            viewModel.saveState(outState)
    }

    fun handleDisplaySelection(itemId: Int){
        when(itemId){
            R.id.nav_courses -> displayCourses()
            R.id.nav_notes -> displayNotes()

        }
    }

    private val listItems: RecyclerView?
        get() {
            return findViewById(R.id.listItems)
        }

    private fun displayCourses() {
        listItems?.layoutManager = courseLayoutManager
        listItems?.adapter = courseRecyclerAdapter

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.hide()

        //Menu remains checked even after another item is checked
//        val navView = findViewById<NavigationView>(R.id.nav_view)
//        navView.menu.findItem(R.id.nav_courses).isChecked = true
    }

    private fun displayNotes() {
        listItems?.layoutManager = notesLayoutManager
        listItems?.adapter = noteRecyclerAdapter

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.show()

        //Menu remains checked even after another item is checked
//        val navView = findViewById<NavigationView>(R.id.nav_view)
//        navView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun setStatusBarColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.Status_bar_color)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        val listItems = listItems
        super.onResume()
        listItems?.adapter?.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
                return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        super.onBackPressed()
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }
}
