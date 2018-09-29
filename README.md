# TP1: Batalha entre Batalhões

![](docs/pathfinding-still.gif)

Pré-requisitos:
  1. Um pouco de Java
  1. NetBeans com _plugin_ _Gradle Support_ instalado
  1. Conhecimento sobre grafos

Objetivos:

1. Praticar o uso de algoritmos de planejamento de caminhos em grafos
1. Implementar **heurísticas para o algoritmo A***
1. Perceber que o **algoritmo de Dijkstra é o A\* que não usa heurística**
1. Entender o que é uma **heurística admissível** e o impacto do uso
   de uma que não é

## Atividade Prática

Você deve começar usando o código do professor como ponto de partida para a
atividade. Você deve implementar 3 heurísticas para o algoritmo A*:

1. Uma heurística "nula", que transforme o A* no Dijkstra
2. A heurística de distância Euclidiana
3. (Opcional) Uma outra heurística à sua escolha (e pesquisa ;)

## Sobre o código

Descrevemos um **Agente** (`Agent.java`) por:

1. Algoritmo de movimentação (_steering_)
1. Algoritmo de planejamento
   - Que, por sua vez, pode conter uma função heurística

A movimentação acontece em três passos. Assim que um clique é feito:

1. **Passo de Planejamento**: Um algoritmo de planejamento (no caso, A*)
   encontra a melhor rota para o ponto desejado.
1. **Passo de Movimentação**: Um algoritmo de movimentação (no caso, _seek_
   estático) determina para onde o agente deve ir. O objetivo (_target_)
   do algoritmo é definido sempre como o próximo nó do caminho retornado
   pelo passo de planejamento.
1. **Passo de Física**: Usamos integração de Euler para atualizar a posição do
   agente.

Um algoritmo de controle identifica se o passo de movimentação cumpriu
seu objetivo e, em caso afirmativo, define o objetivo como o próximo nó do
caminho. Veja o trecho de código de `Agent.java:58`:

```java

```

O Agente (`Agent.java:95`) recebe um evento de clique quando o usuário clica em
uma parte do mapa. Nesse momento, acionamos o algoritmo de planejamento para
traçar a rota:

```java

```

---
