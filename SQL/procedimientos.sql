USE cine_db;

DROP PROCEDURE IF EXISTS sp_agregar_pelicula;

-- Procedimiento 1 - agregar películas
DELIMITER $$
CREATE PROCEDURE sp_agregar_pelicula(
    IN p_titulo VARCHAR(150),
    IN p_director VARCHAR(50),
    IN p_anno INT,
    IN p_duracion INT,
    IN p_genero VARCHAR(25)
)
BEGIN
INSERT INTO cartelera (titulo, director, anno, duracion, genero)
VALUES (p_titulo, p_director, p_anno, p_duracion, p_genero);
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_obtener_todas_las_peliculas()
BEGIN
SELECT * FROM cartelera;
END$$
DELIMITER ;

DELIMITER $$
    CREATE PROCEDURE sp_actualizar_pelicula(
        IN p_id INT,
        IN p_titulo VARCHAR(150),
        IN p_director VARCHAR(50),
        IN p_anno INT,
        IN p_duracion INT,
        IN p_genero VARCHAR(25)
    )
    BEGIN
        UPDATE cartelera set titulo = p_titulo,
                             director = p_director,
                             anno = p_anno,
                             duracion = p_duracion,
                             genero = p_genero
        where id = p_id;
    END$$
DELIMITER ;

DELIMITER $$
    CREATE PROCEDURE sp_eliminar_pelicula(
        IN p_id INT
    )
    BEGIN
       DELETE FROM cartelera WHERE id = p_id;
    END$$
DELIMITER ;