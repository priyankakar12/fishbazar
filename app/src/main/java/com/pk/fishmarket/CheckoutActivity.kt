package com.pk.fishmarket



import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.Utils.SharedPreferencesUtil
import com.pk.fishmarket.repository.AppRepository
import com.pk.fishmarket.viewmodel.GetAllAddressViewModel
import com.pk.fishmarket.viewmodel.GetCartDetailsViewModel
import com.pk.fishmarket.viewmodel.PlaceOrderViewModel
import com.pk.fishmarket.viewmodel.ViewModelFactory
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
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
    var total_price = ""

    lateinit var total:TextView
    lateinit var delivery:TextView
    lateinit var subtotal:TextView
    lateinit var date_txt:TextView
    lateinit var img_cod_unselect:ImageView
    lateinit var img_cod_select:ImageView
    lateinit var img_online_unselect:ImageView
    lateinit var img_online_select:ImageView
    lateinit var edit_address_txt:ImageView
    var items_list :ArrayList<HashMap<String,String>> = ArrayList()
    var cal = Calendar.getInstance()
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
        val repository = AppRepository()
        val factory = ViewModelFactory(repository)
        latitude= SharedPreferencesUtil().getLat(this).toString();
        longitude= SharedPreferencesUtil().getLong(this).toString();
        userid= SharedPreferencesUtil().getUserId(this).toString();
        addAddressModel = ViewModelProvider(this, factory)[GetAllAddressViewModel::class.java]
        getCartDetailsViewModel = ViewModelProvider(this, factory)[GetCartDetailsViewModel::class.java]
        placeOrderViewModel = ViewModelProvider(this, factory)[PlaceOrderViewModel::class.java]

        order_date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
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
            if(Address_id == "")
            {
                Toast.makeText(this,"Please add an address first",Toast.LENGTH_SHORT).show()
            }
            else if(delivery_date=="")

            {
                Toast.makeText(this,"Please provide delivery date",Toast.LENGTH_SHORT).show()
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

                submitData(
                    userid,
                    product,
                    total_price,
                    payment_mode,
                    order_date,
                    "0",
                    delivery_date,
                    Address_id
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
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView()
                }

            DatePickerDialog(this,R.style.DialogTheme,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
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
        placeOrderViewModel.addOrder(userid,product,totalPrice,paymentMode,orderDate,transactionId,deliveryDate,addressId)
        placeOrderViewModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                            Toast.makeText(this,response.body()!!.message,Toast.LENGTH_SHORT).show()

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
                                var hashMap: HashMap<String,String> = HashMap()
                                var arry = response.body()!!.CART_DETAILS

                                for(i in arry.indices)
                                {
                                    hashMap.set("product_id",arry[i].PRODUCT_ID)
                                    hashMap.set("shop_id",arry[i].SHOP_ID)
                                    hashMap.set("product_name",arry[i].PRODUCT_TITLE)
                                    hashMap.set("product_price",arry[i].PRODUCT_PRICE)
                                    hashMap.set("product_quantity",arry[i].PRODUCT_QUANTITY)
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
        addAddressModel.getAddressResponse(userid)
        addAddressModel.response.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->

                when (response) {

                    is Resource.Success -> {

                        response.data?.let { response ->
                            Log.d("response", response.toString())
                            if (response.body()!!.status == 200) {
                                add_address.visibility = View.GONE
                                edit_address.visibility = View.VISIBLE
                                Address_id = response.body()!!.ADDRESS_DETAILS.ADDRESS_ID
                                Address_one = response.body()!!.ADDRESS_DETAILS.ADDRESS_ONE
                                Address_two = response.body()!!.ADDRESS_DETAILS.ADDRESS_TWO
                                Pincode = response.body()!!.ADDRESS_DETAILS.PINCODE
                                Phone_number = response.body()!!.ADDRESS_DETAILS.PHONE
                                full_name = response.body()!!.ADDRESS_DETAILS.NAME


                                fullname.text = response.body()!!.ADDRESS_DETAILS.NAME
                                phone_edt.text = response.body()!!.ADDRESS_DETAILS.PHONE
                                address.text = response.body()!!.ADDRESS_DETAILS.ADDRESS_ONE+","+response.body()!!.ADDRESS_DETAILS.ADDRESS_TWO+","+response.body()!!.ADDRESS_DETAILS.PINCODE
                                //Toast.makeText(this,"Address Added Successfully", Toast.LENGTH_LONG).show()
                            } else {
                                add_address.visibility = View.VISIBLE
                                edit_address.visibility = View.GONE
                                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT)
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

    override fun onPaymentSuccess(transactionid: String?) {
        var product = items_list.toString()
        submitData(
            userid,
            product,
            total_price,
            "RAZORPAY",
            order_date,
            transactionid.toString(),
            delivery_date,
            Address_id)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
    Log.d("Error","Error")
    }
}