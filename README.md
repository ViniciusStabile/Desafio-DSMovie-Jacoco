# Desafio DSMovie Jacoco

## Objetivo
Implementar todos os testes unitários de service para o projeto DSMovie.

## Sobre o projeto DSMovie
O DSMovie é um projeto de filmes e avaliações de filmes. A visualização dos dados dos filmes é pública (não necessita login), mas as alterações de filmes (inserir, atualizar, deletar) são permitidas apenas para usuários com a role ADMIN. As avaliações de filmes podem ser registradas por qualquer usuário logado, seja CLIENT ou ADMIN.

### Funcionalidades do Projeto
- **Visualização de Filmes:** Qualquer usuário, logado ou não, pode visualizar os dados dos filmes.
- **Alterações de Filmes:** Somente usuários ADMIN podem inserir, atualizar ou deletar filmes.
- **Avaliação de Filmes:** Qualquer usuário logado (CLIENT ou ADMIN) pode registrar suas avaliações.

### Entidades Principais
- **Movie:** Armazena os dados dos filmes, incluindo a média das notas e a contagem de votos.
- **Score:** Armazena uma nota de 0 a 5 que cada usuário deu a cada filme.

### Regras de Negócio
1. **Visualização de Dados:** Pública, sem necessidade de login.
2. **Alterações de Filmes:** Restritas a usuários com role ADMIN.
3. **Registro de Avaliações:** Permitido para usuários logados (CLIENT ou ADMIN).
4. **Cálculo de Média de Notas:** Sempre que um usuário registra uma nota, o sistema calcula a média das notas de todos os usuários e armazena essa média na entidade Movie, juntamente com a contagem de votos.

### Exemplo de Funcionamento
- Um usuário logado registra uma nota para um filme.
- O sistema atualiza a média das notas desse filme e a contagem total de votos.


### MovieServiceTests
- `findAllShouldReturnPagedMovieDTO`
- `findByIdShouldReturnMovieDTOWhenIdExists`
- `findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist`
- `insertShouldReturnMovieDTO`
- `updateShouldReturnMovieDTOWhenIdExists`
- `updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist`
- `deleteShouldDoNothingWhenIdExists`
- `deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist`
- `deleteShouldThrowDatabaseExceptionWhenDependentId`

### ScoreServiceTests
- `saveScoreShouldReturnMovieDTO`
- `saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId`

### UserServiceTests
- `authenticatedShouldReturnUserEntityWhenUserExists`
- `authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists`
- `loadUserByUsernameShouldReturnUserDetailsWhenUserExists`
- `loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists`
