package com.example.xmlformatassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var rvAdapter: RVAdapter
    private lateinit var recyclerView : RecyclerView

    private var list = ArrayList<Student>()
    private var tag:String? = ""
    private var text = ""
    private var id = 0
    private var name = ""
    private var grade = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvMain)
        rvAdapter = RVAdapter(list)
        recyclerView.adapter = rvAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        try {
            val stream = assets.open("Student_Details")

            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(stream, null)

            var event: Int = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                tag = parser.name
                when (event) {
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> when (tag) {
                        "id" -> { id = text.toInt() }
                        "name" -> { name = text }
                        "grade" -> { grade = text.toDouble() }
                        else -> list.add(Student(id,name,grade))
                    }
                }
                event = parser.next()
            }
            rvAdapter.notifyDataSetChanged()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }
    }
}