package com.example.countdowntimer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var seekbar:SeekBar
    lateinit var playbt:FloatingActionButton
    lateinit var pausebt:FloatingActionButton
    lateinit var counttext:TextView
    var isActive:Boolean = false
    lateinit var mediaPlayer1:MediaPlayer
    lateinit var mediaPlayer2:MediaPlayer
    lateinit var counttimer:CountDownTimer
    lateinit var resetbt:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekbar = findViewById(R.id.seektime)
        playbt = findViewById(R.id.play)
        pausebt = findViewById(R.id.pause)
        resetbt = findViewById(R.id.reset)
        counttext = findViewById(R.id.timeleft)
        mediaPlayer1 = MediaPlayer.create(applicationContext,R.raw.runningsound)
        mediaPlayer2 = MediaPlayer.create(applicationContext,R.raw.finalsound)
        seekbar.max = 1800
        seekbar.progress = 0
        seekbar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateSeek(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        onPlaybuttonClicked()
        onPausebuttonClicked()
        onResetbuttonClicked()
    }
    fun onPlaybuttonClicked(){
        playbt.setOnClickListener {
            if(seekbar.progress == 0){
                Toast.makeText(applicationContext,"enter time!",Toast.LENGTH_SHORT).show()
            }else {
                startTimer((seekbar.progress * 1000).toLong())
            }
        }
    }
    fun onPausebuttonClicked(){
        pausebt.setOnClickListener {
            if(seekbar.progress == 0)
                Toast.makeText(applicationContext,"at 0 can't paused",Toast.LENGTH_SHORT).show()
            else onpausetimer()
        }
    }
    fun onResetbuttonClicked(){
        resetbt.setOnClickListener {
            if(seekbar.progress == 0)
                Toast.makeText(applicationContext,"already 0!",Toast.LENGTH_SHORT).show()
            else resetSeek()
        }
    }
    fun onpausetimer(){
        if(mediaPlayer1.isPlaying) mediaPlayer1.pause()
        seekbar.isEnabled = false
        counttimer.cancel()
        isActive = false
    }
    private fun updateSeek(progress: Int) {
        var minutes = progress/60
        var seconds = progress%60
        var finTime:String = ""
        if(seconds<=9){
            finTime = "0$seconds"
        }else{
            finTime = seconds.toString()
        }
        seekbar.progress = progress
        counttext.text
            if(minutes<10) {
                counttext.text = "0$minutes:$finTime"
            }else{
                counttext.text = "$minutes:$finTime"
            }
    }
    private fun startTimer(prg:Long){
        if(!isActive){
            isActive = true
            seekbar.isEnabled = false
            counttimer = object:CountDownTimer(prg,1000){
                override fun onTick(millisUntilFinished: Long) {
                    updateSeek((millisUntilFinished/1000).toInt())
                    mediaPlayer1.start()
                }
                override fun onFinish() {
                    resetSeek()
                    mediaPlayer2.start()
                }
            }.start()
        }
        else{
            resetSeek()
        }
    }
    private fun resetSeek() {
        counttext.text = "00:00"
        seekbar.progress = 0
        counttimer.cancel()
        seekbar.isEnabled = true
        isActive = false
        mediaPlayer1.seekTo(0)
        mediaPlayer1.pause()
    }
    override fun onDestroy() {
        super.onDestroy()
        if(isActive) counttimer.cancel()
    }
}