sealed class Creature(
    private val attack: Int,
    private val protection: Int,
    private val damage: IntRange,
    maxHealth: Int
) {

    protected var health = maxHealth

    val isAlive: Boolean
        get() = health > 0

    val isDie: Boolean
        get() = !isAlive

    fun attack(creature: Creature): Boolean {
        val attackModifier = attack - creature.protection + 1
        var successAttack = false
        for (i in 0 until maxOf(1, attackModifier)) {
            if ((1..6).random() >= 5) {
                successAttack = true
                break
            }
        }
        if (successAttack) creature.takeDamage(damage.random())
        return successAttack
    }

    private fun takeDamage(damage: Int) {
        if (damage <= 0) return
        health = maxOf(0, health - damage)
    }
}

class Player(
    private val maxHealth: Int = 100,
    attack: Int = (1..30).random(),
    protection: Int = (1..30).random(),
    damage: IntRange = 1..6
) : Creature(attack, protection, damage, maxHealth) {

    private val restoreHealthModifier = (maxHealth * HEALTH_MULTIPLIER).toInt()

    fun restoreHealth() {
        health = minOf(maxHealth, health + restoreHealthModifier)
    }

    companion object {
        private const val HEALTH_MULTIPLIER = 0.3
    }
}

class Monster(
    attack: Int = (1..30).random(),
    protection: Int = (1..30).random(),
    maxHealth: Int = 100,
    damage: IntRange = 1..6
) : Creature(attack, protection, damage, maxHealth)

fun main() {
    val monster = Monster()
    val player = Player()
    while (monster.isAlive && player.isAlive) {
        player.attack(monster)
        if (monster.isDie) break
        if (monster.attack(player) && player.isAlive) {
            player.restoreHealth()
        }
    }
    if (monster.isAlive) {
        println("Monster win")
    } else {
        println("Player win")
    }
}