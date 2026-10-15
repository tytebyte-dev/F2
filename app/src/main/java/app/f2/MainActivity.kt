package app.f2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.f2.data.GameRepository
import app.f2.ui.CabinetActivity
import app.f2.ui.CreateChannelActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = GameRepository(this)
        if (repo.hasPlayer()) {
            startActivity(Intent(this, CabinetActivity::class.java))
        } else {
            startActivity(Intent(this, CreateChannelActivity::class.java))
        }
        finish()
    }
}
