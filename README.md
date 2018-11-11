   Aplicação BackEnd para controle de checkin de hóspedes em um hotel.

   Sistema construido em Java, com o Framework Hibernate para ORM e o Framework Jersey para WebServices RESTful.
   Todas as dependências são resolvidas utilizando o Maven.
   Não é necessário nenhum build ou biblioteca adicional além do que está no arquivo pom.xml

   O banco de dados utilizado é o PostgreSQL.

   Comandos utilizados para criação do banco de dados.
- $ sudo -u postgres createdb hotel
- $ sudo -u postgres psql
- psql=# alter user postgres with encrypted password 'postgres';
- postgres=# grant all privileges on database hotel to postgres;
- postgres=# \c hotel;

CREATE TABLE hospede (
   id serial PRIMARY KEY,
   nome varchar (50) NOT NULL,
   documento varchar (25) NOT NULL,
   telefone varchar (25) NOT NULL
);

CREATE TABLE checkin (
    id serial PRIMARY KEY,
    hospede INTEGER REFERENCES hospede(id),
    data_entrada timestamp,
    data_saida timestamp,
    adicional_veiculo boolean
);


WebServices Rest

Serviço: Gravar Hóspede. 
Descrição: O sistema procura se ho hóspede já existe. Se existir atualiza os dados e se não existir cria um novo. 
method: POST 
url: localhost:8080/hotel/rest/hospede/gravar 
Media Type: JSON(application/json) 
body: {"nome":"Juca Silva","documento":"123-456-789","telefone":"3370-0000"} 

   Serviço: Consultar Hóspede.
   Descrição: A consulta pode ser feita pelo nome, documento ou telefone.
   method: POST
   url: localhost:8080/hotel/rest/hospede/consultar
   Media Type: JSON(application/json)
   body: {"nome":"Juca Silva"}

   Serviço: Excluir Hóspede.
   Descrição: O sistema exclui o hóspede desde que não tenha nenhum checkin feito.
   method: POST
   url: localhost:8080/hotel/rest/hospede/excluir
   Media Type: JSON(application/json)
   body: {"nome":"Juca Silva"}

   Serviço: Checkin - Entrada.
   Descrição: O sistema faz o checkin do hóspede na data informada. A data deve ter o formato ISO.
   method: POST
   url: localhost:8080/hotel/rest/checkin/entrar
   Media Type: JSON(application/json)
   body: [{"nome":"Juca Silva"},{"dataEntrada":"2018-11-09T18:30:00","adicionalVeiculo":true}]

   Serviço: Checkin - Saída.
   Descrição: O sistema faz o checkout do hóspede na data informada e mostra o valor total da estadia.
   method: POST
   url: localhost:8080/hotel/rest/checkin/sair
   Media Type: JSON(application/json)
   body: [{"nome":"Juca Silva"},{"dataSaida":"2018-11-12T16:00:00"}]

   Serviço: Consultar Histórico do Hóspede.
   Descrição: O sistema mostra uma lista com todos os checkins feitos pelo hóspede.
   method: POST
   url: localhost:8080/hotel/rest/checkin/consultarHistoricoHospede
   Media Type: JSON(application/json)
   body: {"nome":"Juca Silva"}

   Serviço: Listar Hóspedes.
   Descrição: O sistema mostra uma lista de hóspedes que já fizeram checkin.
   Parâmetro: true = hospedados  /  false=não hospedados
   method: POST
   url: localhost:8080/hotel/rest/hospede/listarHospedes
   Media Type: JSON(application/json)
   body: {"listarHospedados":true}


   Regra para cálculo do valor total da hospedagem:
- A cada zero hora (00:00:00) dos dias posteriores ao checkin é cobrada uma diária.
- Se o checkin é feito antes das 16:30:00 é cobrada uma diária adicional referente ao dia do checkin.
- Se o checkout é feito depois das 16:30:59 é cobrada uma diária adicional referente ao dia posterior ao checkout.


   Preços de diárias:
- A diária de segunda a sexta custa R$ 120,00 e finais de semana custa R$ 150,00.
- Se o cliente precisar de uma vaga na garagem há um acréscimo diário de R$ 15,00 de segunda a sexta e de R$ 20,00 aos finais de semana.

