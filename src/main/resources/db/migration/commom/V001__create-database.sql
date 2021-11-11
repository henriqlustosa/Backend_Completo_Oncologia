create table livros (
    id bigint(20) primary key auto_increment,
    titulo varchar(200) not null,
    numero_paginas int not null,
    data_de_lancamento date not null
);
create table autores (
    id bigint(20) primary key auto_increment,
    nome varchar(100) not null,
    email varchar(100) not null,
    data_nascimento date not null,
    mini_curriculo text
);
alter table livros add column autor_id bigint not null;
alter table livros add foreign key (autor_id) references autores(id);
create table usuarios (
    id bigint(20) primary key auto_increment,
    nome varchar(200) not null,
    login varchar(200) unique not null,
    senha varchar(255) not null
);
create table perfis (
  id bigint PRIMARY KEY AUTO_INCREMENT,
  nome varchar(100) not null
);

create table perfis_usuarios (
  usuario_id bigint not null,
  perfil_id bigint not null,

  primary key(usuario_id, perfil_id),
  foreign key(usuario_id) references usuarios(id),
  foreign key(perfil_id) references perfis(id)
);

insert into perfis (nome) values ('ROLE_ADMIN');
insert into perfis (nome) values ('ROLE_USER');

ALTER TABLE livros ADD COLUMN usuario_id BIGINT NOT NULL;
ALTER TABLE livros ADD FOREIGN KEY (usuario_id) REFERENCES usuarios(id);