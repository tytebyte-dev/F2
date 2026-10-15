package app.f2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.f2.R
import app.f2.data.GameCatalog
import app.f2.data.GameRepository

class AchievementsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        val repo = GameRepository(this)
        val player = repo.loadPlayer()
        val container = findViewById<LinearLayout>(R.id.achContainer)
        val inflater = LayoutInflater.from(this)

        findViewById<TextView>(R.id.tvHeader).text =
            "Подписчики: ${player.formatSubs()}  •  Видео: ${player.totalVideos}"

        for (a in GameCatalog.achievements) {
            val unlocked = player.subs >= a.requiredSubs
            val row = inflater.inflate(R.layout.item_achievement, container, false)
            val img = row.findViewById<ImageView>(R.id.ivAch)
            val title = row.findViewById<TextView>(R.id.tvTitle)
            val status = row.findViewById<TextView>(R.id.tvStatus)

            val resId = resources.getIdentifier(a.imageRes, "drawable", packageName)
            if (resId != 0) img.setImageResource(resId)
            img.alpha = if (unlocked) 1f else 0.3f

            title.text = a.title
            status.text = if (unlocked) "Получено!" else "Нужно ${a.requiredSubs} подп."
            container.addView(row)
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }
}
