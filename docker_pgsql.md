## docker-compose.yml

```yml
services:
  app:
    image: proj3twildfly:latest
    build:
      context: ./wildfly
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
      - "8443:8443"
      - "9990:9990"
    volumes:
      - ./backend/target/jorge-osorio-proj3t/:/opt/wildfly/standalone/deployments/jorge-osorio-proj3t.war
      - ./backend/.env:/opt/wildfly/standalone/deployments/.env
    networks:
      - proj3t-network
    depends_on:
      postgres_db:
        condition: service_healthy
      ai_service:
        condition: service_started
    hostname: backend

  ai_service:
    build:
      context: ./ai_microservice
      dockerfile: Dockerfile
    ports:
      - "8000:8000"
    volumes:
      - ./ai_microservice:/app
    restart: unless-stopped
    environment:
      - LOG_LEVEL=info
    networks:
      - proj3t-network
    hostname: ai-service

  postgres_db:
    image: postgres:15
    environment:
      POSTGRES_DB: proj3t_db
      POSTGRES_USER: proj3t_user
      POSTGRES_PASSWORD: proj3t_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - proj3t-network
    hostname: postgres-db
    restart: unless-stopped
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U proj3t_user -d proj3t_db"]
      interval: 5s
      timeout: 5s
      retries: 5

  pgadmin:
    image: dpage/pgadmin4:latest
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@localhost.com
      PGADMIN_DEFAULT_PASSWORD: admin
      PGADMIN_CONFIG_SERVER_MODE: "False"
    ports:
      - "5050:80"
    networks:
      - proj3t-network
    depends_on:
      - postgres_db
    restart: unless-stopped

networks:
  proj3t-network:
    driver: bridge

volumes:
  postgres_data:
    driver: local
```

---

Alterar no persistence.xml o datasource para a db no docker

---

standalone.xml -> Data source config:

```xml
    <datasource jndi-name="java:/localPostgresDS" pool-name="localPostgresDS" enabled="true" use-java-context="true">
        <connection-url>jdbc:postgresql://postgres-db:5432/proj3t_db</connection-url>
        <driver>postgresql</driver>
        <security user-name="proj3t_user" password="proj3t_password"/>
        <pool>
            <min-pool-size>2</min-pool-size>
            <max-pool-size>20</max-pool-size>
            <prefill>true</prefill>
            <flush-strategy>FailingConnectionOnly</flush-strategy>
        </pool>
        <validation>
            <validate-on-match>true</validate-on-match>
            <background-validation>true</background-validation>
            <background-validation-millis>30000</background-validation-millis>
            <check-valid-connection-sql>SELECT 1</check-valid-connection-sql>
        </validation>
        <timeout>
            <blocking-timeout-millis>5000</blocking-timeout-millis>
            <idle-timeout-minutes>15</idle-timeout-minutes>
        </timeout>
    </datasource>
```

---

PGAdmin está containerizado e pode ser acedido por http://localhost:5050 usando as credenciais definidas no yml do docker.

---
