package classes.criaturas

import classes.Carta
import classes.SacrificioEnum

class Lobo : Carta("Lobo", 2, 3, SacrificioEnum.SACRIFICAVEL, 2) {
    override fun atacar(carta: Carta) {
        super.atacar(carta)
        if (carta.vida < this.ataque) this.vida += 1
    }
}