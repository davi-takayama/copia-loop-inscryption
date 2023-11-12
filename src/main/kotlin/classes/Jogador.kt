package classes

import classes.criaturas.Esquilo

class Jogador(
    val nome: String,
    var baralho: MutableList<Carta>,
    var mao: MutableList<Carta>,
) {
    var mesa: MutableList<Carta?> = listOf(null, null, null, null).toMutableList()
    var danoRecebido: Int = 0
    fun comprarEsquilo() {
        mao.addLast(Esquilo())
    }

    fun comprarCarta() {
        val carta = baralho.first()
        mao.addLast(carta)
        baralho.removeFirst()
    }

    private fun colocarCartaNaMesa(carta: Carta) {
        if (carta.custo == 0) {
            mao = mao.filter { it != carta }.toMutableList()
            escolherPosicao(carta)
        } else {
            var custoSacrificio = carta.custo
            var sacrificar = ""
            while (sacrificar != "S" && sacrificar != "N") {
                println("Custo da carta: ${carta.custo}. Quantidade de pontos de sacrifício: ${mesa.filter { it?.sacrificavel == SacrificioEnum.SACRIFICAVEL }.size}")
                if (carta.custo > mesa.filter { it?.sacrificavel == SacrificioEnum.SACRIFICAVEL }.size) {
                    println("Não é possível colocar a carta na mesa")
                    return
                }
                println("Sacrificar cartas para colocar a carta na mesa? (S/N)")
                sacrificar = readlnOrNull().toString()

                when (sacrificar) {
                    "S" -> {
                        val pontosSacrificio = mesa.filter { it?.sacrificavel == SacrificioEnum.SACRIFICAVEL }.size
                        if (pontosSacrificio >= carta.custo) {
                            mao = mao.filter { it != carta }.toMutableList()
                            while (custoSacrificio > 0) {
                                val cartaSacrificio = mesa.first { it?.sacrificavel == SacrificioEnum.SACRIFICAVEL }
                                cartaSacrificio?.sacrificavel = SacrificioEnum.SACRIFICADA
                                custoSacrificio--
                            }
                            this.mesa = (mesa.map {
                                if (it?.sacrificavel == SacrificioEnum.SACRIFICAVEL) {
                                    null
                                } else {
                                    it
                                }
                            }).toMutableList()
                            escolherPosicao(carta)
                        } else {
                            println("Não é possível colocar a carta na mesa")
                        }
                    }

                    "N" -> {
                        println("Carta não colocada na mesa")
                    }

                    else -> {
                        println("Opção inválida")
                    }
                }


            }
        }
    }

    private fun escolherPosicao(carta: Carta) {
        var pos: String
        while (true) {
            println("Posição da carta na mesa (1, 2, 3 ou 4)")
            pos = readlnOrNull().toString()
            if (pos == "1" || pos == "2" || pos == "3" || pos == "4") {
                if (mesa[pos.toInt() - 1] == null) {
                    break
                } else {
                    println("Posição ocupada")
                }
                break
            } else {
                println("Posição inválida")
            }
        }
        when (pos) {
            "1" -> {
                mesa[0] = carta
            }

            "2" -> {
                mesa[1] = carta
            }

            "3" -> {
                mesa[2] = carta
            }

            "4" -> {
                mesa[3] = carta
            }
        }
    }

    private fun printMao() {
        println("Sua mão:")
        println("vida - nome - ataque")
        println("0 - cancelar")
        for ((index, it) in this.mao.withIndex()) {
//            println("${index + 1} - [${it.vida} - ${it.nome} - ${it.ataque}] (custo: ${it.custo}) | ")
//            print("[${carta.nome.padEnd(max, ' ')} (${carta.vida} ❤️ - ${carta.ataque} ⚔️)] ")
            println("${index + 1} - [${it.nome} (${it.vida} ❤️ - ${it.ataque} ⚔️ - ${it.custo} 💰)]")
        }
        println()
    }

    fun turno(): Boolean {
        println("\nEscolha uma ação:\n1 - Colocar carta na mesa\n2 - Encerrar o turno")
        var escolha = readlnOrNull().toString()
        while (escolha != "1" && escolha != "2") {
            println("Escolha inválida")
            escolha = readlnOrNull().toString()
        }
        return if (escolha == "1") {
            println("\n--------------------------------------------------------------")
            println("\nEscolha uma carta para colocar na mesa:")
            this.printMao()

            escolha = readlnOrNull().toString()
            while (escolha.toIntOrNull() == null || escolha.toInt() > this.mao.size || escolha.toInt() < 1) {
                println("Escolha inválida")
                escolha = readlnOrNull().toString()
            }
            if (escolha.toInt() != 0)
                this.colocarCartaNaMesa(this.mao[escolha.toInt() - 1])
            true
        } else {
            false
        }
    }

    fun removerCartasmortas(): MutableList<Carta?> {
        return (mesa.map {
            if (it != null && it.vida <= 0) {
                null
            } else {
                it
            }
        }).toMutableList()
    }
}