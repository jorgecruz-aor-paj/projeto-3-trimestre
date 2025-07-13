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
    {
        "input": "How can I change my password?",
        "output": "To change your password, first go to your profile page and click 'Change Password'. You'll receive a password reset link via email. Click the link and you'll be taken to a secure page where you can set a new password."
    },
    {
        "input": "Can I reset my profile picture to default?",
        "output": "Yes! In your profile page, when you're editing your profile, you'll see options to either upload a new profile picture or reset to the default picture. The reset option will remove your current custom picture and use the system default."
    },
    {
        "input": "What are the password requirements?",
        "output": "Your password must be at least 8 characters long and include: at least one uppercase letter, one lowercase letter, one number, and one special character (@#$%^&+=!*). Spaces and tabs are not allowed."
    },
    {
        "input": "How do I upload a profile picture?",
        "output": "In your profile page, click the 'Change Picture' button or click on your current profile picture. Select an image file from your device (JPG, PNG are supported). The system will automatically resize the image to fit properly. The maximum file size is 5MB."
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
    {
        "input": "Como posso alterar a minha palavra-passe?",
        "output": "Para alterar a sua palavra-passe, primeiro aceda à sua página de perfil e clique em 'Alterar Palavra-passe'. Receberá uma ligação para redefinir a palavra-passe por email. Clique na ligação e será levado para uma página segura onde pode definir uma nova palavra-passe."
    },
    {
        "input": "Posso repor a minha fotografia de perfil para a predefinida?",
        "output": "Sim! Na sua página de perfil, quando estiver a editar o seu perfil, verá opções para carregar uma nova fotografia de perfil ou repor para a predefinida. A opção de reposição removerá a sua fotografia personalizada atual e usará a predefinida do sistema."
    },
    {
        "input": "Quais são os requisitos da palavra-passe?",
        "output": "A sua palavra-passe deve ter pelo menos 8 caracteres e incluir: pelo menos uma letra maiúscula, uma letra minúscula, um número e um carácter especial (@#$%^&+=!*). Espaços e tabulações não são permitidos."
    },
    {
        "input": "Como carrego uma fotografia de perfil?",
        "output": "Na sua página de perfil, clique no botão 'Alterar Fotografia' ou clique na sua fotografia de perfil atual. Selecione um ficheiro de imagem do seu dispositivo (JPG, PNG são suportados). O sistema redimensionará automaticamente a imagem para caber adequadamente. O tamanho máximo do ficheiro é 5MB."
    },

    # Real-time Notifications and WebSocket Features - English
    {
        "input": "How do I know when I receive new messages?",
        "output": "The system provides real-time notifications! You'll see a notification badge with the number of unread messages in the main navigation. The system uses WebSocket technology for instant delivery, so you'll be notified immediately when someone sends you a message."
    },
    {
        "input": "What are the notification indicators in the system?",
        "output": "The system shows notification badges in the main navigation bar. You'll see counters for unread messages and system notifications. When you receive new messages or notifications, these badges update automatically in real-time without needing to refresh the page."
    },
    {
        "input": "Do I get notified about system events?",
        "output": "Yes! The system sends real-time notifications for various events like: new messages, evaluation cycle updates, training assignments, and other important system activities. All notifications appear instantly thanks to WebSocket technology."
    },
    {
        "input": "Can I see when my messages are read?",
        "output": "Yes, the system provides read receipts for messages. You can see when your messages have been read by the recipient through visual indicators in the conversation. This works in real-time through WebSocket connections."
    },
    {
        "input": "How does real-time messaging work?",
        "output": "The system uses advanced WebSocket technology for real-time communication. When you send a message, it's delivered instantly to the recipient if they're online. You'll also see when messages are delivered and read through visual indicators."
    },

    # Real-time Notifications and WebSocket Features - Portuguese
    {
        "input": "Como sei quando recebo novas mensagens?",
        "output": "O sistema fornece notificações em tempo real! Verá um distintivo de notificação com o número de mensagens não lidas na navegação principal. O sistema usa tecnologia WebSocket para entrega instantânea, por isso será notificado imediatamente quando alguém lhe enviar uma mensagem."
    },
    {
        "input": "Quais são os indicadores de notificação no sistema?",
        "output": "O sistema mostra distintivos de notificação na barra de navegação principal. Verá contadores para mensagens não lidas e notificações do sistema. Quando recebe novas mensagens ou notificações, estes distintivos atualizam automaticamente em tempo real sem precisar de atualizar a página."
    },
    {
        "input": "Sou notificado sobre eventos do sistema?",
        "output": "Sim! O sistema envia notificações em tempo real para vários eventos como: novas mensagens, atualizações de ciclos de avaliação, atribuições de formação e outras atividades importantes do sistema. Todas as notificações aparecem instantaneamente graças à tecnologia WebSocket."
    },
    {
        "input": "Posso ver quando as minhas mensagens são lidas?",
        "output": "Sim, o sistema fornece confirmações de leitura para mensagens. Pode ver quando as suas mensagens foram lidas pelo destinatário através de indicadores visuais na conversa. Isto funciona em tempo real através de ligações WebSocket."
    },
    {
        "input": "Como funciona o sistema de mensagens em tempo real?",
        "output": "O sistema usa tecnologia WebSocket avançada para comunicação em tempo real. Quando envia uma mensagem, é entregue instantaneamente ao destinatário se estiver online. Também verá quando as mensagens são entregues e lidas através de indicadores visuais."
    },

    # Admin Features - Bulk Operations and User Management - English
    {
        "input": "How can I assign managers to multiple users at once?",
        "output": "As an administrator, go to the Evaluation Cycles page and click 'Assign Managers'. This opens a multi-step wizard where you can: 1) Select unmanaged users, 2) Choose a manager, and 3) Confirm the bulk assignment. This is much faster than assigning managers individually."
    },
    {
        "input": "What is bulk manager assignment?",
        "output": "Bulk manager assignment allows administrators to assign one manager to multiple users simultaneously. This is useful for organizing teams and ensuring all users have proper management structure before starting evaluation cycles. The system automatically promotes users to manager role when needed."
    },
    {
        "input": "How do I start a new evaluation cycle?",
        "output": "As an administrator, go to the Evaluation Cycles page and click 'Start New Cycle'. You'll need to set start and end dates for the evaluation period. Note: all users must have assigned managers before you can start a cycle. The system will automatically create evaluations for all managed users."
    },
    {
        "input": "Can I close evaluations in bulk?",
        "output": "Yes! As an administrator in the Evaluation Management section, you can select multiple evaluations and close them all at once. This is useful at the end of evaluation cycles to finalize all completed evaluations efficiently."
    },
    {
        "input": "What are unmanaged users?",
        "output": "Unmanaged users are employees who don't have an assigned manager. This is important because managers are responsible for evaluating their team members. You can view unmanaged users on the Evaluation Cycles page and use the bulk manager assignment feature to assign managers to them."
    },
    {
        "input": "How do I manage user roles and permissions?",
        "output": "As an administrator, you can view and manage user roles in the User Management section. The system has three roles: Admin (full access), Manager (can evaluate team members), and User (basic access). When you assign users to a manager, the manager is automatically promoted to Manager role if needed."
    },

    # Admin Features - Bulk Operations and User Management - Portuguese
    {
        "input": "Como posso atribuir gestores a múltiplos utilizadores de uma vez?",
        "output": "Como administrador, aceda à página Ciclos de Avaliação e clique em 'Atribuir Gestores'. Isto abre um assistente de múltiplos passos onde pode: 1) Selecionar utilizadores não geridos, 2) Escolher um gestor, e 3) Confirmar a atribuição em massa. Isto é muito mais rápido do que atribuir gestores individualmente."
    },
    {
        "input": "O que é a atribuição em massa de gestores?",
        "output": "A atribuição em massa de gestores permite aos administradores atribuir um gestor a múltiplos utilizadores simultaneamente. Isto é útil para organizar equipas e garantir que todos os utilizadores têm uma estrutura de gestão adequada antes de iniciar ciclos de avaliação. O sistema promove automaticamente utilizadores ao papel de gestor quando necessário."
    },
    {
        "input": "Como inicio um novo ciclo de avaliação?",
        "output": "Como administrador, aceda à página Ciclos de Avaliação e clique em 'Iniciar Novo Ciclo'. Terá de definir datas de início e fim para o período de avaliação. Nota: todos os utilizadores devem ter gestores atribuídos antes de poder iniciar um ciclo. O sistema criará automaticamente avaliações para todos os utilizadores geridos."
    },
    {
        "input": "Posso fechar avaliações em massa?",
        "output": "Sim! Como administrador na secção Gestão de Avaliação, pode selecionar múltiplas avaliações e fechá-las todas de uma vez. Isto é útil no final dos ciclos de avaliação para finalizar todas as avaliações concluídas eficientemente."
    },
    {
        "input": "O que são utilizadores não geridos?",
        "output": "Utilizadores não geridos são funcionários que não têm um gestor atribuído. Isto é importante porque os gestores são responsáveis por avaliar os membros da sua equipa. Pode ver utilizadores não geridos na página Ciclos de Avaliação e usar a funcionalidade de atribuição em massa de gestores para lhes atribuir gestores."
    },
    {
        "input": "Como giro papéis e permissões de utilizadores?",
        "output": "Como administrador, pode ver e gerir papéis de utilizadores na secção Gestão de Utilizadores. O sistema tem três papéis: Admin (acesso total), Gestor (pode avaliar membros da equipa), e Utilizador (acesso básico). Quando atribui utilizadores a um gestor, o gestor é automaticamente promovido ao papel de Gestor se necessário."
    },

    # Dashboard and Statistics Features - English
    {
        "input": "What can I see on the admin dashboard?",
        "output": "The admin dashboard shows system overview statistics including: total users, active managers, unmanaged users, completed evaluations, and pending evaluations. You also get quick action buttons to access User Management, Training Management, Evaluation Management, and System Settings."
    },
    {
        "input": "What features does the manager dashboard have?",
        "output": "The manager dashboard displays both your personal statistics and team management features. You can see your own evaluation and training history, plus team management actions like evaluating team members, assigning training, and accessing team reports. It shows performance trends and training statistics for better team oversight."
    },
    {
        "input": "How do I access system statistics?",
        "output": "System statistics are available on role-specific dashboards. Administrators see comprehensive system stats including user counts and evaluation metrics. Managers see team-specific statistics and trends. All dashboards provide real-time data about system usage and performance."
    },

    # Dashboard and Statistics Features - Portuguese
    {
        "input": "O que posso ver no painel de administração?",
        "output": "O painel de administração mostra estatísticas de visão geral do sistema incluindo: total de utilizadores, gestores ativos, utilizadores não geridos, avaliações concluídas e avaliações pendentes. Também obtém botões de ação rápida para aceder à Gestão de Utilizadores, Gestão de Formação, Gestão de Avaliação e Configurações do Sistema."
    },
    {
        "input": "Que funcionalidades tem o painel de gestor?",
        "output": "O painel de gestor exibe tanto as suas estatísticas pessoais como funcionalidades de gestão de equipa. Pode ver o seu próprio histórico de avaliação e formação, além de ações de gestão de equipa como avaliar membros da equipa, atribuir formação e aceder a relatórios de equipa. Mostra tendências de desempenho e estatísticas de formação para melhor supervisão da equipa."
    },
    {
        "input": "Como acedo às estatísticas do sistema?",
        "output": "As estatísticas do sistema estão disponíveis em painéis específicos por papel. Os administradores veem estatísticas abrangentes do sistema incluindo contagens de utilizadores e métricas de avaliação. Os gestores veem estatísticas específicas da equipa e tendências. Todos os painéis fornecem dados em tempo real sobre uso e desempenho do sistema."
    },

    # Advanced Training Features - English
    {
        "input": "How can managers track training participation?",
        "output": "Managers can view training participation statistics on their dashboard and in individual user profiles. You can see which team members have completed which trainings, yearly training hours, and identify training gaps. This helps in making informed decisions about training assignments."
    },
    {
        "input": "Can I see training recommendations?",
        "output": "Yes! The AI system can analyze evaluation feedback and suggest relevant training programs. Managers can also review team member profiles to see which trainings they haven't participated in yet, making it easier to recommend appropriate training programs."
    },
    {
        "input": "What training statistics are available?",
        "output": "The system provides comprehensive training statistics including: individual training participation history, yearly training hours per user, training completion rates across teams, and trending analysis. Managers and administrators can use these insights for better training program management."
    },

    # Advanced Training Features - Portuguese
    {
        "input": "Como podem os gestores acompanhar a participação em formação?",
        "output": "Os gestores podem ver estatísticas de participação em formação no seu painel e nos perfis individuais dos utilizadores. Pode ver que membros da equipa completaram que formações, horas de formação anuais e identificar lacunas de formação. Isto ajuda a tomar decisões informadas sobre atribuições de formação."
    },
    {
        "input": "Posso ver recomendações de formação?",
        "output": "Sim! O sistema de IA pode analisar o feedback de avaliação e sugerir programas de formação relevantes. Os gestores também podem rever perfis de membros da equipa para ver em que formações ainda não participaram, facilitando a recomendação de programas de formação apropriados."
    },
    {
        "input": "Que estatísticas de formação estão disponíveis?",
        "output": "O sistema fornece estatísticas abrangentes de formação incluindo: histórico individual de participação em formação, horas de formação anuais por utilizador, taxas de conclusão de formação entre equipas e análise de tendências. Gestores e administradores podem usar estas perceções para melhor gestão de programas de formação."
    },

    # Profile Picture and File Management - English
    {
        "input": "What image formats are supported for profile pictures?",
        "output": "The system supports common image formats including JPG, JPEG, PNG, and other standard image types. Images are automatically resized to 300x300 pixels for optimal display and performance. The maximum file size allowed is 5MB."
    },
    {
        "input": "Can I change someone else's profile picture?",
        "output": "Only administrators can upload or reset profile pictures for other users. Regular users can only manage their own profile pictures. This is a security feature to prevent unauthorized changes to user profiles."
    },
    {
        "input": "What happens when I upload a large image?",
        "output": "The system automatically resizes large images to 300x300 pixels for profile pictures. This ensures consistent display across the application and improves performance. The original aspect ratio is maintained during resizing, and file size is optimized."
    },

    # Profile Picture and File Management - Portuguese
    {
        "input": "Que formatos de imagem são suportados para fotografias de perfil?",
        "output": "O sistema suporta formatos de imagem comuns incluindo JPG, JPEG, PNG e outros tipos de imagem padrão. As imagens são automaticamente redimensionadas para 300x300 pixels para exibição e desempenho ideais. O tamanho máximo de ficheiro permitido é 5MB."
    },
    {
        "input": "Posso alterar a fotografia de perfil de outra pessoa?",
        "output": "Apenas os administradores podem carregar ou repor fotografias de perfil para outros utilizadores. Os utilizadores regulares apenas podem gerir as suas próprias fotografias de perfil. Esta é uma funcionalidade de segurança para prevenir alterações não autorizadas aos perfis de utilizadores."
    },
    {
        "input": "O que acontece quando carrego uma imagem grande?",
        "output": "O sistema redimensiona automaticamente imagens grandes para 300x300 pixels para fotografias de perfil. Isto garante exibição consistente em toda a aplicação e melhora o desempenho. A proporção original é mantida durante o redimensionamento, e o tamanho do ficheiro é otimizado."
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
        "output": "Sim, o sistema fornece confirmações de leitura para mensagens. Pode ver quando as suas mensagens foram lidas pelo destinatário através de indicadores visuais na conversa."
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
    },
    {
        "input": "Olá",
        "output": "Olá! Como posso ajudá-lo hoje? Pode perguntar-me sobre avaliações, formações, ou qualquer funcionalidade do sistema."
    },
    {
        "input": "Hello",
        "output": "Hello! How can I help you today? You can ask me about evaluations, trainings, or any system functionality."
    },
    {
        "input": "Oi",
        "output": "Oi! Em que posso ajudar? Estou aqui para responder questões sobre o sistema."
    },
    {
        "input": "Hi",
        "output": "Hi! What can I help you with? I'm here to answer questions about the system."
    },
    # Enhanced capability questions - English
    {
        "input": "What can you do for me?",
        "output": "I can help you with information about the evaluation and training management system:\n\n📊 **Evaluations** - View results, understand statuses, track progress\n🎓 **Training** - Available programs, participation history, recommendations\n👤 **Profile** - Personal information, profile pictures, activity history\n💬 **System** - Navigation, messaging, notifications\n\nWhich area would you like to explore?"
    },
    {
        "input": "What can you help me with?",
        "output": "I'm here to assist you with the evaluation and training management system. I can provide information about evaluations, training programs, profile management, and system features. You can ask me about how to navigate the system, understand evaluation processes, find training opportunities, or manage your profile."
    },
    {
        "input": "How can you help me?",
        "output": "I can assist you with various aspects of the evaluation and training management system:\n\n• **Evaluations**: How to view your evaluations, understand different statuses, and track your professional development\n• **Training**: Find available programs, view your training history, and get recommendations\n• **Profile**: Manage personal information, upload profile pictures, and view activity history\n• **Communication**: Send messages, view notifications, and use real-time features\n\nWhat specific topic would you like help with?"
    },
    {
        "input": "What are your capabilities?",
        "output": "My capabilities include providing information and guidance about:\n\n✅ Evaluation processes and results\n✅ Training programs and participation\n✅ Profile and account management\n✅ System navigation and features\n✅ Messaging and communication\n✅ Troubleshooting common issues\n\nI can answer questions about how to use system features, explain processes, and provide step-by-step guidance. What would you like to know more about?"
    },

    # Enhanced capability questions - Portuguese
    {
        "input": "O que podes fazer por mim?",
        "output": "Posso ajudar com informações sobre o sistema de gestão de avaliações e formações:\n\n📊 **Avaliações** - Ver resultados, entender estados, acompanhar progresso\n🎓 **Formações** - Programas disponíveis, histórico de participação, recomendações\n👤 **Perfil** - Informações pessoais, fotografias de perfil, histórico de atividades\n💬 **Sistema** - Navegação, mensagens, notificações\n\nQue área gostaria de explorar?"
    },
    {
        "input": "Com que me podes ajudar?",
        "output": "Estou aqui para o ajudar com o sistema de gestão de avaliações e formações. Posso fornecer informações sobre avaliações, programas de formação, gestão de perfil e funcionalidades do sistema. Pode perguntar-me sobre como navegar no sistema, entender processos de avaliação, encontrar oportunidades de formação ou gerir o seu perfil."
    },
    {
        "input": "Como me podes ajudar?",
        "output": "Posso ajudá-lo com vários aspetos do sistema de gestão de avaliações e formações:\n\n• **Avaliações**: Como ver as suas avaliações, entender diferentes estados e acompanhar o seu desenvolvimento profissional\n• **Formações**: Encontrar programas disponíveis, ver o seu histórico de formação e receber recomendações\n• **Perfil**: Gerir informações pessoais, carregar fotografias de perfil e ver histórico de atividades\n• **Comunicação**: Enviar mensagens, ver notificações e usar funcionalidades em tempo real\n\nCom que tópico específico gostaria de ajuda?"
    },
    {
        "input": "Quais são as tuas capacidades?",
        "output": "As minhas capacidades incluem fornecer informações e orientação sobre:\n\n✅ Processos e resultados de avaliação\n✅ Programas de formação e participação\n✅ Gestão de perfil e conta\n✅ Navegação e funcionalidades do sistema\n✅ Mensagens e comunicação\n✅ Resolução de problemas comuns\n\nPosso responder a questões sobre como usar funcionalidades do sistema, explicar processos e fornecer orientação passo a passo. Sobre o que gostaria de saber mais?"
    }
]

