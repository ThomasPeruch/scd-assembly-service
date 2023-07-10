Esse é o assembly-service, serviço responsável por gerenciar pautas e as sessões de votações.

Pré-requisitos
- Java 17
- Docker e docker compose 

Certifique-se de ter as seguintes portas livres na sua máquina para subir a aplicação:
8080 -> porta ocupada pela própria aplicação
5432 -> banco de dados PostgreSQL
5672 e 15672 -> mensageria com RabbitMq

Na pasta raíz do projeto executar o comando docker compose up -d

Tecnologias Utilizadas:
- Java 17
- Spring 3
- Springdoc open api integrado com interface do Swagger UI
- PostgreSQL(via docker compose)
- RabbitMq(via docker compose)
- Docker
