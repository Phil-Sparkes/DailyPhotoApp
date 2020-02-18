package com.example.selfiegoals

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        // get the main vertical layout
        val verticalLayout = findViewById<LinearLayout>(R.id.linearLayout)

        // open and search through the db
        val db = GoalDB(this)
        val goals = db.selectAllGoal()

        for (goal in goals) {
            // for each goal make a horizontal layout
            makeHorizontalLayout(goal.gname!!, verticalLayout)
        }
        db.close()

    }

    private fun makeImageView(imageBitmap: Bitmap, constraintLayout: LinearLayout) {
        // creates a new image view
        val newImageView = ImageView(this)

        // sets the image of the image view
        newImageView.setImageBitmap(imageBitmap)

        // adds it to the constraint layout so visible
        constraintLayout.addView(newImageView)
    }

    private fun loadImages(currentGoal: String): MutableList<Bitmap> {
        // get path to the storage directory
        val path = Environment.getExternalStorageDirectory()
        // create an array of all the files in the directory
        val files: Array<File> = path.listFiles()
        // create a mutableList (Mutable just means you can add to it) of bitmaps
        val imageBitmaps = mutableListOf<Bitmap>()
        // search through all the files and get the ones that are associated with current goal
        for (file in files) {
            if (file.getName().startsWith(currentGoal))
                // get the bitmaps from these image files
                imageBitmaps.add(BitmapFactory.decodeFile(file.getAbsolutePath()))
        }
        // return the list of bitmaps
        return imageBitmaps
    }

    private fun makeHorizontalLayout(currentGoal: String, verticalLayout: LinearLayout) {
        // horizontal layout so can scroll side to side with the image groups

        // creates new scrollview to allow horizontal scrolling
        val newScrollView = HorizontalScrollView(this)
        // new horizontal linear layout
        val newHorizontalLayout = LinearLayout(this)
        // new text view
        val newTextView = TextView(this)

        // sets the text view text to be current goal
        newTextView.text = currentGoal

        // changing some settings of the horizontal layout
        newHorizontalLayout.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        newHorizontalLayout.orientation = LinearLayout.HORIZONTAL

        // adds the text view and scroll view to the main vertical layout
        verticalLayout.addView(newTextView)
        verticalLayout.addView(newScrollView)

        // adds the horizontal layout to the scroll view, scroll view can only have one child, best to use a constraint layout for this
        newScrollView.addView(newHorizontalLayout)

        // gets the images from the files and makes image views for these images
        var imageBitmaps = loadImages(currentGoal)
        for (imageBitmap in imageBitmaps) {
            makeImageView(imageBitmap,newHorizontalLayout)
        }

        newHorizontalLayout.setOnClickListener{
            val intentGoalGalleryActivity = Intent(this, GalleryGoalActivity::class.java)
            intent = intentGoalGalleryActivity.apply { putExtra(EXTRA_MESSAGE, currentGoal)}
            startActivity(intent)
        }
    }
}

