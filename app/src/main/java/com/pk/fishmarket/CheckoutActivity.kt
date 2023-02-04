package com.pk.fishmarket



import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pk.fishmarket.Adapter.AddressAdapter
import com.pk.fishmarket.ResponseModel.AddressDetails
import com.pk.fishmarket.Utils.AddressInterface
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.*
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class CheckoutActivity : AppCompatActivity(), PaymentResultListener,AddressInterface {
    lateinit var add_address:TextView
    lateinit var fullname:TextView
    lateinit var address:TextView
    lateinit var phone_edt:TextView
    lateinit var total_amt:TextView
    lateinit var edit_address:TextView
    lateinit var back_ll:RelativeLayout
    lateinit var rl_date:RelativeLayout
    lateinit var rl_cod:RelativeLayout
    lateinit var rl_pay_online:RelativeLayout
    lateinit var submit_data:RelativeLayout



    lateinit var  addAddressModel: GetAllAddressViewModel
    lateinit var getCartDetailsViewModel: GetCartDetailsViewModel
    lateinit var placeOrderViewModel: PlaceOrderViewModel
    lateinit var addressDeleteViewModel: AddressDeleteViewModel
    var userid =""
    var Address_id=""
    var Address_one=""
    var Address_two=""
    var Pincode=""
    var Phone_number=""
    var full_name=""
    var latitude = ""
    var longitude = ""
    var payment_mode = ""
    var delivery_date = ""
    var order_date = ""
    var order_time = ""
    var total_price = ""

    lateinit var total:TextView
    lateinit var addressList:RecyclerView
    lateinit var saved_address_ll:LinearLayout
    lateinit var add_address_ll:RelativeLayout
    lateinit var rl_shipping_address:RelativeLayout
    lateinit var rl_time:RelativeLayout
    lateinit var time_txt:TextView
    lateinit var delivery:TextView
    lateinit var subtotal:TextView
    lateinit var date_txt:TextView
    lateinit var img_cod_unselect:ImageView
    lateinit var img_cod_select:ImageView
    lateinit var img_online_unselect:ImageView
    lateinit var img_online_select:ImageView
    lateinit var edit_address_txt:ImageView
    var items_list :ArrayList<HashMap<String,String>> = ArrayList()
    var addressArrayList :ArrayList<AddressDetails> = ArrayList()
    var cal = Calendar.getInstance()
    var aTime =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        add_address = findViewById(R.id.add_address)
        userid= SharedPreferencesUtil().getUserId(this).toString();
        fullname = findViewById(R.id.fullname)
        address = findViewById(R.id.address)
        phone_edt = findViewById(R.id.phone_edt)
        back_ll = findViewById(R.id.back_ll)
        rl_date = findViewById(R.id.rl_date)
        edit_address = findViewById(R.id.edit_address)
        total=findViewById(R.id.total)
        delivery=findViewById(R.id.delivery)
        subtotal=findViewById(R.id.subtotal)
        rl_cod=findViewById(R.id.rl_cod)
        rl_pay_online=findViewById(R.id.rl_pay_online)
        img_cod_unselect=findViewById(R.id.img_cod_unselect)
        img_cod_select=findViewById(R.id.img_cod_select)
        img_online_unselect=findViewById(R.id.img_online_unselect)
        img_online_select=findViewById(R.id.img_online_select)
        total_amt=findViewById(R.id.total_amt)
        date_txt=findViewById(R.id.date_txt)
        submit_data=findViewById(R.id.submit_data)
        edit_address_txt=findViewById(R.id.edit_address_txt)
        saved_address_ll=findViewById(R.id.saved_address_ll)
        addressList=findViewById(R.id.addressList)
        add_address_ll=findViewById(R.id.add_address_ll)
        rl_shipping_address=findViewById(R.id.rl_shipping_address)
        rl_time=findViewById(R.id.rl_time)
        time_txt=findViewById(R.id.time_txt)
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        latitude= SharedPreferencesUtil().getLat(this).toString();
        longitude= SharedPreferencesUtil().getLong(this).toString();
        userid= SharedPreferencesUtil().getUserId(this).toString();
        addAddressModel = ViewModelProvider(this, factory)[GetAllAddressViewModel::class.java]
        getCartDetailsViewModel = ViewModelProvider(this, factory)[GetCartDetailsViewModel::class.java]
        placeOrderViewModel = ViewModelProvider(this, factory)[PlaceOrderViewModel::class.java]
        addressDeleteViewModel = ViewModelProvider(this, factory)[AddressDeleteViewModel::class.java]

        order_date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        rl_time.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            var hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val timePickerDialog = TimePickerDialog(this@CheckoutActivity,R.style.DialogTheme,
                { view, hourOfDay, minute ->



                   var hour = hourOfDay
                    var minutes = minute
                    var timeSet = ""
                    if (hour > 12) {
                        hour -= 12
                        timeSet = "PM"
                    } else if (hour === 0) {
                        hour += 12
                        timeSet = "AM"
                    } else if (hour === 12) {
                        timeSet = "PM"
                    } else {
                        timeSet = "AM"
                    }

                    var min: String? = ""
                    if (minutes < 10) min = "0$minutes" else min = java.lang.String.valueOf(minutes)

                    // Append in a StringBuilder

                    // Append in a StringBuilder
                    /* aTime = StringBuilder().append(hour).append(':')
                        .append(min).append(" ").append(timeSet).toString()*/
                    aTime =hour.toString() + ":"+min.toString()+ " "+timeSet
                    time_txt.setText(aTime)
                    order_time = aTime

                  //  Toast.makeText(this@CheckoutActivity, delivery_date, Toast.LENGTH_SHORT).show()
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }
        add_address.setOnClickListener {
            startActivity(Intent(this,AddAddressActivity::class.java))
        }
        back_ll.setOnClickListener {
            finish()
        }
        edit_address.setOnClickListener{
            var intent = Intent(this@CheckoutActivity,EditAddressActivity::class.java)
            intent.putExtra("address_id",Address_id)
            intent.putExtra("address_one",Address_one)
            intent.putExtra("address_two",Address_two)
            intent.putExtra("pincode",Pincode)
            intent.putExtra("fullname",full_name)
            intent.putExtra("phone",Phone_number)
            startActivity(intent)
        }
        edit_address_txt.setOnClickListener {
            var intent = Intent(this@CheckoutActivity,EditAddressActivity::class.java)
            intent.putExtra("address_id",Address_id)
            intent.putExtra("address_one",Address_one)
            intent.putExtra("address_two",Address_two)
            intent.putExtra("pincode",Pincode)
            intent.putExtra("fullname",full_name)
            intent.putExtra("phone",Phone_number)
            startActivity(intent)
        }

        submit_data.setOnClickListener {
            var product = items_list.toString()

            //var transactionId = UUID.randomUUID().toString();
            if(AddressAdapter.AddressId == "")
            {
                Toast.makeText(this,"Please add an address first",Toast.LENGTH_SHORT).show()
            }
            else if(delivery_date=="")

            {
                Toast.makeText(this,"Please provide delivery date",Toast.LENGTH_SHORT).show()
            }
            else if(order_time=="")

            {
                Toast.makeText(this,"Please provide delivery time",Toast.LENGTH_SHORT).show()
            }

            else if(delivery_date=="")

            {
                Toast.makeText(this,"Please provide delivery date",Toast.LENGTH_SHORT).show()
            }
             else if(payment_mode=="")
            {
                Toast.makeText(this,"Please provide Payment Mode",Toast.LENGTH_SHORT).show()
            }else {

                if(payment_mode == "COD")
                {
                    delivery_date = delivery_date+" " +aTime
                    Log.d("product",product)
                submitData(
                    userid,
                    product,
                    total_price,
                    payment_mode,
                    order_date,
                    "0",
                    delivery_date,
                    AddressAdapter.AddressId
                )
                }
                else if(payment_mode == "online")
                {

                   startPayment()
                }
                else
                {

                }

            }
        }
        rl_pay_online.setOnClickListener {
            payment_mode = "online"
            img_cod_unselect.visibility = View.VISIBLE
            img_cod_select.visibility = View.GONE
            img_online_unselect.visibility = View.GONE
            img_online_select.visibility = View.VISIBLE
        }
        rl_cod.setOnClickListener {
            payment_mode = "COD"
            img_cod_unselect.visibility = View.GONE
            img_cod_select.visibility = View.VISIBLE
            img_online_unselect.visibility = View.VISIBLE
            img_online_select.visibility = View.GONE
        }
        getCartItems(userid)
        rl_date.setOnClickListener {
            var calender = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView()
                }

           var datePicker= DatePickerDialog(this,R.style.DialogTheme,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
            datePicker.getDatePicker().setMinDate(calender.getTimeInMillis());

            datePicker.show()
        }

    }

    private fun startPayment() {
        val activity: Activity = this

        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Fish Market")
            options.put("description", "Demoing Charges")

            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            var amount: Double = total_price.toDouble()
            amount = amount * 100
            options.put("amount", amount)
            val preFill = JSONObject()
            preFill.put("email", "priyankakar9091@gmail.com")
            preFill.put("contact", Phone_number)
            options.put("prefill", preFill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }

    }

    private fun submitData(
        userid: String,
        product: String,
        totalPrice: String,
        paymentMode: String,
        orderDate: String,
        transactionId: String,
        deliveryDate: String,
        addressId: String
    ) {
        Log.d("userid",userid)
        Log.d("product",product)
        Log.d("totalPrice",totalPrice)
        Log.d("paymentMode",paymentMode)
        Log.d("transactionId",transactionId)
        Log.d("deliveryDate",deliveryDate)
        Log.d("addressId",addressId)
        Log.d("orderDate",orderDate)
        placeOrderViewModel.addOrder(userid,product,totalPrice,paymentMode,orderDate,transactionId,deliveryDate,addressId,"0")
        placeOrderViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                            Toast.makeText(this,response.body()!!.message,Toast.LENGTH_SHORT).show()
                                SharedPreferencesUtil().saveCartCount("0",this@CheckoutActivity)
                                var i= Intent(this, SuccessPageActivity::class.java)
                                startActivity(i)
                                finish()
                            } else {

                                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {

                        response.message?.let { message ->

                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {

                    }
                }
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd-MM-yyyy" // mention the format you need

        val sdf = SimpleDateFormat(myFormat, Locale.US)

        Log.e("Gvjdasdf",""+sdf.format(cal.time))
        date_txt.text = sdf.format(cal.time)
        delivery_date = date_txt.text.toString()
        time_txt.text ="Select Time"
        order_time=""
    }

    private fun getCartItems(userid: String) {
        items_list.clear()
        getCartDetailsViewModel.getCartResponse(userid,latitude,longitude)
        getCartDetailsViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {

                                var arry = response.body()!!.CART_DETAILS

                                    for (i in arry.indices) {
                                        var hashMap: HashMap<String,String> = HashMap()
                                        hashMap.put("product_id", arry[i].PRODUCT_ID)
                                        hashMap.put("shop_id", arry[i].SHOP_ID)
                                        hashMap.put("shop_name", arry[i].SHOP_NAME)
                                        hashMap.put("product_name", arry[i].PRODUCT_TITLE)
                                        hashMap.put("product_image", arry[i].PRODUCT_IMAGE)
                                        hashMap.put("product_price", arry[i].PRODUCT_PRICE)
                                        hashMap.put("product_quantity", arry[i].PRODUCT_QUANTITY)
                                        hashMap.put("quantity_amount", arry[i].QUANTITY_AMOUNT)
                                        hashMap.put("base_price", arry[i].BASE_PRICE)
                                        hashMap.put("base_amount", arry[i].BASE_AMOUNT)
                                        items_list.add(hashMap)
                                    }


                                subtotal.text=response.body()!!.SUBTOTAL
                                var sub_total=response.body()!!.SUBTOTAL.toString().toInt()
                                var distance = response.body()!!.CART_DETAILS[0].SHOP_DISTANCE.toString().toInt()
                                var distance_partial = distance - 2
                                var distance_charge = 35 + distance_partial

                                if(distance.equals("2"))
                                {
                                    var allTotal = sub_total+35
                                    delivery.text = "35"
                                    total.text = allTotal.toString()
                                    total_amt.text = allTotal.toString()
                                    total_price=allTotal.toString()
                                }
                                else
                                {
                                    delivery.text = distance_charge.toString()
                                    var allTotal = sub_total+distance_charge
                                    total.text = allTotal.toString()
                                    total_amt.text = allTotal.toString()
                                    total_price=allTotal.toString()

                                }


                            } else {

                                Toast.makeText(this, "cart is empty", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }
                    }
                    is Resource.Error -> {

                        response.message?.let { message ->

                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {

                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        getAllAddress(userid)
    }

    private fun getAllAddress(userid: String) {
        addressArrayList.clear()
        addAddressModel.getAddressResponse(userid)
        addAddressModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {
                        addressArrayList.clear()
                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                add_address_ll.visibility = View.VISIBLE
                                rl_shipping_address.visibility = View.VISIBLE
                                //edit_address.visibility = View.VISIBLE
                                //saved_address_ll.visibility = View.VISIBLE

                                addressArrayList = response.body()!!.ADDRESS_DETAILS
                                var addressAdapter = AddressAdapter(this, addressArrayList,this)
                                addressList.adapter = addressAdapter
                                addressList.itemAnimator = DefaultItemAnimator()
                                addressList.layoutManager = GridLayoutManager(this, 1)
                            }
                            else if(response.body()!!.status == 204)
                            {
                                addressArrayList.clear()
                                add_address_ll.visibility = View.VISIBLE
                                rl_shipping_address.visibility = View.GONE
                            }
                            else {
                                add_address_ll.visibility = View.VISIBLE
                               // edit_address.visibility = View.GONE
                              //  saved_address_ll.visibility = View.GONE
                                /*Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT)
                                    .show()*/
                            }

                        }
                    }
                    is Resource.Error -> {

                        response.message?.let { message ->

                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {

                    }
                }
            }
        }

    }

    override fun onPaymentSuccess(transactionid: String?) {
        var product = items_list.toString()
        delivery_date = delivery_date+" " +aTime
        submitData(
            userid,
            product,
            total_price,
            "RAZORPAY",
            order_date,
            transactionid.toString(),
            delivery_date,
            AddressAdapter.AddressId)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
    Log.d("Error","Error")
    }

    override fun updateAddressSection(addressId: String, type: String) {
       deleteAddress(addressId)
    }

    private fun deleteAddress(addressId:String) {
        addressDeleteViewModel.deleteAddressData(userid,addressId)

        addressDeleteViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                addressArrayList= ArrayList()
                             getAllAddress(userid)
                            } else {

                            }

                        }
                    }
                    is Resource.Error -> {

                        response.message?.let { message ->

                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {

                    }
                }
            }
        }


    }
}