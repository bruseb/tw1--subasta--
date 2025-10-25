-- Usuario de prueba (ADMIN)
INSERT INTO Usuario(id, email, password, rol, activo, nombre)
VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true, 'unlam');

-- ======================
-- Categorías principales
-- ======================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE categorias;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO categorias(id, nombre, nombreEnUrl)
VALUES
    (null, 'Electrónica', 'electronica'),
    (null, 'Moda', 'moda'),
    (null, 'Juegos y pasatiempos', 'juegos-y-pasatiempos'),
    (null, 'Hogar', 'hogar'),
    (null, 'Oficina', 'oficina'),
    (null, 'Deportes', 'deportes'),
    (null, 'Automotriz', 'automotriz'),
    (null, 'Bebés', 'bebes'),
    (null, 'Salud y belleza', 'salud-y-belleza');

-- ======================
-- Subcategorías
-- ======================

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE subcategorias;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO subcategorias (id, nombre, nombreEnUrl, categoria_id)
VALUES
    (NULL, 'Cámaras', 'camaras', 1),
    (NULL, 'Equipos de audio', 'equipos-de-audio', 1),
    (NULL, 'Belleza y bienestar', 'belleza-y-bienestar', 1),
    (NULL, 'Tabletas', 'tabletas', 1),
    (NULL, 'Ordenadores', 'ordenadores', 1),
    (NULL, 'Electrodomésticos', 'electrodomesticos', 1),
    (NULL, 'Equipos de vídeo', 'equipos-de-video', 1),
    (NULL, 'Smartphones', 'smartphones', 1),
    (NULL, 'Otros', 'otros', 1),

    (NULL, 'Ropa', 'ropa', 2),
    (NULL, 'Calzado', 'calzado', 2),
    (NULL, 'Bolsos', 'bolsos', 2),
    (NULL, 'Joyería', 'joyeria', 2),
    (NULL, 'Accesorios', 'accesorios', 2),
    (NULL, 'Relojes de pulsera', 'relojes-de-pulsera', 2),

    (NULL, 'Cómics / Anime', 'comics-anime', 3),
    (NULL, 'Tarjetas coleccionables', 'tarjetas-coleccionables', 3),
    (NULL, 'Figuras', 'figuras', 3),
    (NULL, 'Instrumentos musicales', 'instrumentos-musicales', 3),
    (NULL, 'Coleccionables', 'coleccionables', 3),
    (NULL, 'Arte', 'arte', 3),

    (NULL, 'Muebles', 'muebles', 4),
    (NULL, 'Decoración', 'decoracion', 4),
    (NULL, 'Electrodomésticos', 'electrodomesticos', 4),
    (NULL, 'Cocina y comedor', 'cocina-y-comedor', 4),
    (NULL, 'Ropa de cama y baño', 'ropa-de-cama-y-bano', 4),

    (NULL, 'Bolígrafos', 'boligrafos', 5),
    (NULL, 'Lápices', 'lapices', 5),
    (NULL, 'Cuadernos y diarios', 'cuadernos-y-diarios', 5),
    (NULL, 'Artículos de oficina', 'articulos-de-oficina', 5),

    (NULL, 'Ciclismo', 'ciclismo', 6),
    (NULL, 'Monopatín', 'monopatin', 6),
    (NULL, 'Snowboard', 'snowboard', 6),
    (NULL, 'Esquí', 'esqui', 6),
    (NULL, 'Entrenamiento de fuerza', 'entrenamiento-de-fuerza', 6),
    (NULL, 'Fútbol', 'futbol', 6),
    (NULL, 'Artes marciales y lucha', 'artes-marciales-y-lucha', 6),
    (NULL, 'Tenis', 'tenis', 6),
    (NULL, 'Otros', 'otros', 6),

    (NULL, 'Piezas de coche', 'piezas-de-coche', 7),
    (NULL, 'Accesorios', 'accesorios', 7),
    (NULL, 'Piezas de moto', 'piezas-de-moto', 7),
    (NULL, 'Accesorios de moto', 'accesorios-de-moto', 7),

    (NULL, 'Cochecitos y sillas de paseo', 'cochecitos-y-sillas-de-paseo', 8),
    (NULL, 'Pañales y accesorios para el cambio', 'panales-y-accesorios-para-el-cambio', 8),
    (NULL, 'Alimentación y lactancia', 'alimentacion-y-lactancia', 8),
    (NULL, 'Juguetes para bebés', 'juguetes-para-bebes', 8),
    (NULL, 'Muebles para bebés', 'muebles-para-bebes', 8),

    (NULL, 'Cuidado de la piel', 'cuidado-de-la-piel', 9),
    (NULL, 'Cuidado del cabello', 'cuidado-del-cabello', 9),
    (NULL, 'Maquillaje', 'maquillaje', 9),
    (NULL, 'Cuidado personal', 'cuidado-personal', 9);
    -- ======================
-- Subastas
-- ======================
    /*
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Subasta;  -- o subasta, según tu @Table(name = ...)
SET FOREIGN_KEY_CHECKS = 1;

-- Inserción de ejemplo

INSERT INTO Subasta (
    creador_id,
    titulo,
    descripcion,
    subcategoria_id,
    estadoProducto,
    precioInicial,
    precioActual,
    fechaInicio,
    fechaFin,
    estadoSubasta,
    imagen
)
VALUES (
    (SELECT u.id FROM Usuario u WHERE u.email = 'test@unlam.edu.ar'),
    'iPhone 13 128GB',
    'Equipo en excelente estado, con caja y cargador original.',
    (SELECT s.id FROM subcategorias s WHERE s.nombreEnUrl = 'smartphones'),
    'USADO',
    450000.00,
    450000.00,
    '2025-10-22 09:00:00',
    DATE_ADD('2025-10-22 09:00:00', INTERVAL 72 HOUR),
    10,
    NULL
);
*/