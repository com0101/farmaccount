package com.snc.farmaccount.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.snc.farmaccount.NavigationListener
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.databinding.FragmentDayCalendarBinding
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class DayCalendarFragment : Fragment() {

    private lateinit var binding: FragmentDayCalendarBinding
    var navListener: NavigationListener?= null
    private var hash = 0
    var date = ""
    var title = ""

    private val viewModel: DayViewModel by lazy {
        ViewModelProviders.of(this).get(DayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDayCalendarBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.textDate.text = title
        binding.eventList.adapter = DayEventAdapter(DayEventAdapter.OnClickListener {

        }, viewModel)

        arrowButtons()
        addEvent()
        viewModel.getEvent()
        getEvent()
        return binding.root
    }

    private fun arrowButtons(){

        binding.imageArrowRight.setOnClickListener {
            navListener?.goRight()
        }

        binding.imageArrowLeft.setOnClickListener {
            navListener?.goLeft()
        }

    }

    private fun addEvent() {
        var date = Date()
        val eventList = ArrayList<Event>()
        eventList.add(Event(0,"12",getString(R.string.tag_breakfast),"i am hungry", date,true))
        eventList.add(Event(1,"45",getString(R.string.tag_breakfast),"i am hungry", date,true))
        eventList.add(Event(2,"66",getString(R.string.tag_breakfast),"i am hungry", date,false))
        eventList.add(Event(3,"80",getString(R.string.tag_breakfast),"i am hungry", date,false))
        viewModel.eventList.value = eventList
    }

    fun getEvent() {
        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
        db.collection("User")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val sub = FirebaseFirestore.getInstance()
                        sub.collection("User/${document.id}/Event")
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        Log.d("Sophie_db", document.id + " => " + document.data)
                                    }
                                } else {
                                    Log.w("Sophie_db_fail", "Error getting documents.", task.exception)
                                }
                            }
                    }
                }
            }
    }

    fun updateCalendar() {
//        val startTimes = Format.getDayStartTS(date)
//        val endTimes = Format.getDayEndTS(date)
//        context?.eventsHelper?.getEvents(startTS, endTS) {
//            receivedEvents(it)
//        }
    }

}