# Quality validation constants
POOR_RESPONSE_PATTERNS = [
    "i can help with that",
    "i can help with this", 
    "ive got the right stuff",
    "ai",
    "sure thing",
    "of course",
    "no problem",
    "let me help",
    "i understand",
    "i can assist",
    "happy to help",
    "glad to help",
    "posso ajudar com isso",
    "posso ajudar com isto"
]

# Context leakage indicators
CONTEXT_FRAGMENTS = [
    "AVALIAÇÕES:", 
    "FORMAÇÕES:", 
    "MENSAGENS:", 
    "PERFIL:", 
    "GESTÃO"
]

# Question word mappings for semantic analysis
QUESTION_WORDS = {
    'how': ['como'],
    'what': ['que', 'o que'],
    'where': ['onde'],
    'when': ['quando'],
    'why': ['porque', 'por que'],
    'who': ['quem']
}

# Topic keywords for semantic matching
TOPIC_KEYWORDS = {
    'login': ['login', 'entrar', 'acesso', 'password', 'senha'],
    'evaluation': ['avaliação', 'evaluation', 'avaliar', 'desempenho', 'performance'],
    'training': ['formação', 'training', 'curso', 'treino'],
    'profile': ['perfil', 'profile', 'conta', 'account'],
    'message': ['mensagem', 'message', 'chat', 'comunicação']
}

