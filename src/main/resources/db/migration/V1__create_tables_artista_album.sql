-- Criação da tabela de Artistas usando BIGSERIAL para compatibilidade com Long
CREATE TABLE artistas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL
);

-- Criação da tabela de Álbuns usando BIGSERIAL
CREATE TABLE albuns (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    imagem_capa_url VARCHAR(255)
);

-- Tabela intermediária com tipos BIGINT para as chaves estrangeiras
CREATE TABLE artista_album (
    album_id BIGINT NOT NULL,
    artista_id BIGINT NOT NULL,
    PRIMARY KEY (album_id, artista_id),
    CONSTRAINT fk_album FOREIGN KEY (album_id) REFERENCES albuns(id) ON DELETE CASCADE,
    CONSTRAINT fk_artista FOREIGN KEY (artista_id) REFERENCES artistas(id) ON DELETE CASCADE
);
