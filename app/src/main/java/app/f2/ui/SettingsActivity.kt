package app.f2.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.f2.R
import app.f2.data.GameCatalog
import app.f2.data.GameRepository

class SettingsActivity : AppCompatActivity() {

    private lateinit var repo: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        repo = GameRepository(this)
        val player = repo.loadPlayer()

        findViewById<EditText>(R.id.etName).setText(player.channelName)
        findViewById<TextView>(R.id.tvCurrentTheme).text = "Текущая тема: ${player.channelTheme().name}"

        findViewById<Button>(R.id.btnRename).setOnClickListener {
            val name = findViewById<EditText>(R.id.etName).text.toString()
            val (ok, msg) = repo.renameChannel(name)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        val themeContainer = findViewById<LinearLayout>(R.id.themeContainer)
        for (t in GameCatalog.channelThemes) {
            if (t.id == 0) continue
            val btn = Button(this).apply {
                text = "${t.name} (+${t.bonusSubs} подп / +${t.bonusViews} просм) — $${t.price.toInt()}"
                setOnClickListener {
                    val (ok, msg) = repo.buyChannelTheme(t.id)
                    Toast.makeText(this@SettingsActivity, msg, Toast.LENGTH_SHORT).show()
                    if (ok) {
                        findViewById<TextView>(R.id.tvCurrentTheme).text =
                            "Текущая тема: ${repo.loadPlayer().channelTheme().name}"
                    }
                }
            }
            themeContainer.addView(btn)
        }

        findViewById<Button>(R.id.btnReset).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Сброс прогресса")
                .setMessage("Удалить весь прогресс и начать заново?")
                .setPositiveButton("Да") { _, _ ->
                    repo.resetProgress()
                    startActivity(Intent(this, CreateChannelActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()
                }
                .setNegativeButton("Нет", null)
                .show()
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }
}
