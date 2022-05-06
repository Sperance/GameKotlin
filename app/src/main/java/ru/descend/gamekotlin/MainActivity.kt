package ru.descend.gamekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val person = Warrior("КарабасБарабас", 100)
        val enemyOne = Enemy("Годзилла", 1000)

        log("ENEMY STAT: ${enemyOne.stats.printStat()}")

        val helmet = Helmet("Шлем силы")
        helmet.changeStats {
            it.add(Defence(14))
            it.add(Health(52))
        }

        enemyOne.addItem(helmet)
        enemyOne.addItem(helmet)

        log("ENEMY STAT NEW: ${enemyOne.stats.printStat()}")

        log("HELMET STATS: ${helmet.stats.printStat()}")

        val weapon = Weapon("Меч смерти")
        weapon.changeStats {
            it.add(Damage(30, 10))
            it.add(Speed(0, 5))
        }

        person.equipItem(helmet)
        person.equipItem(weapon)

        log(" ОРУЖИЕ ЭКИПИРОВАНО: ${person.weapon?.nameItem}")

        person.characterHealth = 23
        person.setMaxHealth(23)
    }
}

