-- 1. Inserir Artistas conforme o enunciado
INSERT INTO artistas (nome, tipo) VALUES ('Serj Tankian', 'CANTOR');
INSERT INTO artistas (nome, tipo) VALUES ('Mike Shinoda', 'CANTOR');
INSERT INTO artistas (nome, tipo) VALUES ('Michel Teló', 'CANTOR');
INSERT INTO artistas (nome, tipo) VALUES ('Guns N’ Roses', 'BANDA');

-- 2. Inserir Álbuns
-- Serj Tankian
INSERT INTO albuns (titulo) VALUES ('Harakiri');
INSERT INTO albuns (titulo) VALUES ('Black Blooms');
INSERT INTO albuns (titulo) VALUES ('The Rough Dog');

-- Mike Shinoda
INSERT INTO albuns (titulo) VALUES ('The Rising Tied');
INSERT INTO albuns (titulo) VALUES ('Post Traumatic');
INSERT INTO albuns (titulo) VALUES ('Post Traumatic EP');
INSERT INTO albuns (titulo) VALUES ('Where’d You Go');

-- Michel Teló
INSERT INTO albuns (titulo) VALUES ('Bem Sertanejo');
INSERT INTO albuns (titulo) VALUES ('Bem Sertanejo - O Show (Ao Vivo)');
INSERT INTO albuns (titulo) VALUES ('Bem Sertanejo - (1ª Temporada) - EP');

-- Guns N’ Roses
INSERT INTO albuns (titulo) VALUES ('Use Your Illusion I');
INSERT INTO albuns (titulo) VALUES ('Use Your Illusion II');
INSERT INTO albuns (titulo) VALUES ('Greatest Hits');

-- 3. Vincular Artistas aos Álbuns na tabela N:N (artista_album)
-- Serj Tankian (ID 1)
INSERT INTO artista_album (artista_id, album_id) VALUES (1, 1), (1, 2), (1, 3);

-- Mike Shinoda (ID 2)
INSERT INTO artista_album (artista_id, album_id) VALUES (2, 4), (2, 5), (2, 6), (2, 7);

-- Michel Teló (ID 3)
INSERT INTO artista_album (artista_id, album_id) VALUES (3, 8), (3, 9), (3, 10);

-- Guns N’ Roses (ID 4)
INSERT INTO artista_album (artista_id, album_id) VALUES (4, 11), (4, 12), (4, 13);