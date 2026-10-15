package app.f2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.f2.R
import app.f2.data.Equipment
import app.f2.data.GameCatalog
import app.f2.data.GameRepository

class MarketActivity : AppCompatActivity() {

    private lateinit var repo: GameRepository
    private lateinit var container: LinearLayout
    private lateinit var tvBalance: TextView
    private var currentTab = 0 // 0=PC, 1=Cam, 2=Mic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

        repo = GameRepository(this)
        container = findViewById(R.id.marketContainer)
        tvBalance = findViewById(R.id.tvBalance)

        findViewById<Button>(R.id.btnTabPc).setOnClickListener { currentTab = 0; render() }
        findViewById<Button>(R.id.btnTabCam).setOnClickListener { currentTab = 1; render() }
        findViewById<Button>(R.id.btnTabMic).setOnClickListener { currentTab = 2; render() }
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }

        render()
    }

    private fun render() {
        val player = repo.loadPlayer()
        tvBalance.text = "Баланс: ${player.formatMoney()}"
        container.removeAllViews()

        val items: List<Equipment>
        val ownedId: Int
        val bonusLabel: String
        val buyAction: (Int) -> Pair<Boolean, String>

        when (currentTab) {
            0 -> {
                items = GameCatalog.computers
                ownedId = player.computerId
                bonusLabel = "−%d сек. к кулдауну"
                buyAction = { repo.buyComputer(it) }
            }
            1 -> {
                items = GameCatalog.cameras
                ownedId = player.cameraId
                bonusLabel = "+%d просмотров"
                buyAction = { repo.buyCamera(it) }
            }
            else -> {
                items = GameCatalog.microphones
                ownedId = player.mikroId
                bonusLabel = "+%d подписчиков"
                buyAction = { repo.buyMikro(it) }
            }
        }

        val inflater = LayoutInflater.from(this)
        for (item in items) {
            val row = inflater.inflate(R.layout.item_market, container, false)
            val img = row.findViewById<ImageView>(R.id.ivItem)
            val name = row.findViewById<TextView>(R.id.tvName)
            val price = row.findViewById<TextView>(R.id.tvPrice)
            val bonus = row.findViewById<TextView>(R.id.tvBonus)
            val btn = row.findViewById<Button>(R.id.btnBuy)

            val resId = resources.getIdentifier(item.imageRes, "drawable", packageName)
            if (resId != 0) img.setImageResource(resId)
            else img.setImageResource(R.drawable.ic_launcher_foreground)

            name.text = item.name
            price.text = if (item.price <= 0) "Стартовый" else "$${item.price.toInt()}"
            bonus.text = bonusLabel.format(item.bonus)

            if (ownedId == item.id) {
                btn.text = "Куплено"
                btn.isEnabled = false
            } else if (item.price <= 0) {
                btn.visibility = View.GONE
            } else {
                btn.text = "Купить"
                btn.setOnClickListener {
                    val (ok, msg) = buyAction(item.id)
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    if (ok) render()
                }
            }
            container.addView(row)
        }
    }
}
