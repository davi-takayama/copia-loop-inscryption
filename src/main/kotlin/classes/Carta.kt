package classes

abstract class Carta(var vida: Int, var nome: String, var ataque: Int) {
    var sacrificavel: SacrificioEnum = SacrificioEnum.SACRIFICAVEL
    var custo: Int = 0
        protected set

    constructor(nome: String, vida: Int, ataque: Int, sacrificavel: SacrificioEnum, custo: Int) : this(0, "", 0) {
        this.nome = nome
        this.vida = vida
        this.ataque = ataque
        this.sacrificavel = sacrificavel
        this.custo = custo
    }

    open fun atacar(carta: Carta) {
        carta.vida -= this.ataque
    }

    open fun atacar(jogador: Jogador) {
        jogador.danoRecebido += this.ataque
    }


}