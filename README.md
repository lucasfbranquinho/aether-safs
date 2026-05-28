# AETHER 2.0 - Surface Autonomous Fleet System (SAFS)

**FIAP Global Solution 2026 - 1 Semestre - Engenharia de Software**  
**Disciplina: Domain Driven Design com Java**  
**Professor: Eduardo Ramos**

---

## Contexto

Em maio de 2024, 478 municípios do Rio Grande do Sul foram atingidos pela maior enchente da história do estado. A resposta humanitária foi limitada pela impossibilidade de acesso terrestre e aéreo a diversas areas afetadas.

O AETHER 2.0 (Autonomous Exploration & Terrain Handling Emergency Rover) e um sistema de controle de frota de sondas autonomas terrestres inspirado no programa Artemis da NASA, com engenharia reversa aplicada ao contexto de desastres climaticos brasileiros. O sistema gerencia sondas capazes de navegar por terrenos alagados, lamacosos e com escombros para realizar entregas de kits de sobrevivencia e mapeamento das areas atingidas.

---

## Tecnologias

- Java 21
- Maven 3.x
- Arquitetura DDD (Domain-Driven Design)

---

## Como executar

**Pre-requisitos:** JDK 21+ e Maven instalados.

```powershell
# Configurar o Java (necessario toda vez que abrir o terminal)
$env:JAVA_HOME = "C:\Users\lucas\AppData\Local\Programs\Eclipse Adoptium\jdk-25.0.3.9-hotspot"

# Compilar e executar
mvn compile exec:java
```

Sera exibido um menu interativo no terminal para operar a frota de sondas.

---

## Arquitetura - Domain Driven Design

O projeto segue a arquitetura em 4 camadas do DDD:

```
src/main/java/br/com/fiap/space/
|
|-- presentation/          # Camada de apresentacao (interface com o usuario)
|   `-- Main.java          # Menu interativo via Scanner
|
|-- application/           # Camada de aplicacao (orquestra os casos de uso)
|   `-- MissaoService.java
|
|-- domain/                # Camada de dominio (regras de negocio)
|   |-- model/             # Entidades e agregados
|   |   |-- Sonda.java             # Entidade abstrata principal
|   |   |-- SondaMineradora.java   # Sonda de entrega de kits
|   |   |-- SondaExploradora.java  # Sonda de mapeamento
|   |   |-- CentroDeComando.java   # Agregado raiz (Singleton)
|   |   |-- Recurso.java           # Enum de recursos
|   |   `-- Terreno.java           # Enum de tipos de terreno
|   |-- valueobject/       # Objetos de valor (imutaveis)
|   |   |-- Coordenada.java
|   |   |-- NivelEnergia.java
|   |   `-- CompartimentoKits.java
|   |-- interfaces/        # Contratos do dominio
|   |   |-- Recarregavel.java
|   |   `-- SondaRepository.java
|   `-- exception/         # Excecoes de dominio
|       |-- BateriaCriticaException.java
|       |-- CargaExcedidaException.java
|       `-- TerrenoInvalidoException.java
|
`-- infrastructure/        # Camada de infraestrutura (implementacoes tecnicas)
    |-- SondaRepositoryImpl.java
    `-- factory/
        `-- SondaFactory.java
```

---

## Padroes de Projeto Implementados

### 1. Template Method - `Sonda.java`

A classe abstrata `Sonda` define o esqueleto da rotina autonoma com 4 passos fixos. As subclasses implementam apenas o passo especifico de cada tipo de sonda.

```
executarRotinaAutonoma():
  Passo 1 - validarSistema()     -> verifica bateria e terreno
  Passo 2 - mover()              -> desloca a sonda e consome bateria
  Passo 3 - realizarAcaoLocal()  -> HOOK: cada sonda faz sua acao especifica
  Passo 4 - enviarRelatorio()    -> envia dados ao Centro de Comando
```

### 2. Factory Method - `SondaFactory.java`

Centraliza a criacao de sondas, evitando que o codigo cliente conheça os detalhes de instanciacao.

```java
SondaFactory.criar("ENTREGA")    // retorna SondaMineradora
SondaFactory.criar("MAPEAMENTO") // retorna SondaExploradora
```

### 3. Singleton - `CentroDeComando.java`

Garante que exista apenas uma instancia do Centro de Comando controlando toda a frota durante a execucao do sistema.

```java
CentroDeComando centro = CentroDeComando.getInstance();
```

---

## Classes do Dominio

### Sonda (abstrata)

Entidade base de todas as sondas. Possui id unico, bateria (NivelEnergia), posicao atual (Coordenada) e terreno atual. Implementa a interface `Recarregavel`.

### SondaMineradora

Especializada em entrega de kits de sobrevivencia. Possui compartimento de carga com capacidade maxima de 50 kg. Nao opera em terreno SUBMERSO.

Recursos que pode transportar: `AGUA`, `KIT_MEDICO`, `COMIDA`, `RADIO`.

### SondaExploradora

Especializada em mapeamento de areas. Equipada com sensor de 500m de alcance. Captura 12 imagens por missao e registra as areas varridas.

### CentroDeComando (Singleton)

Gerencia toda a frota, registra sondas ativas e contabiliza missoes concluidas. Ponto unico de controle do sistema.

---

## Value Objects

Objetos imutaveis que representam conceitos do dominio sem identidade propria:

| Classe | Descricao |
|---|---|
| `Coordenada` | Par (x, y) no grid de operacao. Calcula distancia euclidiana entre dois pontos. |
| `NivelEnergia` | Capacidade atual e maxima da bateria. Operacoes de consumo e recarga retornam nova instancia. |
| `CompartimentoKits` | Volume ocupado e maximo da carga da SondaMineradora. Imutavel por design. |

---

## Terrenos

O consumo de bateria varia conforme o terreno percorrido:

| Terreno | Multiplicador de consumo |
|---|---|
| PLANICIE | 1.0x |
| ALAGADO | 1.8x |
| LAMA | 2.5x |
| ESCOMBROS | 3.0x |
| SUBMERSO | bloqueado para SondaMineradora |

---

## Excecoes de Dominio

| Excecao | Quando e lancada |
|---|---|
| `BateriaCriticaException` | Bateria abaixo de 10% ao iniciar missao |
| `CargaExcedidaException` | Kit ultrapassa capacidade do compartimento |
| `TerrenoInvalidoException` | SondaMineradora tenta operar em terreno SUBMERSO |

---

## Diagrama de Classes

O diagrama completo esta disponivel no arquivo `aether-uml.puml` na raiz do projeto (requer extensao PlantUML para visualizacao no VS Code).

---

## Equipe

| Integrante | RM | Responsabilidade |
|---|---|---|
| Lucas Branquinho | 562262 | DDD-Java, Dynamic Programming (Python), Network Architect (Cisco) |
| Vitor Bordalo | 561592 | Database Design (Oracle SQL), Agile & Squads, AR/VR Modelagem 3D |

**Tema unificado:** Rovers autonomos de resgate climatico inspirados no programa Artemis da NASA, aplicados ao desastre do Rio Grande do Sul de maio de 2024.
