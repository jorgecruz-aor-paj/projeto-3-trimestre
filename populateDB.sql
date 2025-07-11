-- Workplaces
INSERT INTO "public"."workplace" ("id", "name") VALUES ('1', 'Lisboa'), ('2', 'Coimbra'), ('3', 'Porto'), ('4', 'Viseu'), ('5', 'Munich'), ('6', 'Boston'), ('7', 'Southampton');

-- Users
INSERT INTO "public"."users" ("id", "address", "biography", "birth_date", "created_at", "email", "firstname", "lastname", "password", "phonenumber", "photograph", "role", "manager_id", "workplace_id") VALUES ('13', '123 Avenida das Flores, Lisboa', 'Software developer with a passion for AI and web tech.', '1990-05-12', '2025-06-12 15:32:06.241922+00', 'osario@student.dei.uc.pt', 'João', 'Osório', '$2a$12$vAcJdMHf11ySMadNVZdWVOerAI7uMHbBRbLMQvOZ56Zgnf6utosU6', '912345679', 'base64-encoded-image-data', 'ADMIN', null, '4'), ('14', 'Rua Larga, Beco Estreito, 3', 'Napping professional.', '1983-04-30', '2025-06-12 15:41:49.120175+00', 'jorgecruz@student.dei.uc.pt', 'Jorge', 'Cruz', '$2a$12$6mYWHPSmM/mUEOGFDLh6c.pYPIve5oxv7n0tZkRBTWQpbnE66yFbe', '921234567', 'b6e10bc4-32a5-4e4f-bb67-dd4a13a768a6_20250613173702.png', 'MANAGER', '13', '5'), ('15', 'nowhere street, nº5', 'no', '1989-07-20', '2025-06-26 16:22:04.83724+00', 'osario@email.com', 'john', 'doe', '$2a$12$MAeLps6fMd6wmPlmo5SsQuATh1tHTXCT.w./ix3VhLtBvKUxYvkj.', '912454455', 'default-profile-picture.png', 'USER', '14', '3'), ('18', 'Rua do Amilcar 49', 'Sou o amilcar', '1990-06-12', '2025-07-01 10:06:38.96137+00', 'jdcruz83@outlook.pt', 'Amilcar', 'Alhadas', '$2a$12$w34RLJgKrtWvNS0WxY.MJeNHOfOMY7mTqApt/wUdh8TOzjI3goGpa', '456773567', 'default-profile-picture.png', 'USER', '14', '3');

-- Training Areas
INSERT INTO "public"."training_areas" ("id", "name") VALUES ('1', 'Backend Development'), ('4', 'Frontend Development');

-- Trainings
INSERT INTO "public"."trainings" ("id", "created_at", "description", "duration", "edited_at", "is_active", "language", "link", "title", "training_area_id") VALUES ('1', '2025-06-13 14:44:58.366409+00', 'Learn how to design, document, and secure RESTful APIs using best practices. Perfect for backend and full-stack developers.', '90', '2025-06-16 12:48:58.293953+00', 'true', 'EN', 'https://training.example.com/courses/restful-api-design', 'Intro to RESTful API Design', '1'), ('2', '2025-06-16 09:18:27.632549+00', 'Learn how to use react. Even idiots can learn', '90', '2025-06-16 09:19:28.703071+00', 'true', 'EN', 'https://training.example.com/courses/react101', 'Introduction to React', '4');

-- Training Participation
INSERT INTO "public"."training_participation" ("id", "created_at", "participated_at", "manager_id", "participant_id", "training_id") VALUES ('1', '2025-06-16 13:39:55.400735+00', '2024-07-22 15:30:00+00', '13', '14', '2'), ('2', '2025-06-16 14:10:02.939617+00', '2023-07-22 15:30:00+00', '13', '14', '2'), ('3', '2025-06-16 14:10:24.913831+00', '2024-07-22 15:30:00+00', '13', '14', '2'), ('4', '2025-06-16 14:10:28.122242+00', '2022-07-22 15:30:00+00', '13', '14', '2'), ('5', '2025-06-16 14:34:23.404891+00', '2024-07-22 15:30:00+00', '13', '14', '2'), ('6', '2025-06-30 17:04:35.007767+00', '2024-07-22 15:30:00+00', '14', '15', '2'), ('7', '2025-06-30 17:04:49.669081+00', '2024-07-22 15:30:00+00', '14', '15', '1'), ('8', '2025-06-30 17:05:03.575598+00', '2025-02-22 15:30:00+00', '14', '15', '2'), ('9', '2025-07-01 07:52:28.231299+00', '2025-02-22 15:30:00+00', '14', '15', '2');

