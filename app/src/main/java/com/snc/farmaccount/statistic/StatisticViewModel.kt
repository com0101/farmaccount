package com.snc.farmaccount.statistic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.StatisticCatalog
import com.snc.farmaccount.`object`.SumEvent
import com.snc.farmaccount.helper.*
import kotlin.collections.ArrayList


class StatisticViewModel : ViewModel() {

    private val _event = MutableLiveData<List<Event>>()
    val event: LiveData<List<Event>>
        get() = _event

    private val _sum = MutableLiveData<List<SumEvent>>()
    val sum: LiveData<List<SumEvent>>
        get() = _sum

    val category = MutableLiveData<StatisticCatalog>()
    val categoryPress = MutableLiveData<StatisticCatalog>()
    private var pickMonth = MutableLiveData<String>()
    var currentMonth = MutableLiveData<String>()
    var sumEvent = MutableLiveData<List<SumEvent>>()
    var filter = MutableLiveData<Boolean>()
    var dayMode = ""

    val eventByCategory: LiveData<List<Event>> = Transformations.map(categoryPress) { catalog ->
        catalog?.let {
            event.value?.filter {
                it.catalog == catalog.name
            }
        }
    }

    val eventByTotalPrice: LiveData<Int> = Transformations.map(event) { it ->
        it.sumBy { it.price.toInt() }
    }

    private val eventByEat: LiveData<List<Event>> = Transformations.map(event) { it ->
        it.filter {
            it.catalog == ApplicationContext.applicationContext().getString(R.string.catalog_eat)
        }
    }

    val eventByEatPrice: LiveData<Int> = Transformations.map(eventByEat) { it ->
        it.sumBy { it.price.toInt() }
    }

    private val eventByCloth: LiveData<List<Event>> = Transformations.map(event) { it ->
        it.filter {
            it.catalog == ApplicationContext.applicationContext().getString(R.string.catalog_cloth)
        }
    }

    val eventByClothPrice: LiveData<Int> = Transformations.map(eventByCloth) { it ->
        it.sumBy { it.price.toInt() }
    }

    private val eventByLive: LiveData<List<Event>> = Transformations.map(event) { it ->
        it.filter {
            it.catalog == ApplicationContext.applicationContext().getString(R.string.catalog_live)
        }
    }

    val eventByLivePrice: LiveData<Int> = Transformations.map(eventByLive) { it ->
        it.sumBy { it.price.toInt() }
    }

    private val eventByTraffic: LiveData<List<Event>> = Transformations.map(event) { it ->
        it.filter {
            it.catalog == ApplicationContext.applicationContext().getString(R.string.catalog_traffic)
        }
    }

    val eventByTrafficPrice: LiveData<Int> = Transformations.map(eventByTraffic) { it ->
        it.sumBy { it.price.toInt() }
    }

    private val eventByFun: LiveData<List<Event>> = Transformations.map(event) { it ->
        it.filter {
            it.catalog == ApplicationContext.applicationContext().getString(R.string.catalog_fun)
        }
    }

    val eventByFunPrice: LiveData<Int> = Transformations.map(eventByFun) { it ->
        it.sumBy { it.price.toInt() }
    }

    private val eventByIncome: LiveData<List<Event>> = Transformations.map(event) { it ->
        it.filter {
            it.catalog == ApplicationContext.applicationContext().getString(R.string.catalog_income)
        }
    }

    val eventByIncomePrice: LiveData<Int> = Transformations.map(eventByIncome) { it ->
        it.sumBy { it.price.toInt() }
    }

    var eventList = ArrayList<Event>()

    init {
        dayMode = Format.getCurrentDate()
    }

    fun getCurrentMonth() {
        pickMonth.value = currentMonth.value
    }

    private fun sumCategoryPrice() {
        _sum.value = sumEvent.value
    }

    fun getEvent() {
        val db = FirebaseFirestore.getInstance()
        db.collection(USER).document("${UserManager.userToken}")
            .collection(EVENT)
            .whereEqualTo(MONTH,"${pickMonth.value}")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(SOPHIE, "getEvent:${document.id} => ${document.data}")

                        document.data.let {
                            eventList.add(document.toObject(Event::class.java))
                        }
                    }
                }
                _event.value = eventList
                sumCategoryPrice()
            }
    }

}