# Common words to ignore in relevance checking
COMMON_WORDS = {
    'a', 'an', 'the', 'and', 'or', 'but', 'in', 'on', 'at', 'to', 'for', 'of', 'with', 'by', 
    'o', 'a', 'os', 'as', 'um', 'uma', 'e', 'ou', 'mas', 'em', 'no', 'na', 'para', 'de', 'com', 'por',
    'how', 'what', 'where', 'when', 'why', 'who', 'can', 'could', 'would', 'should',
    'como', 'que', 'onde', 'quando', 'porque', 'quem', 'pode', 'poderia', 'deveria'
}

# Language-specific fallback messages
fallback_messages = {
    'pt': "Desculpe, não consegui encontrar uma resposta específica para a sua pergunta. Pode reformular ou perguntar sobre avaliações, formações, perfis ou mensagens do sistema?",
    'en': "I'm sorry, I couldn't find a specific answer to your question. Could you rephrase or ask about evaluations, trainings, profiles, or system messages?"
}

# Language detection helper words
LANGUAGE_INDICATORS = {
    'pt': ['como', 'posso', 'onde', 'quando', 'porque', 'que', 'não', 'sim', 'obrigado', 'por favor', 'avaliação', 'formação', 'treinamento'],
    'en': ['how', 'can', 'where', 'when', 'why', 'what', 'yes', 'no', 'thank', 'please', 'evaluation', 'training']
}

