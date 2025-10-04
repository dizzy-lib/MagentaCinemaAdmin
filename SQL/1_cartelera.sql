-- La base de datos ya está creada por Docker Compose
-- Solo la usamos
USE CINE_DB;

CREATE TABLE CARTELERA (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           titulo VARCHAR(150) NOT NULL,
                           director VARCHAR(50) NOT NULL,
                           anno INT NOT NULL,
                           duracion INT NOT NULL COMMENT "Duración en minutos",
                           genero VARCHAR(25) NOT NULL
);

-- VALIDADOR DE AÑO
ALTER TABLE CARTELERA
    ADD CONSTRAINT CHK_YEAR
        CHECK (anno >= 1000 AND anno <= 9999);

-- VERIFICACION DE DURACION MAYOR A 0 MINUTOS
ALTER TABLE CARTELERA
    ADD CONSTRAINT CHK_DURATION
        CHECK (duracion > 0);

-- VERIFICACION DE GENERO DE PELÍCULA
ALTER TABLE CARTELERA
    ADD CONSTRAINT CHK_GENERO
        CHECK (genero in ('COMEDIA', 'TERROR', 'DRAMA', 'ACCION', 'SUSPENSO', 'ROMANCE', 'ANIMADO'));