## Missing features

1. [] 2FA - Two factor authenticator;
2. [] Exportar para .xlsx;
3. [] Integração do LLM nas restantes funcionalidades de geração de relatórios e sugestões de melhoria;
4. [x/-] JavaDoc e JsDoc;
5. [] Alterar os textos das paǵinas para parecer uma aplicação e não um produto vendido em separado.

## Known bugs:

### Vários:

- [x] EvaluationCycleServiceImpl contém método já apagado que só está a ser chamado em testes???

### Dashboard:

1. [x] Dashboard do user e do gestor necessita de informação para povoar dados;

### Users:

1. [x] Filtro de "Todos os Gestores" -> colocar todos os users;

2. [x] Cartão de participação com design consistente com o de formação.

### Wizard atribuir gestores:

1. [] Opção seleccionar todos lança excepção "User not defined";

### Notificações:

1. [x] Notificações de eventos como fechos de ciclo ou conclusão de avaliações devolvem excepção!

2. [x] Enviar do backend um placeholder para a tradução do New Message from;

### Responsividade:

1. [x] Cartões de avaliação: Nota da avaliação sai do cartão em ecrãs pequenos;

### Excepções:

1. [x] Mapper para excepções do JwtUtil ? (Quando o jwt falha validação, imprime stacktrace desnecessário)

### Avaliações:

1. [x] checkAndCloseEvaluationCycle -> refactor para passar do EvaluationService para o EvaluationCycleService.

2. [X] Caixa de pesquisa está a pesquisar pelo feedback apenas;

3. [x] Após marcar avaliações como fechadas página não faz refresh (Email Assincrono);

4. [] Visão de lista de avaliações para o admin não dá para mostrar avaliações;

---

### Visão Geral do Projeto

O documento descreve um projeto intitulado **Employee Performance Evaluation Management**, que tem como objetivo criar uma ferramenta online para a avaliação do desempenho dos colaboradores numa empresa. O sistema serve para acompanhar as avaliações, registar formações e fornecer feedback de forma estruturada e transparente.

---

### Contexto e Enquadramento

- **Ambiente:**  
  Desenvolvido no âmbito do programa "Acertar o Rumo" (11ª edição), o projeto é realizado pelos alunos, organizados em grupos de 2 (com um grupo de 3, devido ao número ímpar de participantes).
- **Interação com o Cliente:**  
  A Critical Software, que apresenta o desafio, vai acompanhar o desenrolar do projeto através de reuniões e apresentações marcadas numa sequência de datas (desde a apresentação do desafio até à demonstração final do sistema).

---

### Objectivo do Projeto

Criar uma aplicação web que:

- Facilite a avaliação do desempenho dos colaboradores.
- Registe e acompanhe as formações realizadas pelos utilizadores.
- Sirva de motor para o desenvolvimento profissional e fortalecimento da cultura meritocrática na empresa.

O sistema deve garantir que cada colaborador receba avaliações detalhadas e que o seu progresso profissional seja monitorizado de forma contínua.

---

### Funcionalidades Principais

1. **Gestão dos Utilizadores:**

   - Cada utilizador tem um perfil com dados pessoais, histórico de formações e avaliações.
   - O registo é feito por e-mail, com validação e gestão segura da palavra-passe.
   - Existem três perfis:
     - **Administrador:** Tem acesso total à plataforma e pode gerir os perfis dos utilizadores.
     - **Utilizador:** Pode gerir a sua própria página e, se designado, avaliar outros colaboradores.
     - **Utilizador não autenticado:** Tem acesso limitado para o registo inicial e recuperação de palavra-passe.

2. **Avaliações de Desempenho:**

   - O processo de avaliação ocorre em ciclos.
   - O Administrador inicia o ciclo, atribuindo cada colaborador a um gestor (que pode ser um utilizador com funções adicionais).
   - As avaliações são preenchidas através de uma escala de 1 a 4, acompanhadas de feedback escrito.
   - Após a avaliação, o sistema passa por fases: "Em Avaliação", "Concluído" e, finalmente, "Fechado" (ação irrevogável).

3. **Gestão das Formações:**
   - Área exclusiva onde os administradores podem criar, editar e inativar formações.
   - Mantém o histórico das formações que cada utilizador concluiu, permitindo o registo detalhado das horas e do tipo de formação.

---

### Qualidade e Segurança

O projecto inclui várias medidas para garantir um sistema robusto e seguro:

- **Adaptabilidade:**  
  A aplicação deve funcionar bem em telemóveis, tablets e computadores.
- **Testes:**  
  São previstos testes unitários, de API, de integração e de Frontend para assegurar o funcionamento correto do sistema.

- **Sessão e Validação:**  
  A sessão do utilizador expira automaticamente após um determinado período de inatividade.  
  Todos os inputs são validados para evitar ataques (por exemplo, injeção de SQL ou XSS).

- **Armazenamento Seguro:**  
  As palavras-passe são guardadas de forma segura (nunca em texto legível) e é necessário garantir a utilização de palavras-passe fortes.

- **Comunicação Segura:**  
  Toda a comunicação utiliza o protocolo HTTPS e existem mecanismos adicionais de segurança, como autenticação de dois fatores (2FA) ou captcha, para prevenir acessos não autorizados.

---

### Cronograma e Avaliação

- **Datas Importantes:**

  - **5 de Maio:** Apresentação do desafio.
  - **12 de Maio:** Prazo para envio de dúvidas por e-mail.
  - **19 de Maio:** Esclarecimento das dúvidas pelo representante da Critical Software.
  - **29 de Maio:** Apresentação dos protótipos dos ecrãs, com sessões individuais com o cliente.
  - **15 de Julho:** Submissão do relatório final.
  - **18 de Julho:** Defesa e demonstração final do sistema.

- **Avaliação:**  
  A nota final (de 0 a 20) é baseada na qualidade do relatório, na apresentação oral do projeto e na capacidade dos alunos em responder às questões técnicas colocadas pelo júri.

---

### Resumindo

O projeto consiste na criação de uma aplicação web que gere as avaliações de desempenho e as formações dos colaboradores, promovendo assim o seu desenvolvimento profissional e melhorando a transparência e justiça no processo avaliativo. A realização do projeto exige uma boa organização, atenção aos prazos e uma interação eficaz com o cliente durante todas as fases do desenvolvimento.

---
