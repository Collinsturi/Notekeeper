package com.rsk.notekeeper

import android.os.Bundle
import androidx.lifecycle.ViewModel

class ItemsActivityViewModel : ViewModel() {
    fun saveState(outState: Bundle) {
        outState.putInt(navDrawerDisplaySelectionStateName, navDrawerDisplaySelection)
    }

    fun restoreState(savedInstanceState: Bundle) {
       navDrawerDisplaySelection = savedInstanceState.getInt(navDrawerDisplaySelectionStateName)
    }

    var navDrawerDisplaySelectionStateName =
        "com.rsk.notekeeper.itemsActivityViewModel.navDrawerDisplaySelection"
    var navDrawerDisplaySelection = R.id.nav_notes
}