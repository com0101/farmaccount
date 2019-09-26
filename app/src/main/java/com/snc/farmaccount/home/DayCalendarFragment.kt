package com.snc.farmaccount.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.snc.farmaccount.helper.NavigationListener
import com.snc.farmaccount.databinding.FragmentDayCalendarBinding

class DayCalendarFragment : Fragment() {

    private lateinit var binding: FragmentDayCalendarBinding
    var navListener: NavigationListener?= null
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
        viewModel.currentDate.value = title
        viewModel.getCurrentDate()
//        viewModel.getFirebase()

        binding.eventList.adapter = DayEventAdapter(DayEventAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        }, viewModel)

        viewModel.navigateToDetail.observe(this, androidx.lifecycle.Observer { it ->
            it?.let {
                this.findNavController()
                    .navigate(DayCalendarFragmentDirections.actionGlobalDetailFragment(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })

//        viewModel.event.observe(this, Observer {
//            if (it.isEmpty()) {
//                binding.spendHint.visibility = View.VISIBLE
//            } else {
//                binding.spendHint.visibility = View.GONE
//
//            }
//        })

        viewModel.getFirebase.observe(this, Observer {
            Log.i("Sophie_filter","$it")
            (binding.eventList.adapter as DayEventAdapter).submitList(it)
            if (it.isEmpty()) {
                binding.spendHint.visibility = View.VISIBLE
            } else {
                binding.spendHint.visibility = View.GONE

            }
        })

        return binding.root
    }


    fun updateCalendar() {
//        val startTimes = Format.getDayStartTS(date)
//        val endTimes = Format.getDayEndTS(date)
//        context?.eventsHelper?.getEvents(startTS, endTS) {
//            receivedEvents(it)
//        }
    }

}