-- Messages
INSERT INTO "public"."messages" ("id", "content", "is_read", "timestamp_read", "timestamp_sent", "recipient_id", "sender_id") VALUES ('1', 'Hello, this is a test message', 'true', '2025-07-02 10:37:38.274432+00', '2025-06-16 14:57:48.62244+00', '13', '14'), ('2', 'Olá! Esta é uma mensagem de teste de resposta!', 'true', '2025-07-02 10:37:40.055066+00', '2025-07-01 15:19:02.476702+00', '13', '14'), ('3', 'Posso?', 'true', '2025-07-01 21:47:16.180471+00', '2025-07-01 16:10:35.107157+00', '14', '13'), ('4', 'teste', 'true', '2025-07-01 21:47:17.38398+00', '2025-07-01 16:12:42.000672+00', '14', '13'), ('5', 'Teste bem sucedido, já vejo todas as mensagens e o websocket está operacional!', 'true', '2025-07-02 10:37:41.679427+00', '2025-07-01 21:48:15.433597+00', '13', '14'), ('6', 'Mensagem para testar a hora.', 'true', '2025-07-02 10:37:43.241441+00', '2025-07-01 22:15:32.887665+00', '13', '14'), ('7', 'Oi', 'true', '2025-07-03 10:15:19.268961+00', '2025-07-02 10:38:06.77259+00', '13', '14'), ('8', 'oi', 'true', '2025-07-02 10:38:44.406533+00', '2025-07-02 10:38:25.38347+00', '14', '13'), ('9', 'Mais um teste, mais uma volta.', 'true', '2025-07-03 10:15:20.689803+00', '2025-07-02 11:07:50.892529+00', '13', '14'), ('10', 'Vais ter boa avaliação, o teu gestor é um gajo altamente!', 'true', '2025-07-03 10:15:22.120568+00', '2025-07-02 11:08:08.340569+00', '13', '14'), ('11', 'É sim senhor', 'true', '2025-07-03 10:18:16.228915+00', '2025-07-03 10:16:30.154729+00', '14', '13');

-- Evaluation Cycles
INSERT INTO "public"."evaluation_cycles" ("id", "closure_date", "created_at", "end_date", "start_date", "status", "created_by") VALUES ('2', null, '2025-06-17 10:20:10.986184+00', '2025-08-30 23:59:59+00', '2025-06-30 00:00:00+00', 'CLOSED', '13'), ('4', null, '2025-06-17 10:27:06.654405+00', '2025-08-30 23:59:59+00', '2025-06-30 00:00:00+00', 'CLOSED', '13'), ('6', null, '2025-06-17 10:32:24.444888+00', '2025-08-30 23:59:59+00', '2025-06-30 00:00:00+00', 'CLOSED', '13'), ('7', '2025-06-17 15:07:04.734573+00', '2025-06-17 14:17:14.596973+00', '2025-08-30 23:59:59+00', '2025-06-30 00:00:00+00', 'CLOSED', '13'), ('8', '2025-04-18 14:51:41+00', '2025-06-18 09:39:57.63933+00', '2025-08-30 23:59:59+00', '2025-06-30 00:00:00+00', 'CLOSED', '13'), ('11', '2025-06-30 16:09:52.967375+00', '2025-06-18 14:59:18.128389+00', '2025-08-30 23:59:59+00', '2025-06-30 00:00:00+00', 'CLOSED', '13'), ('12', '2025-06-30 16:13:07.823016+00', '2025-06-30 16:10:13.098551+00', '2025-08-30 23:59:59+00', '2025-07-01 00:00:00+00', 'CLOSED', '13'), ('13', null, '2025-07-03 08:14:05.40244+00', '2025-08-30 23:59:59+00', '2025-07-18 00:00:00+00', 'ACTIVE', '13');

