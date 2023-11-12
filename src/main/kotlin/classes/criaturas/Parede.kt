package classes.criaturas

import classes.Carta
import classes.Jogador
import classes.SacrificioEnum

class Parede : Carta("Parede", 5, 1, SacrificioEnum.NAO_SACRIFICAVEL, 1) {
    override fun atacar(jogador: Jogador) {
        super.atacar(jogador)
        super.atacar(jogador)
    }
}