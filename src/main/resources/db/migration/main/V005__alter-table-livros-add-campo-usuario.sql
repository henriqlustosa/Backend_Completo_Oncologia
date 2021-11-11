ALTER TABLE livros ADD COLUMN usuario_id BIGINT NOT NULL;
ALTER TABLE livros ADD FOREIGN KEY (usuario_id) REFERENCES usuarios(id);