# Validation error messages
VALIDATION_MESSAGES = {
    'too_short': "❌ Response too short",
    'context_leakage': "❌ Context leakage detected",
    'generic_response': "❌ Rejected generic response",
    'pattern_dominated': "❌ Rejected pattern-dominated response",
    'no_punctuation': "❌ No proper punctuation",
    'too_repetitive': "❌ Too repetitive",
    'not_relevant': "❌ Response not relevant to question",
    'short_with_pattern': "❌ Rejected short response with pattern",
    'single_word': "❌ Rejected single-word response",
    'passed_validation': "✅ Response passed quality check"
}

# LLM generation error messages
GENERATION_MESSAGES = {
    'llm_failed': "❌ LLM failed",
    'business_check_failed': "Business appropriateness check failed",
    'problematic_patterns': "Response contains problematic patterns",
    'model_not_loaded': "LLM model is not loaded"
}

# System response templates for specific contexts
SYSTEM_RESPONSE_TEMPLATES = {
    'pt': {
        'general_help': "Posso ajudar com informações sobre o sistema de gestão de avaliações e formações. Que área específica gostaria de explorar?",
        'evaluation_help': "No sistema de avaliações, pode ver as suas avaliações, entender os diferentes estados e acompanhar o seu desenvolvimento profissional.",
        'training_help': "Na secção de formações, pode ver programas disponíveis, o seu histórico de participação e receber recomendações.",
        'profile_help': "No seu perfil, pode atualizar informações pessoais, ver histórico de avaliações e gerir a sua fotografia de perfil."
    },
    'en': {
        'general_help': "I can help with information about the evaluation and training management system. Which specific area would you like to explore?",
        'evaluation_help': "In the evaluation system, you can view your evaluations, understand different statuses, and track your professional development.",
        'training_help': "In the training section, you can see available programs, your participation history, and how to receive recommendations.",
        'profile_help': "In your profile, you can update personal information, view evaluation history, and manage your profile picture."
    }
}

