package classes.criaturas

import classes.Carta
import classes.SacrificioEnum

class CanhaoDeVidro : Carta("Canhao de vidro", 1, 5, SacrificioEnum.SACRIFICAVEL, 2) {
    override fun atacar(carta: Carta) {
        super.atacar(carta)
        this.vida -= 1
    }
}