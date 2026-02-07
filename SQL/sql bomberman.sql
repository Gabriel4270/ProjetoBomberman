CREATE SCHEMA IF NOT EXISTS public;

SET search_path TO public;

CREATE TABLE estado_jogo (
    id SERIAL PRIMARY KEY,
    bomberman_x INT,
    bomberman_y INT,
    bombas_restantes INT,
    caixas_posicoes TEXT
);

INSERT INTO estado_jogo (id, bomberman_x, bomberman_y, bombas_restantes, caixas_posicoes)
VALUES (1, 125, 100, 3, '');

SELECT * FROM estado_jogo;