# Enhanced capability response templates
CAPABILITY_RESPONSE_TEMPLATES = {
    'pt': {
        'comprehensive': """Posso ajudar com informações sobre o sistema de gestão de avaliações e formações:

📊 **Avaliações** - Ver resultados, entender estados, acompanhar progresso
🎓 **Formações** - Programas disponíveis, histórico de participação, recomendações  
👤 **Perfil** - Informações pessoais, fotografias de perfil, histórico de atividades
💬 **Sistema** - Navegação, mensagens, notificações

Que área gostaria de explorar?""",
        'detailed': """As minhas capacidades incluem fornecer informações e orientação sobre:

✅ Processos e resultados de avaliação
✅ Programas de formação e participação
✅ Gestão de perfil e conta
✅ Navegação e funcionalidades do sistema
✅ Mensagens e comunicação
✅ Resolução de problemas comuns

Posso responder a questões sobre como usar funcionalidades do sistema, explicar processos e fornecer orientação passo a passo."""
    },
    'en': {
        'comprehensive': """I can help you with information about the evaluation and training management system:

📊 **Evaluations** - View results, understand statuses, track progress
🎓 **Training** - Available programs, participation history, recommendations
👤 **Profile** - Personal information, profile pictures, activity history
💬 **System** - Navigation, messaging, notifications

Which area would you like to explore?""",
        'detailed': """My capabilities include providing information and guidance about:

✅ Evaluation processes and results
✅ Training programs and participation
✅ Profile and account management
✅ System navigation and features
✅ Messaging and communication
✅ Troubleshooting common issues

I can answer questions about how to use system features, explain processes, and provide step-by-step guidance."""
    }
}

