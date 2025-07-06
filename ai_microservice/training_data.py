training_conversations = [
    # Authentication and Account Management
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

    # Evaluation Management - User Perspective
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

    # Evaluation Management - Manager Perspective
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

    # Training Management - User Perspective
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

    # Training Management - Manager/Admin Perspective
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

    # Messaging and Communication
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

    # AI Assistant and Chatbot
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

    # Administrative Functions
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

    # System Navigation and General Help
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
    {
        "input": "How often should evaluations be conducted?",
        "output": "Evaluation frequency depends on your organization's policy. Administrators set up evaluation cycles which typically occur annually, semi-annually, or quarterly based on company requirements."
    },
    {
        "input": "What happens if I miss an evaluation deadline?",
        "output": "Managers should complete evaluations within the cycle period. If deadlines are missed, administrators can extend cycles or handle evaluations on a case-by-case basis."
    },
    {
        "input": "Can I export my evaluation data?",
        "output": "Administrators and managers can generate PDF reports containing evaluation data. Individual users can view their evaluation history in their profile but export capabilities depend on administrative permissions."
    },
    {
        "input": "Who can see my evaluation results?",
        "output": "Your evaluation results are visible to you, your manager (who conducted the evaluation), and system administrators. The system maintains confidentiality and appropriate access controls."
    },
    {
        "input": "How do I get training recommendations?",
        "output": "The AI system can analyze your evaluation feedback and suggest relevant training programs. Managers can also review your profile and unparticipated trainings to make recommendations."
    },
    {
        "input": "What if I disagree with my evaluation?",
        "output": "If you have concerns about your evaluation, discuss them directly with your manager first. For formal processes, contact your HR department or system administrator as per your organization's policies."
    },
    {
        "input": "Can I track my professional development over time?",
        "output": "Yes! Your profile shows your evaluation history and training participation statistics over time, helping you track your professional growth and development progress."
    },
    {
        "input": "Is my data secure in the system?",
        "output": "Yes, the system implements security best practices including encrypted passwords, secure HTTPS communication, session management, and role-based access controls to protect your data."
    },
    {
        "input": "How do I get technical support?",
        "output": "For technical issues, contact your system administrator or IT support team. You can also use the AI assistant for basic system usage questions and guidance."
    },
    {
        "input": "What should I do if the system is slow?",
        "output": "Try refreshing the page, clearing your browser cache, or checking your internet connection. If problems persist, report the issue to your system administrator."
    }
]