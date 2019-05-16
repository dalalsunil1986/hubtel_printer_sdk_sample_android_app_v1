package com.maxwellcofie.printers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.hubtel.hubtelprinters.Delegates.PrinterConnectionDelegate
import com.hubtel.hubtelprinters.Delegates.PrinterSeachDelegate
import com.hubtel.hubtelprinters.Delegates.PrintingTaskDelegate
import com.hubtel.hubtelprinters.PrinterManager
import com.hubtel.hubtelprinters.printerCore.Communication
import com.hubtel.hubtelprinters.receiptbuilder.HubtelDeviceInfo
import com.hubtel.hubtelprinters.receiptbuilder.ReceiptObject
import com.hubtel.hubtelprinters.receiptbuilder.ReceiptOrderItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PrinterSeachDelegate, PrinterConnectionDelegate, PrintingTaskDelegate {

    private var printerManager: PrinterManager = PrinterManager()
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this@MainActivity

        printerManager = PrinterManager(this)
        printerManager.seachDelegate = this
        printerManager.connectionDelegate = this
        printerManager.printingTaskDelegate = this

        //Initialise the search job
        searchPrinters()

        //Button clicked
        printer.setOnClickListener {
            initiatePrint()
        }

    }

    private fun searchPrinters() {
        printerManager.searchPrinter()
    }


    private fun initiatePrint() {
        val receiptObject = ReceiptObject()

        val logo = BitmapFactory.decodeResource(resources, R.drawable.unnamed)
        val qrCode = BitmapFactory.decodeResource(resources, R.drawable.default_qrcode)

        receiptObject.logo = Bitmap.createScaledBitmap(logo, 150, 150, false)
        receiptObject.businessName = "Hubtel Limited"
        receiptObject.businessBranch = "Kokomlemle, Accra"
        receiptObject.isOrder = true
        receiptObject.businessPhone = "23323984610"
        receiptObject.businessWebUrl = "www.hubtel.com"
        receiptObject.paymentDate = "14th July 2018"
        receiptObject.paymentType = "MoMo"
        receiptObject.subtotal = "GHS 390"
        receiptObject.businessAddress = "Hotel Road"
        receiptObject.tax = "GHS 10.00"
        receiptObject.discount = "GHS 0.00"
        receiptObject.total = "GHS 400"
        receiptObject.customer = "233240000000"
        receiptObject.employeeName = "Mary Poppins"
        receiptObject.amountPaid = "GHS 400"
        receiptObject.qrcode = Bitmap.createScaledBitmap(qrCode, 250, 250, false)

        val receiptItem = ArrayList<ReceiptOrderItem>()
        receiptItem.add(ReceiptOrderItem("1", "Blue Band", "GHS 30.00"))
        receiptItem.add(ReceiptOrderItem("2", "Milo", "GHS 19.00"))
        receiptItem.add(ReceiptOrderItem("5", "Nido", "GHS 10.00"))
        receiptItem.add(ReceiptOrderItem("1", "Cerelac", "GHS 2.00"))
        receiptItem.add(ReceiptOrderItem("2", "Bread", "GHS 2.00"))
        receiptItem.add(ReceiptOrderItem("7", "Corn Flakes", "GHS 76.00"))
        receiptItem.add(ReceiptOrderItem("3", "Cassava", "GHS 5.00"))
        receiptItem.add(ReceiptOrderItem("2", "Yam", "GHS 12.00"))
        receiptItem.add(ReceiptOrderItem("5", "Plantain", "GHS 20.00"))
        receiptItem.add(ReceiptOrderItem("6", "Ginger", "GHS 9.00"))
        receiptItem.add(ReceiptOrderItem("8", "Coke", "GHS 90.00"))

        receiptObject.items = receiptItem
        printerManager.printOrderPayment(receiptObject)
    }

    override fun printingTaskBegan(deviceInfo: HubtelDeviceInfo?) {
        Toast.makeText(context, "Printing Started ...", Toast.LENGTH_SHORT).show()
    }

    override fun printingTaskFailed(deviceInfo: HubtelDeviceInfo?, error: String?) {
    }

    override fun printingTaskCompleted(deviceInfo: HubtelDeviceInfo?, results: Boolean?) {
        Toast.makeText(context, "Printing Completed!", Toast.LENGTH_SHORT).show()
    }

    override fun printingTaskCompleted(deviceInfo: HubtelDeviceInfo?, results: String?) {
    }

    override fun printingTaskFailed(error: String?) {
    }

    override fun cashDrawertatusReport(result: Communication.Result?) {
        printerManager.openCashDrawer()
    }

    override fun printerConnectionBegan(deviceInfo: HubtelDeviceInfo?) {
    }

    override fun printerConnectionFailed(deviceInfo: HubtelDeviceInfo?, error: String?) {
    }

    override fun printerConnectionSuccess(deviceInfo: HubtelDeviceInfo?) {
        Toast.makeText(context, "Connected to  ${deviceInfo?.portName}", Toast.LENGTH_SHORT).show()
    }

    override fun printerConnectionFailed(error: String?) {
    }

    override fun printerSearchBegan() {
    }

    override fun printerSearchFailed(error: String?) {
    }

    override fun printerSearchCompleted(devices: MutableList<HubtelDeviceInfo>?) {
        printerManager.connectToPrinter(devices?.first())
    }

}
