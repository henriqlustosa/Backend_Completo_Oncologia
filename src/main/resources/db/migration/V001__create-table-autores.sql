create table autores
(
    id              SERIAL      PRIMARY KEY,
    nome            varchar(60)  not null,
    email           varchar(60)  not null,
    data_nascimento date         not null,
    mini_curriculo  varchar(300) not null

)