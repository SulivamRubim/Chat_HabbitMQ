# 📡 Chat com RabbitMQ

Este projeto consiste em uma aplicação Java que simula um sistema de comunicação assíncrona utilizando o RabbitMQ como intermediário (broker) de mensagens. A aplicação é dividida em dois serviços principais: Producer (produtor) e Consumer (consumidor). Toda a aplicação está containerizada usando Docker e orquestrada com Docker Compose.

# 📚 Pré-requisitos
Certifique-se de ter os seguintes softwares instalados:

- Docker

- Docker Compose

- JDK 21 ou superior

# 🚀 Como Executar a Aplicação
 ### Siga estes passos para executar a aplicação corretamente:

### Passo 1: Clonar o Repositório

    git clone https://github.com/SulivamRubim/Chat_HabbitMQ.git


### Passo 2: Executar com Docker Compose

     docker-compose up --build -d

### Passo 3: Verificar containers em execução

        docker ps

### Passo 4: Testar o chat

    docker attach chat_habbitmq-main-producer-1

- Digite mensagens no console e envie pressionando Enter:

- Digite uma das opcões do menu:

### Passo 5: Visualizar o Consumer

- Observe em tempo real as mensagens recebidas pelo consumidor:

         docker-compose logs -f consumer 

### Passo 6: Parar a Aplicação

- Finalize os serviços Docker com:

        docker-compose down

# 🖥️ Monitoramento do RabbitMQ

Acesse o painel web do RabbitMQ através do navegador:

URL: http://localhost:15672

Usuário padrão: guest

Senha padrão: guest

No painel, você pode monitorar filas, mensagens pendentes e conexões entre os serviços.

# 📂 Estrutura do Projeto

A estrutura do projeto é organizada da seguinte maneira:

.
├── consumer/                  
│   ├── src/main/java/...      
│   ├── Dockerfile             
│   └── pom.xml                
├── producer/                  
│   ├── src/main/java/...       
│   ├── Dockerfile              
│   └── pom.xml                 
├── docker-compose.yml          
├── pom.xml                     
└── README.md                 
