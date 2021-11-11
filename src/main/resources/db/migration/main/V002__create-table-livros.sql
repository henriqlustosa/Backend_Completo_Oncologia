create table livros
(
    id              SERIAL      PRIMARY KEY,
    titulo          varchar(100) not null,
    data_de_lancamento date         not null,
    numero_paginas          int          not null,
    autor_id        bigint       not null,
    foreign key (autor_id) references autores (id)
);