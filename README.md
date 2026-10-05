# PetApp 🐾 — Trabalho 2 (Navegação + Listas + Detalhes)

App de petshop em Kotlin + Jetpack Compose.
Protótipos no Figma: https://www.figma.com/design/daCJDgpDFjniHfibaGx3km/PetApp?m=auto&t=apR3iFe8Cosik8Er-6

## Como rodar
1. Abrir a pasta do projeto no Android Studio.
2. Aguardar o Gradle Sync (baixa o `navigation-compose`).
3. Rodar o módulo `app` em um emulador ou celular (minSdk 24).
4. Na tela de login basta tocar em **Entrar** (não há validação de credenciais neste trabalho).

## Telas (8)
| # | Tela | Rota | O que faz |
|---|------|------|-----------|
| 1 | Login | `login` | Entrar, cadastro, "esqueci a senha" (diálogo), Apple/Google (entram no app) |
| 2 | Cadastro | `cadastro` | Formulário de conta; voltar/cadastrar retornam ao login |
| 3 | Início | `inicio` | Resumo: pet principal, serviços (abrem a Agenda já com o serviço marcado), próximo agendamento |
| 4 | Pets (**Lista 1**) | `pets` | Adicionar, remover e abrir detalhes de pets |
| 5 | Detalhe do Pet | `pet/{petId}` | Informações calculadas, edição de peso e agendamentos do pet |
| 6 | Agenda (**Lista 2**) | `agenda` | Agendar, marcar como concluído, remover e abrir detalhes |
| 7 | Detalhe do Agendamento | `agendamento/{agendamentoId}` | Pet do agendamento + valor calculado |
| 8 | Conta | `conta` | Estatísticas, atalhos e sair |

## Organização do código
- `Rotas.kt` — objeto `Rotas` com todas as rotas nomeadas (e helpers `detalhePet(id)`, `detalheAgendamento(id)`).
- `AppNavigation.kt` — `NavHost`, `NavigationBar` e a função `irParaAba(...)`.
- `Modelos.kt` — data classes `Pet` e `Agendamento`, catálogo `Servicos` e a classe `DadosApp` (as duas `mutableStateListOf`).
- `PetsScreen.kt`, `AgendaScreen.kt` — as duas listas (`LazyColumn` + `Card`).
- `DetalhePetScreen.kt`, `DetalheAgendamentoScreen.kt` — as duas telas de detalhes.
- `InicioScreen.kt`, `ContaScreen.kt`, `LoginScreen.kt`, `CadastroScreen.kt` — demais telas.

---

# Decisões do projeto

## Como estava o projeto no Trabalho 1 e o que mudou?
No Trabalho 1 o app tinha 3 telas (Login, Cadastro e Início) trocadas por uma variável `telaAtual` com `when` no `MainActivity`. A barra inferior da Home era só desenho (`clickable {}` vazio), e os cards e a lista de serviços eram dados fixos no código.

No Trabalho 2 trocamos o `when` por `NavHost`/`NavController` com um objeto `Rotas`, criamos uma barra inferior real (`NavigationBar`), dois tipos de item (`Pet` e `Agendamento`) em listas reativas e telas de detalhes com argumento de rota. A Home passou a ler os dados reais das listas.

## Por que essas telas novas?
O app é de petshop, então o que o cliente mais gerencia são os seus pets e os agendamentos de serviços. Por isso as duas listas:
- **Pets** — cadastrar e remover pets. É a base de tudo, pois o preço e o agendamento dependem do pet.
- **Agenda** — criar agendamentos (pet, serviço, data, horário e observações), marcar como concluído e remover.
- **Detalhe do Pet e Detalhe do Agendamento** — mostram o item exato que foi tocado.
- **Conta** — resumo, atalhos e o botão Sair, que volta ao login.

## Decisões de configuração e organização do código
- **Rotas em um objeto** (`Rotas`), sem strings soltas pelo código.
- **Estado das listas em `DadosApp`**, criado com `remember` acima do `NavHost`. Assim os dados não se perdem ao trocar de tela, e Home, Pets, Agenda e Detalhes enxergam as mesmas listas.
- **Detalhes recebem só o `id`** pela rota (`pet/{petId}` e `agendamento/{agendamentoId}`, com `NavType.IntType`) e buscam o item em `DadosApp`. Se o item sumir, a tela mostra "não encontrado".
- **Navegação nas abas** com `popUpTo(Rotas.INICIO)` e `launchSingleTop`, para o botão voltar não empilhar abas repetidas.
- **Tema fixo claro**: as telas usam fundos claros fixos, então desativamos o tema escuro e as cores dinâmicas para o texto não ficar ilegível.
- **Botões que no Trabalho 1 não faziam nada** ganharam função: "Esqueci a senha" abre um diálogo, Apple/Google entram no app, o sino abre a Agenda, "Trocar" abre os Pets e os cards de serviço abrem a Agenda com o serviço já selecionado.

## Complexidade extra na tela de Detalhes
O **Detalhe do Pet** faz mais do que reexibir os campos:
1. **Informações calculadas**: idade em anos humanos, porte (pelo peso) e a tabela de preços de cada serviço ajustada pelo porte.
2. **Edição na própria tela**: um campo atualiza o peso, e o porte e os preços são recalculados na hora.
3. **Combina as duas listas**: mostra os agendamentos daquele pet, e tocar em um abre o Detalhe do Agendamento (navegação secundária). Há também o botão "Agendar serviço", que abre a Agenda com o pet já selecionado.

O **Detalhe do Agendamento** também combina as listas: mostra o pet do agendamento (tocar abre o detalhe do pet) e calcula o valor estimado pelo porte.

Escolhemos essa complexidade porque ela faz sentido para um petshop: o preço de banho e tosa depende do tamanho do animal, então calcular o valor pelo porte é uma regra real do negócio. Ela também liga as duas listas, e isso mostra o uso de passagem de dados entre telas de um jeito que o exemplo de aula não cobria.

## Dificuldades do grupo
- **Passar o item certo para a tela de Detalhes.** No começo não ficou claro como levar o item da lista até o detalhe. Resolvemos passando só o `id` na rota e buscando o item no `DadosApp`, o que garante que o item certo apareça e evita que o dado fique desatualizado.
- **Manter as listas ao trocar de tela.** Se o estado ficasse dentro de cada tela, os itens adicionados sumiam ao navegar. Resolvemos criando o `DadosApp` uma única vez acima do `NavHost`.
- **Botão voltar e barra inferior.** A navegação entre abas empilhava telas repetidas. Resolvemos com `popUpTo` e `launchSingleTop`.
- **Rodar o projeto pela primeira vez.** O emulador demorou a iniciar e o Android Studio mostrou o erro "already running". Esperamos o Gradle terminar, selecionamos a configuração `app` e clicamos em Run uma única vez.