# Enhanced prompt context templates
PROMPT_CONTEXT_TEMPLATES = {
    'system_context': "Sistema de Gestão de Avaliações e Formações / Evaluation and Training Management System",
    'assistant_role': "You are a helpful assistant for an evaluation and training management system. Provide clear, specific information about system features, processes, and usage.",
    'response_guidelines': "Keep responses focused on the system functionality. Use structured formatting with bullet points or numbered lists when appropriate. Provide actionable guidance."
}

# Single word responses to reject
SINGLE_WORD_RESPONSES = ['yes', 'no', 'ok', 'sim', 'não', 'ai']

# Context categorization for business responses
CONTEXT_CATEGORIES = {
    'evaluation': ["evaluation", "evaluate", "performance", "avaliação", "avaliar", "desempenho"],
    'training': ["training", "course", "learn", "formação", "curso", "treino"],
    'message': ["message", "chat", "communication", "mensagem", "comunicação"],
    'profile': ["profile", "account", "perfil", "conta"],
    'login': ["login", "access", "password", "entrar", "acesso", "senha"]
}

# Topic-specific fallback responses for simple keyword matching
topic_fallback_responses = {
    'evaluation': {
        'pt': "Sobre avaliações, posso ajudar com informações sobre como ver as suas avaliações, entender os diferentes estados e acompanhar o seu desenvolvimento profissional.",
        'en': "About evaluations, I can help with information on how to view your evaluations, understand different statuses, and track your professional development."
    },
    'training': {
        'pt': "Sobre formações, posso ajudar com informações sobre programas disponíveis, o seu histórico de participação e como receber recomendações.",
        'en': "About training, I can help with information on available programs, your participation history, and how to receive recommendations."
    },
    'messaging': {
        'pt': "Sobre mensagens, posso ajudar com informações sobre como enviar mensagens, ver notificações em tempo real e usar o sistema de comunicação.",
        'en': "About messaging, I can help with information on how to send messages, view real-time notifications, and use the communication system."
    },
    'login': {
        'pt': "Sobre acesso ao sistema, posso ajudar com informações sobre login, alteração de palavra-passe e gestão da sua conta.",
        'en': "About system access, I can help with information on login, password changes, and account management."
    },
    'error': {
        'pt': "Se está a ter problemas técnicos, recomendo contactar o administrador do sistema ou tentar atualizar a página.",
        'en': "If you're having technical issues, I recommend contacting the system administrator or trying to refresh the page."
    },
    'general': {
        'pt': "Posso ajudar com informações sobre o sistema de gestão de avaliações e formações. Pode perguntar sobre avaliações, formações, perfis ou mensagens.",
        'en': "I can help with information about the evaluation and training management system. You can ask about evaluations, training, profiles, or messaging."
    }
}

