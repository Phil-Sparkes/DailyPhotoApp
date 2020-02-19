package com.example.selfiegoals

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import java.io.File

class GalleryGoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_goal)

        val imageLayout = findViewById<LinearLayout>(R.id.imageLayout)
        val stopMotionBtn = findViewById<Button>(R.id.btn_stopMotion)
        val currentGoal = intent.getStringExtra(EXTRA_MESSAGE)

        val imageFiles = loadImageFiles(currentGoal)
        var imageID = 0

        for (imageFile in imageFiles){
            imageBitmap = (BitmapFactory.decodeFile(imageFile.getAbsolutePath()))
            makeImageView(imageBitmap, imageLayout, imageID)
            imageID += 1
        }
        stopMotionBtn.setOnClickListener {
            val intentStopMotionActivity = Intent(this, StopMotionActivity::class.java)
            intent = intentStopMotionActivity.apply { putExtra(EXTRA_MESSAGE, currentGoal)}
            startActivity(intent)
        }
    }

    private fun makeImageView(imageBitmap: Bitmap, constraintLayout: LinearLayout, imageID: Int) {
        // creates a new image view
        val newImageView = ImageView(this)

        // sets the image of the image view
        newImageView.setImageBitmap(imageBitmap)

        //gives the imageview an ID
        newImageView.id = imageID

        // adds it to the constraint layout so visible
        constraintLayout.addView(newImageView)

        // creating a context menu for the imageView (context menu is when you hold click and more options appear)
        registerForContextMenu(newImageView)
    }

    private fun loadImageFiles(currentGoal: String): MutableList<File> {
        // get path to the storage directory
        val path = Environment.getExternalStorageDirectory()
        // create an array of all the files in the directory
        val files: Array<File> = path.listFiles()
        // create a mutableList (Mutable just means you can add to it) of Files
        val imageFiles = mutableListOf<File>()
        // search through all the files and get the ones that are associated with current goal
        for (file in files) {
            if (file.getName().startsWith(currentGoal))
                // add the file to the list
                imageFiles.add(file)
        }
        // return the list of Files
        return imageFiles
    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val imageView = v as ImageView
        val imageID = imageView.id

        // give context menu buttons with group id matching gid
        menu!!.add(imageID, v!!.id, 0, "Delete Image")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // loop through context menu items to see which was clicked
        when (item.title) {
            "Delete Image" -> {
                deleteImageFile(item.groupId)
                return true
            }
        }
        return true
    }
    private fun deleteImageFile(FileID: Int) {
        val currentGoal = intent.getStringExtra(EXTRA_MESSAGE)
        val imageFiles = loadImageFiles(currentGoal)
        val fileToDelete = imageFiles[FileID]

        fileToDelete.delete()
        val intentGoalGalleryActivity = Intent(this, GalleryGoalActivity::class.java)

        intent = intentGoalGalleryActivity.apply { putExtra(EXTRA_MESSAGE, intent.getStringExtra(EXTRA_MESSAGE))}
        startActivity(intent)

    }
}
