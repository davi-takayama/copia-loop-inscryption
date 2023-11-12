import classes.Carta
import classes.Jogador
import classes.SacrificioEnum
import classes.criaturas.Abelha
import classes.criaturas.CanhaoDeVidro
import classes.criaturas.Lobo
import kotlin.math.abs
import kotlin.random.Random

fun main(args: Array<String>) {
    println("Digite seu nome:")
    val nome = readln()

    val listaDeCartas = (0..2).toList()
    val baralho: MutableList<Carta> = mutableListOf()
    for (i in 1..10) {
        baralho.add(
            when (listaDeCartas.shuffled().first()) {
                0 -> Abelha()
                1 -> CanhaoDeVidro()
                else -> Lobo()
            }
        )
    }

    val jogador = Jogador(nome, baralho, mutableListOf())
    val inimigo = Jogador("Dalva", baralho, mutableListOf())

    while (true) {
        if (abs(jogador.danoRecebido - inimigo.danoRecebido) > 5) {
            println("Fim de jogo")
            println("\n" + if (jogador.danoRecebido < inimigo.danoRecebido) "Você venceu!" else "Você perdeu!")
            return
        }

        printJogo(jogador, inimigo)
        var emTurno = true

//        fase de compra
        println("\nEscolha o que comprar:\n1 - Esquilo\n2 - Carta do baralho (" + jogador.baralho.size + " restantes)")
        var escolha = readlnOrNull().toString()
        while (escolha != "1" && escolha != "2") {
            println("Escolha inválida")
            escolha = readlnOrNull().toString()
        }
        if (escolha == "1")
            jogador.comprarEsquilo()
        else
            jogador.comprarCarta()

//        fase de acoes
        while (emTurno) {
            println("\n--------------------------------------------------------------")
            printJogo(jogador, inimigo)
            emTurno = jogador.turno()
            inimigo.mesa = inimigo.removerCartasmortas()
            jogador.mesa = jogador.removerCartasmortas()
        }

//        fase de ataques
        for ((index, it) in jogador.mesa.withIndex()) {
            if (it != null) {
                if (inimigo.mesa[index] != null) {
                    it.atacar(inimigo.mesa[index]!!)
                    if (inimigo.mesa[index]!!.vida <= 0) {
                        inimigo.mesa[index] = null
                    }
                } else {
                    it.atacar(inimigo)
                }
            }
        }


//        turno do inimigo

//        escolher aleatoriamente entre comprar carta ou esquilo
        if (inimigo.mao.size == 0) {
            inimigo.comprarEsquilo()
        } else {
            if (inimigo.baralho.size > 0) {
                if (Random.nextBoolean()) inimigo.comprarCarta() else inimigo.comprarEsquilo()
            } else {
                inimigo.comprarEsquilo()
            }
        }


        var turnoInimigo = true
        while (turnoInimigo) {
            if (inimigo.mao.size > 0) {
//            escolher aleatoriamente entre colocar carta na mesa ou encerrar o turno
                if (Random.nextBoolean()) {
                    val carta = inimigo.mao.shuffled().first()
                    if (carta.custo > 0 && inimigo.mesa.filter { it?.sacrificavel == SacrificioEnum.SACRIFICAVEL }.size >= carta.custo) {
                        var num = inimigo.mesa.filter { it?.sacrificavel == SacrificioEnum.SACRIFICAVEL }.size
                        while (num > 0) {
                            inimigo.mesa = inimigo.mesa.map {
                                if (it?.sacrificavel == SacrificioEnum.SACRIFICAVEL) {
                                    num--
                                    null
                                } else {
                                    it
                                }
                            }.toMutableList()
                        }
                    } else {
                        var pos = Random.nextInt(4)
                        while (inimigo.mesa[pos] != null) {
                            pos = Random.nextInt(4)
                        }
                        inimigo.mesa[pos] = carta
                    }
                    inimigo.mao.remove(carta)
                } else {
                    turnoInimigo = false
                }
            } else {
                turnoInimigo = false
            }
            jogador.mesa = jogador.removerCartasmortas()
            inimigo.mesa = inimigo.removerCartasmortas()
        }

        for ((index, it) in inimigo.mesa.withIndex()) {
            if (it != null) {
                if (jogador.mesa[index] != null) {
                    it.atacar(jogador.mesa[index]!!)
                    if (jogador.mesa[index]!!.vida <= 0) {
                        jogador.mesa[index] = null
                    }
                } else {
                    it.atacar(jogador)
                }
            }
        }
    }
}

fun printJogo(jogador: Jogador, inimigo: Jogador) {
    val tamanhoMaximo = jogador.mesa.maxOfOrNull { it?.nome?.length ?: 0 } ?: 0
    val tamanhoMaximoInimigo = inimigo.mesa.maxOfOrNull { it?.nome?.length ?: 0 } ?: 0

    val tamanhoMaximoNome = if (tamanhoMaximo > tamanhoMaximoInimigo) tamanhoMaximo else tamanhoMaximoInimigo

    println("\n--------------------------------------------------------------")
    println("Mesa de ${inimigo.nome} (dano recebido: ${inimigo.danoRecebido})")
    printMesa(inimigo, tamanhoMaximoNome)
    printMesa(jogador, tamanhoMaximoNome)
    println("Mesa de ${jogador.nome} (dano recebido: ${jogador.danoRecebido})")
}

fun printMesa(jogador: Jogador, max: Int) {
    var espacos = ""
    for (j in 0..max) {
        espacos += " "
    }

    for (i in 0..3) {
        val carta = jogador.mesa[i]
        if (carta != null) {
            print("[${carta.nome.padEnd(max, ' ')} (${carta.vida} ❤️ - ${carta.ataque} ⚔️)] ")
        } else {
            print("[${espacos}             ] ")
        }
    }
    println()
}