# Business advice responses for common professional topics
business_advice = {
    'feedback': {
        'pt': {
            'question': 'Como dar feedback eficaz?',
            'response': 'Para dar feedback eficaz: seja específico e focado em comportamentos observáveis, dê feedback em tempo útil, use a técnica SBI (Situação-Comportamento-Impacto), mantenha um tom construtivo e ofereça sugestões para melhoria.'
        },
        'en': {
            'question': 'How to give effective feedback?',
            'response': 'For effective feedback: be specific and focus on observable behaviors, give timely feedback, use the SBI technique (Situation-Behavior-Impact), maintain a constructive tone, and offer suggestions for improvement.'
        }
    },
    'management': {
        'pt': {
            'question': 'Como gerir uma equipa eficazmente?',
            'response': 'Para gerir uma equipa eficazmente: estabeleça objetivos claros, comunique regularmente, reconheça conquistas, forneça recursos necessários, promova desenvolvimento profissional e mantenha um ambiente de trabalho positivo.'
        },
        'en': {
            'question': 'How to manage a team effectively?',
            'response': 'To manage a team effectively: set clear objectives, communicate regularly, recognize achievements, provide necessary resources, promote professional development, and maintain a positive work environment.'
        }
    },
    'performance': {
        'pt': {
            'question': 'Como melhorar o desempenho?',
            'response': 'Para melhorar o desempenho: identifique áreas de melhoria através de avaliações, estabeleça metas SMART, procure formação relevante, peça feedback regular e implemente as sugestões recebidas.'
        },
        'en': {
            'question': 'How to improve performance?',
            'response': 'To improve performance: identify improvement areas through evaluations, set SMART goals, seek relevant training, ask for regular feedback, and implement received suggestions.'
        }
    }
}

