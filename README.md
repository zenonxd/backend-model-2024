# Introdução

A ideia deste projeto é fixar meus conhecimentos em Java, Spring juntamente com Postgres e AWS.

[Video Fernanda Kipper](https://www.youtube.com/watch?v=d0KaNzAMVO4)

# Objetivo

Desenvolveremos um backend de uma aplicação para gerenciar eventos de tecnologia. Será permitido realizar:

- Cadastro;
- Listagem;
- Filtragem;
- Detalhamento de evento;
- Associação de cupons de desconto.

# Estrutura projeto

[ ] O sistema deve permitir que o usuário cadastre um evento com os seguintes campos:

- Titulo (obrigatório)
- Descrição (opcional)
- Data (obrigatório)
- Local (obrigatório, se presencial)
- Imagem (opcional)
- URL do evento (obrigatório, se remoto)

[ ] Eventos podem ser classificados como remotos ou presenciais

[ ] O sistema deve permitir que o usuário associe um ou mais cupons de desconto a um evento. Cada cupom deve possuir os 
seguintes campos:

- Código do cupom (obrigatório)
- Desconto percentual ou valor fixo (obrigatório)
- Data de validade (opcional)

[ ] O sistema deve listar os eventos cadastrados, com paginação. A listagem deve incluir:

- Título
- Data
- Local
- Tipo (remoto ou presencial)
- Banner
- Descrição

[ ] O sistema deve retornar somente eventos que ainda não aconteceram

[ ] O sistema deve permitir que o usuário filtre a lista de eventos pelos seguintes critérios:

- Título
- Data
- Local

[ ] O sistema deve permitir que o usuário consulte todos os detalhes de um evento específico, incluindo:

- Título
- Descrição
- Data
- Local
- Imagem
- URL do evento
- Lista de cupons ativos, com seus respectivos detalhes (código do cupom, desconto, data de validade)

# Modelagem UML

![img.png](img.png)

# Infraestrutura 

![img_1.png](img_1.png)

A Nossa infraestrutura ficará dentro da AWS.

Nosso servidor java vai rodar em uma máquina EC2 Java Server. Essa máquina EC2 fará queries de leitura/escrita em um
banco de dados SQL (Amazon Aurora).

Na frente do nosso Java Server, teremos uma internet gateway. Ela será responsável por expor a nossa aplicação para a
internet.

O EC2 (servidor java) fará o upload das imagens em bucket da Amazon S3.

Assim que ele realizar o upload, ele vai pegar a URL correspondente e na hora de salvar o evento na tabela
do Amazon Aurora, ele irá também salvar a URL da imagem salva no S3.

Tudo isso ficará dentro de uma VPC (virtual private cloud), onde ficarão os nossos componentes (EC2, Aurora e Internet 
Gateway).

E dentro do VPC teremos uma subnet privada, que só poderá ser acessada pelos componentes que estiverem dentro da VPC.

Assim, o nosso banco não ficará exposto para internet.

# Dependências

- Spring web;
- PostgreSQL driver;
- Spring Data JPA;
- Lombok;
- Spring Boot DevTools (para live reload, etc)

# Criação de classes e mapeamento

Criar pacote domain. Dentro do domain, cada entidade terá seu pacote.

## Event (domain.event)

```java
@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String description;
    private Date date;
    private Boolean remote;
    private String img_url;
    private String event_url;
}
```

## Coupon (domain.coupon)

```java
@Entity
@Table(name = "coupon")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Coupon {

    @Id
    @GeneratedValue
    private UUID id;
    private Integer discount;
    private String code;
    private Date valid;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
```

## Address (domain.address)

```java
@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {

    private UUID id;
    private String uf;
    private String city;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
```