/*
*
* @author offensive #TDBSoft
* This code implements google multiple consumable in app subscriptions
* using google billing client library
 */
package com.example.smartrecipe.subscription;


//imports
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.SkuType
import com.example.smartrecipe.R
import java.io.IOException
import java.util.*

class SubscriptionActivity : AppCompatActivity(), PurchasesUpdatedListener {
    //initialize billing variables
    var arrayAdapter: ArrayAdapter<String>? = null
    var listView: ListView? = null
    var access_home_btn: ImageView? = null
    private var billingClient: BillingClient? = null
    ////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subscription_activity)
        listView = findViewById<View>(R.id.listview) as ListView

        // Establish connection to billing client
        //check purchase status from google play store cache on every app start
        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases().setListener(this).build()

        //establish a connection to google billing client
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {//check if billing client connection is established and returning true
                    val queryPurchase = billingClient!!.queryPurchases(SkuType.SUBS)//query existing purchases from google sku ids
                    val queryPurchases = queryPurchase.purchasesList  //get the purcahse list
                    if (queryPurchases != null && queryPurchases.size > 0) {//check if purchase list exist otherwise return false
                        handlePurchases(queryPurchases)//call the method to handle purchase query<<<...>>>implemented below in the same code
                    }

                    //check which items are in purchase list and which are not in purchase list
                    //if items that are found add them to purchaseFound
                    //check status of found items and save values to preference
                    //item which are not found simply save false values to their preference
                    //indexOf return index of item in purchase list from 0-2 (because we have 3 items) else returns -1 if not found
                    val purchaseFound = ArrayList<Int>()
                    if (queryPurchases != null && queryPurchases.size > 0) {
                        //check item in purchase list
                        for (p in queryPurchases) {
                            val index = subcribeItemIDs.indexOf(p.sku)
                            //if purchase found
                            if (index > -1) {
                                purchaseFound.add(index)
                                if (p.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                    saveSubscribeItemValueToPref(subcribeItemIDs[index], true)
                                }
                                else {
                                    saveSubscribeItemValueToPref(subcribeItemIDs[index], false)
                                }
                            }
                        }
                        //items that are not found in purchase list mark false
                        //indexOf returns -1 when item is not in foundlist
                        for (i in subcribeItemIDs.indices) {
                            if (purchaseFound.indexOf(i) == -1) {
                                saveSubscribeItemValueToPref(subcribeItemIDs[i], false)
                            }
                        }
                    }
                    //if purchase list is empty that means no item is not purchased/Subscribed
                    //Or purchase is refunded or canceled
                    //so mark them all false
                    else {
                        for (purchaseItem in subcribeItemIDs) {
                            saveSubscribeItemValueToPref(purchaseItem, false)
                        }
                    }
                }
            }
            //implement an overide method to retry connecting to billing client in case of disconnection.
            //the method is in built and is available in the billing api...you only need to call it here
            override fun onBillingServiceDisconnected() {}
        })

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subscribeItemDisplay)
        listView!!.adapter = arrayAdapter
        notifyList()

        listView!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            if (getSubscribeItemValueFromPref(subcribeItemIDs[position])) {
                Toast.makeText(applicationContext, subcribeItemIDs[position] + " is Already Subscribed", Toast.LENGTH_SHORT).show()
                //selected item is already purchased/subscribed
                return@OnItemClickListener
            }
            //initiate purchase on selected product/subscribe item click
            //check if service is already connected
            if (billingClient!!.isReady) {
                initiatePurchase(subcribeItemIDs[position])
            }
            else {
                billingClient = BillingClient.newBuilder(this@SubscriptionActivity).enablePendingPurchases().setListener(this@SubscriptionActivity).build()
                billingClient!!.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            initiatePurchase(subcribeItemIDs[position])
                        } else {
                            Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onBillingServiceDisconnected() {}
                })
            }
        }
    }

    private fun notifyList() {
        subscribeItemDisplay.clear()
        for (p in subcribeItemIDs) {
            if(getSubscribeItemValueFromPref(p)==true) {
                    subscribeItemDisplay.add(
                        "🔵 " + p.toUpperCase() + " Package Subscription Status " + "[✔]" + "(" + getSubscribeItemValueFromPref(
                            p
                        ).toString().replace("true", "active").toUpperCase() + ")"
                    )
                }else{
                    subscribeItemDisplay.add(
                        "🔴 " + p.toUpperCase() + " Package Subscription Status " + "🔕" + "(" + getSubscribeItemValueFromPref(
                            p
                        ).toString().replace("false", "dormant").toUpperCase() + ")"
                    )
            }
        }
        arrayAdapter!!.notifyDataSetChanged()
    }

    private val preferenceObject: SharedPreferences
        private get() = applicationContext.getSharedPreferences(PREF_FILE, 0)
    private val preferenceEditObject: Editor
        private get() {
            val pref = applicationContext.getSharedPreferences(PREF_FILE, 0)
            return pref.edit()
        }

    private fun getSubscribeItemValueFromPref(PURCHASE_KEY: String): Boolean {
        return preferenceObject.getBoolean(PURCHASE_KEY, false)
    }

    private fun saveSubscribeItemValueToPref(PURCHASE_KEY: String, value: Boolean) {
        preferenceEditObject.putBoolean(PURCHASE_KEY, value).commit()
    }

    private fun initiatePurchase(PRODUCT_ID: String) {
        val skuList: MutableList<String> = ArrayList()
        skuList.add(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(SkuType.SUBS)
        val billingResult = billingClient!!.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            billingClient!!.querySkuDetailsAsync(params.build()
            ) { billingResult, skuDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (skuDetailsList != null && skuDetailsList.size > 0) {
                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList[0])
                            .build()
                        billingClient!!.launchBillingFlow(this@SubscriptionActivity, flowParams)
                    }
                    else {
                        //try to add item/product id "s1" "s2" "s3" inside subscription in google play console
                        Toast.makeText(applicationContext, "Subscription package $PRODUCT_ID not Found!", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(applicationContext,
                        " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
        else {
            Toast.makeText(applicationContext,
                "Sorry Subscription not Supported. Please Update Play Store", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        //if item newly purchased
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            val queryAlreadyPurchasesResult = billingClient!!.queryPurchases(SkuType.SUBS)
            val alreadyPurchases = queryAlreadyPurchasesResult.purchasesList
            alreadyPurchases?.let { handlePurchases(it) }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(applicationContext, "Purchase Canceled by user", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            val index = subcribeItemIDs.indexOf(purchase.sku)
            //purchase found
            if (index > -1) {

                //if item is purchased
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                        // Invalid purchase
                        // show error to user
                        Toast.makeText(applicationContext, "Error : Invalid Purchase", Toast.LENGTH_SHORT).show()
                        continue  //skip current iteration only because other items in purchase list must be checked if present
                    }
                    // else purchase is valid
                    //if item is purchased/subscribed and not Acknowledged
                    if (!purchase.isAcknowledged) {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                            .build()
                        billingClient!!.acknowledgePurchase(acknowledgePurchaseParams
                        ) { billingResult ->
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                //if purchase is acknowledged
                                //then saved value in preference
                                saveSubscribeItemValueToPref(subcribeItemIDs[index], true)
                                Toast.makeText(applicationContext, "Subscription for "+subcribeItemIDs[index] + "package  was succeful!", Toast.LENGTH_SHORT).show()
                                notifyList()
                            }
                        }
                    }
                    else {
                        // Grant entitlement to the user on item purchase
                        if (!getSubscribeItemValueFromPref(subcribeItemIDs[index])) {
                            saveSubscribeItemValueToPref(subcribeItemIDs[index], true)
                            Toast.makeText(applicationContext, "Subscription for "+subcribeItemIDs[index]  + "package  was succeful!", Toast.LENGTH_SHORT).show()
                            notifyList()
                        }
                    }
                }
                else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                    Toast.makeText(applicationContext, subcribeItemIDs[index] + " Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show()
                }
                else if (purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                    //mark purchase false in case of UNSPECIFIED_STATE
                    saveSubscribeItemValueToPref(subcribeItemIDs[index], false)
                    Toast.makeText(applicationContext, subcribeItemIDs[index] + " Purchase Status Unknown", Toast.LENGTH_SHORT).show()
                    notifyList()
                }
            }
        }
    }

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     *
     * Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     *
     */
    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        return try {
            //for old playconsole
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            //for new play console
            //To get key go to Developer Console > Select your app > Monetize > Monetization setup
            val base64Key = "Paste the key from monetize setup here. Unique for every app"
            Security.verifyPurchase(base64Key, signedData, signature)
        } catch (e: IOException) {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (billingClient != null) {
            billingClient!!.endConnection()
        }
    }

    companion object {
        const val PREF_FILE = "MyPref"

        //note add unique product ids
        //use same id for preference key
        private val subcribeItemIDs: ArrayList<String> = object : ArrayList<String>() { //ann array method to handle the product ids
            init {
                /*
                *create your in-app prouct ids>go to monetize>subscriptions>create subscription
                * and create product ids with the same ids as the ones created below
                *
                 */
                add("standard")
                add("professional")
                add("advanced")
            }
        }
        private val subscribeItemDisplay = ArrayList<String>()
    }


}