# Template responses for different question types
response_templates = {
    'pt': {
        'how_to': "Para {action}, você pode {steps}. Se precisar de mais ajuda, fique à vontade para perguntar!",
        'what_is': "O {concept} é {explanation}. No nosso sistema, isso significa {specific_info}.",
        'why': "Isso acontece porque {reason}. É importante para {benefit}.",
        'where': "Você pode encontrar isso em {location}. Acesse através de {navigation}.",
        'general': "Sobre {topic}, posso dizer que {information}. Precisa de mais detalhes sobre algum aspecto específico?"
    },
    'en': {
        'how_to': "To {action}, you can {steps}. If you need more help, feel free to ask!",
        'what_is': "The {concept} is {explanation}. In our system, this means {specific_info}.",
        'why': "This happens because {reason}. It's important for {benefit}.",
        'where': "You can find this in {location}. Access it through {navigation}.",
        'general': "About {topic}, I can tell you that {information}. Do you need more details about any specific aspect?"
    }
}

# Domain knowledge for system-specific responses
domain_knowledge = {
    'evaluations': {
        'pt': 'avaliações permitem que gestores avaliem o desempenho dos funcionários numa escala de 1-4',
        'en': 'evaluations allow managers to assess employee performance on a 1-4 scale'
    },
    'training': {
        'pt': 'formações ajudam no desenvolvimento profissional e podem ser registadas pelos gestores',
        'en': 'trainings help with professional development and can be registered by managers'
    },
    'messaging': {
        'pt': 'mensagens permitem comunicação em tempo real entre colegas no sistema',
        'en': 'messaging allows real-time communication between colleagues in the system'
    },
    'profile': {
        'pt': 'perfis contêm informações pessoais, histórico de avaliações e formações',
        'en': 'profiles contain personal information, evaluation history, and training records'
    },
    'system': {
        'pt': 'o sistema oferece gestão de avaliações, formações, mensagens e perfis de utilizadores',
        'en': 'the system provides management of evaluations, training, messaging and user profiles'
    }
}

# Inappropriate content indicators for business filtering
inappropriate_indicators = [
    'paid in cash', 'pago em dinheiro', 'trabalho informal',
    'sem contrato', 'no contract', 'under the table',
    'tax evasion', 'evasão fiscal', 'black market',
    'illegal', 'ilegal', 'inappropriate', 'inapropriado'
]

# Language-specific stop words for keyword extraction
stop_words = {
    'en': {'how', 'can', 'i', 'do', 'the', 'to', 'a', 'an', 'and', 'or', 'but', 'in', 'on', 'at', 'for', 'with', 'by', 'what', 'where', 'when', 'why', 'who', 'is', 'are', 'was', 'were', 'be', 'been', 'have', 'has', 'had', 'will', 'would', 'could', 'should', 'may', 'might', 'must'},
    'pt': {'como', 'posso', 'eu', 'fazer', 'o', 'a', 'para', 'um', 'uma', 'e', 'ou', 'mas', 'em', 'no', 'na', 'por', 'com', 'de', 'que', 'onde', 'quando', 'porque', 'quem', 'é', 'são', 'foi', 'foram', 'ser', 'sido', 'ter', 'tem', 'teve', 'vai', 'iria', 'poderia', 'deveria', 'pode', 'deve'}
}