training_conversations = [
    # Authentication and Account Management - English
    {
        "input": "How can I login to the system?",
        "output": "To login, go to the login page and enter your email address and password. If you don't have an account yet, you'll need to register first using a valid email address."
    },
    {
        "input": "I forgot my password, what should I do?",
        "output": "Click on 'Forgot Password' on the login page and enter your email address. You'll receive a password reset link via email. Follow the instructions to create a new password."
    },
    {
        "input": "How do I register for an account?",
        "output": "On the login page, click 'Register' and fill out the registration form with your personal information including first name, last name, email, phone number, address, birthdate, workplace, and optionally a biography. After submitting, check your email for a confirmation link."
    },
    {
        "input": "Can I update my profile information?",
        "output": "Yes! Go to your profile page where you can update your personal information, biography, and upload a profile picture. You can also view your evaluation history and training participations there."
    },
    {
        "input": "How do I change my profile picture?",
        "output": "In your profile page, click on the profile picture area and select a new image file to upload. The system supports common image formats. You can also reset to the default picture if needed."
    },

    # Authentication and Account Management - Portuguese
    {
        "input": "Como posso fazer login no sistema?",
        "output": "Para fazer login, aceda à página de entrada e introduza o seu endereço de email e palavra-passe. Se ainda não tem uma conta, terá de se registar primeiro usando um endereço de email válido."
    },
    {
        "input": "Esqueci-me da minha palavra-passe, o que devo fazer?",
        "output": "Clique em 'Esqueci-me da Palavra-passe' na página de login e introduza o seu endereço de email. Receberá uma ligação para redefinir a palavra-passe por email. Siga as instruções para criar uma nova palavra-passe."
    },
    {
        "input": "Como me registo para uma conta?",
        "output": "Na página de login, clique em 'Registar' e preencha o formulário de registo com as suas informações pessoais incluindo nome próprio, apelido, email, número de telefone, morada, data de nascimento, local de trabalho e opcionalmente uma biografia. Após submeter, verifique o seu email para uma ligação de confirmação."
    },
    {
        "input": "Posso atualizar as informações do meu perfil?",
        "output": "Sim! Aceda à sua página de perfil onde pode atualizar as suas informações pessoais, biografia e carregar uma fotografia de perfil. Também pode ver o seu histórico de avaliações e participações em formações."
    },
    {
        "input": "Como altero a minha fotografia de perfil?",
        "output": "Na sua página de perfil, clique na área da fotografia de perfil e selecione um novo ficheiro de imagem para carregar. O sistema suporta formatos de imagem comuns. Também pode repor a imagem predefinida se necessário."
    },

    # Evaluation Management - User Perspective - English
    {
        "input": "How can I view my evaluations?",
        "output": "Access the 'Evaluations' section in the main menu to see all your evaluations. You can filter by evaluation cycle, status (ongoing, completed, closed), or date range to find specific evaluations."
    },
    {
        "input": "What do the evaluation statuses mean?",
        "output": "Evaluation statuses are: 'Ongoing' (currently being filled out by your manager), 'Completed' (finished by manager), and 'Closed' (final and cannot be modified). Only completed and closed evaluations are visible to the evaluated employee."
    },
    {
        "input": "Who evaluates me and when?",
        "output": "Your direct manager evaluates your performance during evaluation cycles. Evaluation cycles are initiated by administrators and have specific start and end dates. You'll receive notifications when a cycle begins."
    },
    {
        "input": "Can I see my evaluation scores?",
        "output": "Yes, once your manager completes your evaluation, you can see your performance rating (scale 1-4) and the feedback comments provided by your manager in the evaluations section."
    },
    {
        "input": "What is an evaluation cycle?",
        "output": "An evaluation cycle is a time period (with start and end dates) during which all employee performance evaluations are conducted. Administrators create cycles, and during active cycles, managers evaluate their team members."
    },

    # Evaluation Management - User Perspective - Portuguese
    {
        "input": "Como posso ver as minhas avaliações?",
        "output": "Aceda à secção 'Avaliações' no menu principal para ver todas as suas avaliações. Pode filtrar por ciclo de avaliação, estado (em curso, concluída, fechada) ou intervalo de datas para encontrar avaliações específicas."
    },
    {
        "input": "O que significam os estados das avaliações?",
        "output": "Os estados das avaliações são: 'Em curso' (atualmente a ser preenchida pelo seu gestor), 'Concluída' (terminada pelo gestor) e 'Fechada' (final e não pode ser modificada). Apenas as avaliações concluídas e fechadas são visíveis ao funcionário avaliado."
    },
    {
        "input": "Quem me avalia e quando?",
        "output": "O seu gestor direto avalia o seu desempenho durante os ciclos de avaliação. Os ciclos de avaliação são iniciados pelos administradores e têm datas específicas de início e fim. Receberá notificações quando um ciclo começar."
    },
    {
        "input": "Posso ver as minhas pontuações de avaliação?",
        "output": "Sim, uma vez que o seu gestor complete a sua avaliação, pode ver a sua classificação de desempenho (escala 1-4) e os comentários de feedback fornecidos pelo seu gestor na secção de avaliações."
    },
    {
        "input": "O que é um ciclo de avaliação?",
        "output": "Um ciclo de avaliação é um período de tempo (com datas de início e fim) durante o qual todas as avaliações de desempenho dos funcionários são conduzidas. Os administradores criam ciclos, e durante os ciclos ativos, os gestores avaliam os membros das suas equipas."
    },

    # Evaluation Management - Manager Perspective - English
    {
        "input": "How do I evaluate my team members?",
        "output": "As a manager, go to the Evaluations section and look for evaluations assigned to you during the current evaluation cycle. Click on each evaluation to provide a rating (1-4 scale) and written feedback for your team members."
    },
    {
        "input": "What should I include in evaluation feedback?",
        "output": "Provide constructive and specific feedback about the employee's performance, achievements, areas for improvement, and recommendations for future development. Be objective and focus on observable behaviors and results."
    },
    {
        "input": "Can I modify an evaluation after submitting it?",
        "output": "You can modify ongoing evaluations until you mark them as completed. Once an evaluation is completed, only administrators can close evaluations during the cycle closure process."
    },
    {
        "input": "How do I know which team members to evaluate?",
        "output": "The system automatically creates evaluations for all your direct reports when an evaluation cycle starts. You'll see them listed in your evaluations dashboard with 'Ongoing' status."
    },

    # Evaluation Management - Manager Perspective - Portuguese
    {
        "input": "Como avalio os membros da minha equipa?",
        "output": "Como gestor, aceda à secção Avaliações e procure pelas avaliações atribuídas a si durante o ciclo de avaliação atual. Clique em cada avaliação para fornecer uma classificação (escala 1-4) e feedback escrito para os membros da sua equipa."
    },
    {
        "input": "O que devo incluir no feedback da avaliação?",
        "output": "Forneça feedback construtivo e específico sobre o desempenho do funcionário, conquistas, áreas para melhoria e recomendações para desenvolvimento futuro. Seja objetivo e foque-se em comportamentos observáveis e resultados."
    },
    {
        "input": "Posso modificar uma avaliação depois de a submeter?",
        "output": "Pode modificar avaliações em curso até as marcar como concluídas. Uma vez que uma avaliação está concluída, apenas os administradores podem fechar avaliações durante o processo de encerramento do ciclo."
    },
    {
        "input": "Como sei quais membros da equipa avaliar?",
        "output": "O sistema cria automaticamente avaliações para todos os seus subordinados diretos quando um ciclo de avaliação começa. Verá-os listados no seu painel de avaliações com o estado 'Em curso'."
    },

    # Training Management - User Perspective - English
    {
        "input": "How do I find available trainings?",
        "output": "Go to the 'Trainings' section to browse all available training programs. You can filter by training area, language (EN/PT), duration, or search by title to find relevant trainings."
    },
    {
        "input": "How do I register for a training?",
        "output": "Only managers or administrators can register you for trainings. Your manager can add training participations to your profile, specifying which training you completed and when."
    },
    {
        "input": "Can I see my training history?",
        "output": "Yes! In your profile page, you'll find a 'Training Participations' section showing all trainings you've completed, including dates, training details, and yearly statistics of training hours."
    },
    {
        "input": "Who can assign trainings to me?",
        "output": "Your direct manager or system administrators can register training participations for you. They have access to see which trainings you haven't participated in yet."
    },
    {
        "input": "What information is shown for each training?",
        "output": "For each training, you can see the title, description, duration (in minutes), training area, language, external link (if available), and whether it's currently active."
    },

    # Training Management - User Perspective - Portuguese
    {
        "input": "Como encontro as formações disponíveis?",
        "output": "Aceda à secção 'Formações' para navegar por todos os programas de formação disponíveis. Pode filtrar por área de formação, idioma (EN/PT), duração ou pesquisar por título para encontrar formações relevantes."
    },
    {
        "input": "Como me registo numa formação?",
        "output": "Apenas gestores ou administradores podem registá-lo em formações. O seu gestor pode adicionar participações em formação ao seu perfil, especificando que formação completou e quando."
    },
    {
        "input": "Posso ver o meu histórico de formações?",
        "output": "Sim! Na sua página de perfil, encontrará uma secção 'Participações em Formação' mostrando todas as formações que completou, incluindo datas, detalhes da formação e estatísticas anuais de horas de formação."
    },
    {
        "input": "Quem me pode atribuir formações?",
        "output": "O seu gestor direto ou administradores do sistema podem registar participações em formação para si. Eles têm acesso para ver em que formações ainda não participou."
    },
    {
        "input": "Que informação é mostrada para cada formação?",
        "output": "Para cada formação, pode ver o título, descrição, duração (em minutos), área de formação, idioma, ligação externa (se disponível) e se está atualmente ativa."
    },

    # Training Management - Manager/Admin Perspective - English
    {
        "input": "How do I assign training to team members?",
        "output": "As a manager, you can view your team members' profiles and add training participations. Click 'Add Participation' to register that an employee completed a specific training on a given date."
    },
    {
        "input": "How do I create a new training program?",
        "output": "As an administrator, go to the Training Management section and click 'New Training'. Fill in the title, description, duration, select a training area, language, and optionally provide an external link."
    },
    {
        "input": "Can I create new training areas?",
        "output": "Yes, administrators can create new training areas by clicking 'New Area' in the Training Management section. Training areas help organize trainings into categories."
    },
    {
        "input": "How do I deactivate a training?",
        "output": "Administrators can deactivate trainings in the Training Management section. Deactivated trainings are no longer visible to regular users but remain in the system for historical purposes."
    },
    {
        "input": "Can I see which trainings a team member hasn't taken?",
        "output": "Yes, managers and administrators can view unparticipated trainings for any team member in their profile, making it easier to recommend relevant training programs."
    },

    # Training Management - Manager/Admin Perspective - Portuguese
    {
        "input": "Como atribuo formação aos membros da equipa?",
        "output": "Como gestor, pode ver os perfis dos membros da sua equipa e adicionar participações em formação. Clique em 'Adicionar Participação' para registar que um funcionário completou uma formação específica numa determinada data."
    },
    {
        "input": "Como crio um novo programa de formação?",
        "output": "Como administrador, aceda à secção Gestão de Formação e clique em 'Nova Formação'. Preencha o título, descrição, duração, selecione uma área de formação, idioma e opcionalmente forneça uma ligação externa."
    },
    {
        "input": "Posso criar novas áreas de formação?",
        "output": "Sim, os administradores podem criar novas áreas de formação clicando em 'Nova Área' na secção Gestão de Formação. As áreas de formação ajudam a organizar as formações em categorias."
    },
    {
        "input": "Como desativo uma formação?",
        "output": "Os administradores podem desativar formações na secção Gestão de Formação. As formações desativadas deixam de ser visíveis aos utilizadores regulares mas permanecem no sistema para fins históricos."
    },
    {
        "input": "Posso ver que formações um membro da equipa não frequentou?",
        "output": "Sim, gestores e administradores podem ver formações não participadas para qualquer membro da equipa no seu perfil, facilitando a recomendação de programas de formação relevantes."
    },

    # Messaging and Communication - English
    {
        "input": "How do I send messages to colleagues?",
        "output": "Go to the Messages section, click 'New Message' to select a user from the list, then type and send your message. You can have ongoing conversations with multiple colleagues."
    },
    {
        "input": "How do I know if I have unread messages?",
        "output": "The system shows a notification badge with the number of unread messages in the main navigation. You'll also see unread messages highlighted in your conversations list."
    },
    {
        "input": "Are messages delivered in real-time?",
        "output": "Yes! The system uses WebSocket technology for real-time message delivery. You'll receive messages instantly when other users are online, and see read receipts when messages are viewed."
    },
    {
        "input": "Can I see when someone read my message?",
        "output": "Yes, the system provides read receipts. You can see when your messages have been read by the recipient through visual indicators in the conversation."
    },
    {
        "input": "How do I start a conversation with someone?",
        "output": "In the Messages section, click 'New Message', select the person you want to message from the user list, and start typing. This creates a new conversation thread."
    },

    # Messaging and Communication - Portuguese
    {
        "input": "Como envio mensagens aos colegas?",
        "output": "Aceda à secção Mensagens, clique em 'Nova Mensagem' para selecionar um utilizador da lista, depois escreva e envie a sua mensagem. Pode ter conversas contínuas com múltiplos colegas."
    },
    {
        "input": "Como sei se tenho mensagens não lidas?",
        "output": "O sistema mostra um distintivo de notificação com o número de mensagens não lidas na navegação principal. Também verá mensagens não lidas destacadas na sua lista de conversas."
    },
    {
        "input": "As mensagens são entregues em tempo real?",
        "output": "Sim! O sistema usa tecnologia WebSocket para entrega de mensagens em tempo real. Receberá mensagens instantaneamente quando outros utilizadores estão online, e verá confirmações de leitura quando as mensagens são visualizadas."
    },
    {
        "input": "Posso ver quando alguém leu a minha mensagem?",
        "output": "Sim, o sistema fornece confirmações de leitura. Pode ver quando as suas mensagens foram lidas pelo destinatário através de indicadores visuais na conversa."
    },
    {
        "input": "Como inicio uma conversa com alguém?",
        "output": "Na secção Mensagens, clique em 'Nova Mensagem', selecione a pessoa a quem quer enviar mensagem da lista de utilizadores e comece a escrever. Isto cria um novo tópico de conversa."
    },

    # AI Assistant and Chatbot - English
    {
        "input": "What can the AI assistant help me with?",
        "output": "The AI assistant can help with information about trainings, evaluation guidance, system functionality questions, and general support. You can access it through the floating chat button or in the Messages section."
    },
    {
        "input": "How do I talk to the virtual assistant?",
        "output": "Click the robot icon (floating chatbot button) on any page, or go to Messages and select 'Virtual Assistant'. Then type your questions and the AI will provide helpful responses."
    },
    {
        "input": "Can the AI assistant access my personal data?",
        "output": "The AI assistant provides general guidance and information but doesn't access your personal evaluation data or private information. It's designed to help with system usage and general questions."
    },

    # AI Assistant and Chatbot - Portuguese
    {
        "input": "Com que pode o assistente de IA ajudar-me?",
        "output": "O assistente de IA pode ajudar com informações sobre formações, orientação de avaliação, questões de funcionalidade do sistema e suporte geral. Pode acedê-lo através do botão de chat flutuante ou na secção Mensagens."
    },
    {
        "input": "Como falo com o assistente virtual?",
        "output": "Clique no ícone do robô (botão de chatbot flutuante) em qualquer página, ou aceda a Mensagens e selecione 'Assistente Virtual'. Depois escreva as suas questões e a IA fornecerá respostas úteis."
    },
    {
        "input": "O assistente de IA pode aceder aos meus dados pessoais?",
        "output": "O assistente de IA fornece orientação geral e informação mas não acede aos seus dados pessoais de avaliação ou informação privada. Foi concebido para ajudar com o uso do sistema e questões gerais."
    },

    # Administrative Functions - English
    {
        "input": "How do I start a new evaluation cycle?",
        "output": "As an administrator, go to the Evaluation Cycles section and click 'Start New Cycle'. Set the start and end dates for the evaluation period. All managers will be notified and evaluations will be automatically created."
    },
    {
        "input": "How do I assign managers to employees?",
        "output": "Administrators can assign managers in the User Management section. Select users and assign their managers using the bulk manager assignment feature. All users except administrators need an assigned manager."
    },
    {
        "input": "Can I close evaluation cycles early?",
        "output": "Administrators can close evaluation cycles and mark specific evaluations as closed in the evaluation management interface. This prevents further modifications to those evaluations."
    },
    {
        "input": "How do I view system statistics?",
        "output": "Administrators have access to various statistics including evaluation completion rates, user management stats (managed vs unmanaged users), and system usage metrics in the dashboard."
    },
    {
        "input": "What reports can I generate?",
        "output": "The system supports generating detailed PDF reports for evaluations, including all evaluation data, feedback, and participant information. These can be exported for documentation purposes."
    },

    # Administrative Functions - Portuguese
    {
        "input": "Como inicio um novo ciclo de avaliação?",
        "output": "Como administrador, aceda à secção Ciclos de Avaliação e clique em 'Iniciar Novo Ciclo'. Defina as datas de início e fim para o período de avaliação. Todos os gestores serão notificados e as avaliações serão criadas automaticamente."
    },
    {
        "input": "Como atribuo gestores aos funcionários?",
        "output": "Os administradores podem atribuir gestores na secção Gestão de Utilizadores. Selecione utilizadores e atribua os seus gestores usando a funcionalidade de atribuição em massa de gestores. Todos os utilizadores exceto administradores precisam de um gestor atribuído."
    },
    {
        "input": "Posso fechar ciclos de avaliação antecipadamente?",
        "output": "Os administradores podem fechar ciclos de avaliação e marcar avaliações específicas como fechadas na interface de gestão de avaliação. Isto previne modificações adicionais a essas avaliações."
    },
    {
        "input": "Como vejo as estatísticas do sistema?",
        "output": "Os administradores têm acesso a várias estatísticas incluindo taxas de conclusão de avaliação, estatísticas de gestão de utilizadores (utilizadores geridos vs não geridos) e métricas de uso do sistema no painel de controlo."
    },
    {
        "input": "Que relatórios posso gerar?",
        "output": "O sistema suporta a geração de relatórios PDF detalhados para avaliações, incluindo todos os dados de avaliação, feedback e informação dos participantes. Estes podem ser exportados para fins de documentação."
    },

    # System Navigation and General Help - English
    {
        "input": "I cannot access the system",
        "output": "Verify your login credentials are correct. If you're having trouble, try clearing your browser cache or try a different browser. For persistent issues, contact your system administrator for assistance."
    },
    {
        "input": "What browsers are supported?",
        "output": "The system works best with modern browsers like Chrome, Firefox, Safari, and Edge. Make sure your browser is up to date and has JavaScript enabled for full functionality."
    },
    {
        "input": "Is the system mobile-friendly?",
        "output": "Yes! The system is responsive and adapts to mobile devices and tablets. You can access all features from your phone or tablet browser."
    },
    {
        "input": "How do I logout safely?",
        "output": "Click on your profile menu in the top-right corner and select 'Logout'. This will securely end your session and return you to the login page."
    },
    {
        "input": "What are the different user roles?",
        "output": "There are three user roles: Administrator (full system access), Manager (can evaluate team members and manage trainings), and User (can view their evaluations and trainings, send messages)."
    },
    {
        "input": "Can I change the interface language?",
        "output": "Yes, the system supports multiple languages. Look for the language selector in the interface to switch between available languages like English and Portuguese."
    },

    # System Navigation and General Help - Portuguese
    {
        "input": "Não consigo aceder ao sistema",
        "output": "Verifique se as suas credenciais de login estão corretas. Se está a ter problemas, tente limpar a cache do seu navegador ou use um navegador diferente. Para problemas persistentes, contacte o seu administrador do sistema para assistência."
    },
    {
        "input": "Que navegadores são suportados?",
        "output": "O sistema funciona melhor com navegadores modernos como Chrome, Firefox, Safari e Edge. Certifique-se de que o seu navegador está atualizado e tem JavaScript ativado para funcionalidade completa."
    },
    {
        "input": "O sistema é compatível com dispositivos móveis?",
        "output": "Sim! O sistema é responsivo e adapta-se a dispositivos móveis e tablets. Pode aceder a todas as funcionalidades a partir do navegador do seu telefone ou tablet."
    },
    {
        "input": "Como faço logout em segurança?",
        "output": "Clique no seu menu de perfil no canto superior direito e selecione 'Logout'. Isto terminará a sua sessão em segurança e levá-lo-á de volta à página de login."
    },
    {
        "input": "Quais são os diferentes papéis de utilizador?",
        "output": "Existem três papéis de utilizador: Administrador (acesso total ao sistema), Gestor (pode avaliar membros da equipa e gerir formações) e Utilizador (pode ver as suas avaliações e formações, enviar mensagens)."
    },
    {
        "input": "Posso alterar o idioma da interface?",
        "output": "Sim, o sistema suporta múltiplos idiomas. Procure pelo seletor de idioma na interface para alternar entre idiomas disponíveis como Inglês e Português."
    },

    # Additional System Questions - English/Portuguese
    {
        "input": "How often should evaluations be conducted?",
        "output": "Evaluation frequency depends on your organization's policy. Administrators set up evaluation cycles which typically occur annually, semi-annually, or quarterly based on company requirements."
    },
    {
        "input": "Com que frequência devem ser conduzidas as avaliações?",
        "output": "A frequência das avaliações depende da política da sua organização. Os administradores configuram ciclos de avaliação que normalmente ocorrem anualmente, semestralmente ou trimestralmente baseados nos requisitos da empresa."
    },
    {
        "input": "What happens if I miss an evaluation deadline?",
        "output": "Managers should complete evaluations within the cycle period. If deadlines are missed, administrators can extend cycles or handle evaluations on a case-by-case basis."
    },
    {
        "input": "O que acontece se perder um prazo de avaliação?",
        "output": "Os gestores devem completar avaliações dentro do período do ciclo. Se os prazos forem perdidos, os administradores podem estender ciclos ou lidar com avaliações caso a caso."
    },
    {
        "input": "Can I export my evaluation data?",
        "output": "Administrators and managers can generate PDF reports containing evaluation data. Individual users can view their evaluation history in their profile but export capabilities depend on administrative permissions."
    },
    {
        "input": "Posso exportar os meus dados de avaliação?",
        "output": "Administradores e gestores podem gerar relatórios PDF contendo dados de avaliação. Utilizadores individuais podem ver o seu histórico de avaliação no seu perfil mas as capacidades de exportação dependem das permissões administrativas."
    },
    {
        "input": "Who can see my evaluation results?",
        "output": "Your evaluation results are visible to you, your manager (who conducted the evaluation), and system administrators. The system maintains confidentiality and appropriate access controls."
    },
    {
        "input": "Quem pode ver os meus resultados de avaliação?",
        "output": "Os seus resultados de avaliação são visíveis para si, o seu gestor (que conduziu a avaliação) e administradores do sistema. O sistema mantém confidencialidade e controlos de acesso apropriados."
    },
    {
        "input": "How do I get training recommendations?",
        "output": "The AI system can analyze your evaluation feedback and suggest relevant training programs. Managers can also review your profile and unparticipated trainings to make recommendations."
    },
    {
        "input": "Como obtenho recomendações de formação?",
        "output": "O sistema de IA pode analisar o seu feedback de avaliação e sugerir programas de formação relevantes. Os gestores também podem rever o seu perfil e formações não participadas para fazer recomendações."
    },
    {
        "input": "What if I disagree with my evaluation?",
        "output": "If you have concerns about your evaluation, discuss them directly with your manager first. For formal processes, contact your HR department or system administrator as per your organization's policies."
    },
    {
        "input": "E se discordar da minha avaliação?",
        "output": "Se tem preocupações sobre a sua avaliação, discuta-as diretamente com o seu gestor primeiro. Para processos formais, contacte o seu departamento de RH ou administrador do sistema conforme as políticas da sua organização."
    },
    {
        "input": "Can I track my professional development over time?",
        "output": "Yes! Your profile shows your evaluation history and training participation statistics over time, helping you track your professional growth and development progress."
    },
    {
        "input": "Posso acompanhar o meu desenvolvimento profissional ao longo do tempo?",
        "output": "Sim! O seu perfil mostra o seu histórico de avaliação e estatísticas de participação em formação ao longo do tempo, ajudando-o a acompanhar o seu crescimento profissional e progresso de desenvolvimento."
    },
    {
        "input": "Is my data secure in the system?",
        "output": "Yes, the system implements security best practices including encrypted passwords, secure HTTPS communication, session management, and role-based access controls to protect your data."
    },
    {
        "input": "Os meus dados estão seguros no sistema?",
        "output": "Sim, o sistema implementa as melhores práticas de segurança incluindo palavras-passe encriptadas, comunicação HTTPS segura, gestão de sessões e controlos de acesso baseados em papéis para proteger os seus dados."
    },
    {
        "input": "How do I get technical support?",
        "output": "For technical issues, contact your system administrator or IT support team. You can also use the AI assistant for basic system usage questions and guidance."
    },
    {
        "input": "Como obtenho suporte técnico?",
        "output": "Para problemas técnicos, contacte o seu administrador do sistema ou equipa de suporte de TI. Também pode usar o assistente de IA para questões básicas de uso do sistema e orientação."
    },
    {
        "input": "What should I do if the system is slow?",
        "output": "Try refreshing the page, clearing your browser cache, or checking your internet connection. If problems persist, report the issue to your system administrator."
    },
    {
        "input": "O que devo fazer se o sistema estiver lento?",
        "output": "Tente atualizar a página, limpar a cache do seu navegador ou verificar a sua ligação à internet. Se os problemas persistirem, reporte o problema ao seu administrador do sistema."
    }
]