-- Evaluations
INSERT INTO "public"."evaluations" ("id", "created_at", "edited_at", "feedback", "scale", "status", "evaluated_id", "evaluation_cycle_id", "evaluator_id") VALUES ('3', '2025-06-17 10:32:24.818631+00', '2025-06-17 14:05:38.69108+00', 'Not great, not terrible', 'PARCIAL_CONTRIBUTION', 'CLOSED', '14', '6', '13'), ('4', '2025-06-17 14:17:15.054809+00', '2025-06-17 15:07:04.329148+00', 'Not great, not terrible', 'PARCIAL_CONTRIBUTION', 'CLOSED', '14', '7', '13'), ('5', '2025-06-18 09:39:58.09961+00', '2025-06-18 14:51:26.355866+00', 'something something', 'EXCEED_CONTRIBUTION', 'CLOSED', '14', '8', '13'), ('8', '2025-06-18 14:59:18.45737+00', '2025-06-30 16:09:26.265036+00', 'Very nice', 'EXCEED_CONTRIBUTION', 'CLOSED', '14', '11', '13'), ('9', '2025-06-30 16:10:13.767524+00', '2025-06-30 16:13:07.631451+00', 'MEH', 'PARCIAL_CONTRIBUTION', 'CLOSED', '14', '12', '13'), ('10', '2025-06-30 16:10:13.966843+00', '2025-06-30 16:13:07.631784+00', 'Tterrible', 'LOW_CONTRIBUTION', 'CLOSED', '15', '12', '14'), ('11', '2025-07-03 08:14:05.842094+00', '2025-07-03 10:55:18.317619+00', 'Just right', 'EXPECTED_CONTRIBUTION', 'CLOSED', '15', '13', '14'), ('12', '2025-07-03 08:14:06.008979+00', null, null, null, 'ONGOING', '14', '13', '13'), ('13', '2025-07-03 08:14:06.168837+00', '2025-07-03 10:55:18.317478+00', 'Lazy!', 'LOW_CONTRIBUTION', 'CLOSED', '18', '13', '14');

--Settings
INSERT INTO settings ( id, confirmation_expiration_time, created_at, reset_password_expiration_time, session_expiration_time, created_by ) VALUES (1,1440,NOW(),60,60,13);


-- Adjust sequences for all tables to ensure they match the current maximum IDs
-- Adjust sequence for 'workplace'
SELECT setval(pg_get_serial_sequence('public.workplace', 'id'), (SELECT MAX(id) FROM public.workplace));

-- Adjust sequence for 'users'
SELECT setval(pg_get_serial_sequence('public.users', 'id'), (SELECT MAX(id) FROM public.users));

-- Adjust sequence for 'training_areas'
SELECT setval(pg_get_serial_sequence('public.training_areas', 'id'), (SELECT MAX(id) FROM public.training_areas));

-- Adjust sequence for 'trainings'
SELECT setval(pg_get_serial_sequence('public.trainings', 'id'), (SELECT MAX(id) FROM public.trainings));

-- Adjust sequence for 'training_participation'
SELECT setval(pg_get_serial_sequence('public.training_participation', 'id'), (SELECT MAX(id) FROM public.training_participation));

-- Adjust sequence for 'messages'
SELECT setval(pg_get_serial_sequence('public.messages', 'id'), (SELECT MAX(id) FROM public.messages));

-- Adjust sequence for 'evaluation_cycles'
SELECT setval(pg_get_serial_sequence('public.evaluation_cycles', 'id'), (SELECT MAX(id) FROM public.evaluation_cycles));

-- Adjust sequence for 'evaluations'
SELECT setval(pg_get_serial_sequence('public.evaluations', 'id'), (SELECT MAX(id) FROM public.evaluations));

