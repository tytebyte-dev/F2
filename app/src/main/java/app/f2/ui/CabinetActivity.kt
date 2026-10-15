package app.f2.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import app.f2.R
import app.f2.data.GameRepository
import app.f2.data.Player

class CabinetActivity : BaseActivity() {

    private lateinit var repo: GameRepository
    private lateinit var tvChannel: TextView
    private lateinit var tvSubs: TextView
    private lateinit var tvViews: TextView
    private lateinit var tvMoney: TextView
    private lateinit var tvAuditor: TextView
    private lateinit var tvCooldown: TextView
    private lateinit var btnRecord: Button
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateCooldown()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet)

        repo = GameRepository(this)
        tvChannel = findViewById(R.id.tvChannelName)
        tvSubs = findViewById(R.id.tvSubs)
        tvViews = findViewById(R.id.tvViews)
        tvMoney = findViewById(R.id.tvMoney)
        tvAuditor = findViewById(R.id.tvAuditor)
        tvCooldown = findViewById(R.id.tvCooldown)
        btnRecord = findViewById(R.id.btnRecord)

        findViewById<Button>(R.id.btnMarket).setOnClickListener {
            startActivity(Intent(this, MarketActivity::class.java))
        }
        findViewById<Button>(R.id.btnChannel).setOnClickListener {
            startActivity(Intent(this, ChannelActivity::class.java))
        }
        findViewById<Button>(R.id.btnAchievements).setOnClickListener {
            startActivity(Intent(this, AchievementsActivity::class.java))
        }
        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnRecord.setOnClickListener { showRecordDialog() }
    }

    override fun onResume() {
        super.onResume()
        refresh()
        handler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
    }

    private fun refresh() {
        val p = repo.loadPlayer()
        tvChannel.text = p.channelName
        tvSubs.text = "Подписчики: ${p.formatSubs()}"
        tvViews.text = "Просмотры: ${p.formatViews()}"
        tvMoney.text = "Деньги: ${p.formatMoney()}"
        tvAuditor.text = "Аудитория: ${p.formatAuditor()}"
        updateCooldown()
    }

    private fun updateCooldown() {
        val p = repo.loadPlayer()
        if (p.canRecordVideo()) {
            tvCooldown.text = "Можно записывать видео"
            btnRecord.isEnabled = true
            btnRecord.alpha = 1f
        } else {
            val left = p.remainingCooldown()
            tvCooldown.text = "Запись через ${Player.formatTime(left)}"
            btnRecord.isEnabled = false
            btnRecord.alpha = 0.5f
        }
    }

    private fun showRecordDialog() {
        val themes = app.f2.data.GameCatalog.videoThemes
        val names = themes.map { it.name }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Выберите тему ролика")
            .setItems(names) { _, which ->
                val result = repo.recordVideo(themes[which].id)
                AlertDialog.Builder(this)
                    .setTitle(if (result.success) "Успех!" else "Подождите")
                    .setMessage(result.message)
                    .setPositiveButton("Хорошо") { _, _ -> refresh() }
                    .show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
