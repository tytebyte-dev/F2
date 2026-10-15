package app.f2.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.f2.R
import app.f2.data.GameRepository

class ChannelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        val repo = GameRepository(this)
        val p = repo.loadPlayer()

        findViewById<TextView>(R.id.tvName).text = p.channelName
        findViewById<TextView>(R.id.tvSubs).text = "Подписчики: ${p.formatSubs()}"
        findViewById<TextView>(R.id.tvViews).text = "Просмотры: ${p.formatViews()}"
        findViewById<TextView>(R.id.tvAuditor).text = "Аудитория: ${p.formatAuditor()}"
        findViewById<TextView>(R.id.tvTheme).text = "Тема: ${p.channelTheme().name}"
        findViewById<TextView>(R.id.tvVideos).text = buildString {
            append("Видео всего: ${p.totalVideos}\n")
            append("Пранки: ${p.prankCount}  Клипы: ${p.clipCount}\n")
            append("Пародии: ${p.parodyCount}  Шоу: ${p.showCount}\n")
            append("Игры: ${p.gameCount}")
        }

        val genderImg = findViewById<ImageView>(R.id.ivGender)
        genderImg.setImageResource(if (p.gender == 1) R.drawable.male else R.drawable.woman)

        // Оборудование
        val pc = p.computer()
        val cam = p.camera()
        val mic = p.mikro()
        setEquipImage(findViewById(R.id.ivPc), pc.imageRes)
        setEquipImage(findViewById(R.id.ivCam), cam.imageRes)
        setEquipImage(findViewById(R.id.ivMic), mic.imageRes)
        findViewById<TextView>(R.id.tvPc).text = pc.name
        findViewById<TextView>(R.id.tvCam).text = cam.name
        findViewById<TextView>(R.id.tvMic).text = mic.name

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }

    private fun setEquipImage(iv: ImageView, resName: String) {
        val id = resources.getIdentifier(resName, "drawable", packageName)
        if (id != 0) iv.setImageResource(id)
    }
}
