package app.f2.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import app.f2.R
import app.f2.data.GameRepository

class CreateChannelActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_channel)

        val etName = findViewById<EditText>(R.id.etChannelName)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val btnCreate = findViewById<Button>(R.id.btnCreate)

        btnCreate.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.length < 2) {
                Toast.makeText(this, "Введите название канала (мин. 2 символа)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val gender = if (findViewById<RadioButton>(R.id.rbMale).isChecked) 1 else 0
            GameRepository(this).createPlayer(name, gender)
            startActivity(Intent(this, CabinetActivity::class.java))
            finish()
        }
    }
}
