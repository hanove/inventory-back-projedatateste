Este repositório, assim como o `inventory-front-projedatateste`, é parte de um teste técnico.
## Instalação
1. Clone o repositório: `git clone`
2. Dentro do mesmo diretório onde este repositório foi clonado, clone o repositório do frontend disponível em: https://github.com/hanove/inventory-front-projedatatest
3. Navegue até o diretório da api: `cd inventory-back-projedatateste`
4. Execute `docker-compose up`. Certifique-se de ter o Docker instalado e em execução.
5. A API estará disponível em `http://localhost:8000`
6. A aplicação frontend estará disponível em `http://localhost:5173`
## Principais Tecnologias Utilizadas
### Backend
- Spring Boot - Devido ao meu limitado tempo de desenvolvimento, optei por utilizar o Spring Boot ao invés do Quarkus por já ter uma prévia experiência com a tecnologia.
- PostgreSQL
- Docker
### Frontend
- React
- Zustand - Novamente, devido ao tempo limitado, optei por utilizar o Zustand ao invés de Redux.