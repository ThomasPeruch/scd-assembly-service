Esse é o assembly-service, serviço responsável por gerenciar pautas e as sessões de votações.

Pré-requisitos
- Java 17
- Docker e docker compose 

Certifique-se de ter as seguintes portas livres na sua máquina para subir a aplicação:<br>
- 8080 -> porta ocupada pela própria aplicação
- 5432 -> banco de dados PostgreSQL
- 5672 e 15672 -> mensageria com RabbitMq

Na pasta raíz do projeto executar o comando docker compose up -d, esse comando criará containers com as especificações descritas no arquivo docker-compose.yml, nesse caso subirá uma instância do banco PostgreSQL e o serviço de mensageria do RabbitMq.

Tecnologias Utilizadas:
- Java 17
- Spring 3
- Springdoc open api integrado com interface do Swagger UI
- PostgreSQL(via docker compose)
- RabbitMq(via docker compose)
- Docker

<h2>Descrição do projeto</h2>
Serviço responsável por gerenciar as sessões de votação, criando pautas e sessões de votações e avisando o vote-service quando uma votação expirou(foi encerrada). <br>
Possui as seguintes funcionalidades(endpoints): <br>
<strong>PAUTAS:</strong>

- Criar uma pauta:<br>
Request : http://localhost:8080/topic POST:<br>
````
{
    "name":"pauta 4"
}
````
Response:
````
{
    "id": 4,
    "name": "pauta 4"
}
````
- Busca todas as pautas:<br>
Request : http://localhost:8080/topic GET<br>
Response :
````
[
    {
        "id": 1,
        "name": "pauta 1"
    },
    {
        "id": 2,
        "name": "pauta 2"
    },
    {
        "id": 3,
        "name": "pauta 3"
    },
    {
        "id": 4,
        "name": "pauta 4"
    }
]
````
<h3>SESSÕES:</h3>

- Abre uma sessão de votação:<br>
Request : http://localhost:8080/session POST
````
{
    "topicId":4,
    "sessionTimeInMinutes":20
}
````
Response:
````
{
    "id": 4,
    "topicId": 4,
    "votingStart": "10/07/2023 12:30:39",
    "votingEnd": "10/07/2023 12:50:39"
}
````
- Busca todas as sessões<br>
Request : http://localhost:8080/session GET<br>
Response:
````
[
    {
        "id": 1,
        "topicId": 1,
        "votingStart": "10/07/2023 11:22:04",
        "votingEnd": "10/07/2023 11:26:04",
        "isFinished": true,
    },
    {
        "id": 2,
        "topicId": 2,
        "votingStart": "10/07/2023 11:28:22",
        "votingEnd": "10/07/2023 15:00:00",
        "isFinished": false
    }
]
````
- Fechar as sessões<br>
Possui um job que executa no segundo 30 de cada minuto, verificando na tabela session se o campo end_date é de uma data que ja passou (end_date < now()), caso seja é montado um objeto e enviado para o vote-service, para o avisar que a sessão que fechou.

Swagger do serviço : http://127.0.0.1:8080/swagger-ui.html (serviço deve estar rodando)
Inteface do RabbitMq : http://127.0.0.0:15672/#/ (container com o rabbit deve estar rodando)
