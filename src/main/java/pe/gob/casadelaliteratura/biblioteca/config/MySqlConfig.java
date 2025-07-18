package pe.gob.casadelaliteratura.biblioteca.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MySqlConfig implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public MySqlConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM documentacion", Integer.class);

        if (count != null && count == 0) {

            // Triggers para agregar automaticamente los 14 dias de prestamo
            crearTriggerSet14diasPrestamo();
            crearTriggerSet14diasRenovacion();
            crearTriggerSetStateDetallePrestamo();

            // Tabla que almacena los ids
            insertarCodigosIniciales();

            // Carga de Data
            insertarDocumentacion();
            insertarClientes();
            insertarSalas();
            insertarColecciones();
            insertarEditoriales();
            insertarAutores();
            insertarLibrosDetalle();
            insertarLibros();
            insertarPrestamos();
            insertarDetallePrestamos();
        }
    }

    private void crearTriggerSet14diasPrestamo() {
        jdbcTemplate.execute("""
            CREATE TRIGGER trg_set_fecha_vencimiento_prestamo
            BEFORE INSERT ON prestamo
            FOR EACH ROW
            BEGIN
                IF NEW.fecha_vencimiento IS NULL THEN
                    SET NEW.fecha_vencimiento = DATE_ADD(NEW.fecha_prestamo, INTERVAL 14 DAY);
                END IF;
            END
        """);
    }

    private void crearTriggerSet14diasRenovacion() {
        jdbcTemplate.execute("""
            CREATE TRIGGER trg_set_fecha_vencimiento_renovacion
            BEFORE INSERT ON renovacion
            FOR EACH ROW
            BEGIN
                DECLARE fecha_actual_vencimiento DATE;

                SELECT MAX(nueva_fecha_vencimiento)
                INTO fecha_actual_vencimiento
                FROM renovacion
                WHERE id_prestamo = NEW.id_prestamo;

                IF fecha_actual_vencimiento IS NULL THEN
                    SELECT fecha_vencimiento
                    INTO fecha_actual_vencimiento
                    FROM prestamo
                    WHERE id_prestamo = NEW.id_prestamo;
                END IF;

                IF NEW.nueva_fecha_vencimiento IS NULL THEN
                    SET NEW.nueva_fecha_vencimiento = DATE_ADD(fecha_actual_vencimiento, INTERVAL 14 DAY);
                END IF;
            END
        """);
    }

    private void crearTriggerSetStateDetallePrestamo() {
        jdbcTemplate.execute("""
                CREATE TRIGGER trg_update_estado_libro
                BEFORE INSERT ON detalle_prestamo
                FOR EACH ROW
                BEGIN
                    UPDATE libro SET
                    estado = 'PRESTADO'
                    WHERE id_libro = NEW.id_libro;
                END
                """);
    }

    private void insertarCodigosIniciales() {
        jdbcTemplate.update("""
                INSERT INTO almacen_codigos (codigo, numero)
                VALUES
                ('CL', 11), ('SL', 3), ('CC', 17),
                ('AT', 11), ('ED', 11), ('LB', 21),
                ('DV', 1), ('PS', 8), ('PD', 1),
                ('RN', 1), ('SR', 1), ('SD', 1);
                """);
    }

    private void insertarDocumentacion() {
        jdbcTemplate.update("""
                INSERT INTO documentacion(img_doc_identidad, img_rec_servicio) VALUES
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png')),
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png')),
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png')),
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png')),
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png')),
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png')),
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png')),
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png')),
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png')),
                (LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\DNI_peruano.jpg'), LOAD_FILE('C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\recibo.png'));
           """);
    }

    private void insertarClientes() {
        jdbcTemplate.update("""
                INSERT INTO cliente(cod_cliente, apellidos, correo, direccion, fecha_nacimiento, nombres, numero_doc, numero_principal, numero_secundario, tipo_doc, id_documentacion) VALUES
                        ('CL00001', 'Ramírez Huamán', 'carlos.ramirez@example.com', 'Av. Los Álamos 123, San Juan de Lurigancho, Lima', '1995-04-15', 'Carlos Andrés', '12345678', '987654321', '912345678', 'DNI', 1),
                        ('CL00002', 'Gonzales Rojas', 'ana.gonzales@example.com', 'Calle Las Rosas 456, Miraflores, Lima', '1992-08-20', 'Ana María', '87654321', '998877665', '987654320', 'DNI', 2),
                        ('CL00003', 'Fernández López', 'jorge.fernandez@example.com', 'Jr. Amazonas 789, Trujillo, La Libertad', '1988-12-01', 'Jorge Luis', '11223344', '912345678', '987654322', 'DNI', 3),
                        ('CL00004', 'Torres Meza', 'maria.torres@example.com', 'Av. Grau 1025, Piura', '1990-05-10', 'María Elena', '33445566', '987654323', '912345679', 'DNI', 4),
                        ('CL00005', 'Vega Castillo', 'diego.vega@example.com', 'Calle Libertad 321, Cusco', '1993-07-19', 'Diego Armando', '55667788', '999888777', '911223344', 'DNI', 5),
                        ('CL00006', 'Sánchez Quispe', 'luz.sanchez@example.com', 'Jr. San Martín 55, Cajamarca', '1991-09-09', 'Luz Milagros', '66778899', '966554433', '977665544', 'DNI', 6),
                        ('CL00007', 'Paredes Inga', 'hugo.paredes@example.com', 'Av. El Sol 420, Huancayo', '1987-11-23', 'Hugo César', '77889900', '944556677', '911122233', 'DNI', 7),
                        ('CL00008', 'Reyes Salas', 'veronica.reyes@example.com', 'Calle Unión 777, Arequipa', '1996-03-30', 'Verónica Alejandra', '88990011', '933445566', '922334455', 'DNI', 8),
                        ('CL00009', 'Morales Díaz', 'luis.morales@example.com', 'Av. Túpac Amaru 1001, Iquitos', '1989-06-05', 'Luis Alberto', '99001122', '988776655', '933221100', 'DNI', 9),
                        ('CL00010', 'Chávez Ramos', 'karla.chavez@example.com', 'Jr. Puno 200, Ayacucho', '1994-10-17', 'Karla Yessenia', '10111213', '911122233', '900011223', 'DNI', 10);
            """);
    }

    private void insertarSalas() {
        jdbcTemplate.update("""
                INSERT INTO sala(cod_sala, nombre_sala) VALUES
                ('SL00001', 'Sala de Literatura Infantil Cota Carvallo'),
                ('SL00002', 'Biblioteca Mario Vargas Llosa');
        """);
    }

    private void insertarColecciones() {
        jdbcTemplate.update("""
                    INSERT INTO coleccion(cod_coleccion, descripcion, id_sala) VALUES
                    ('CC00001', 'El Pájaro Niño', 'SL00001'),
                    ('CC00002', 'Oshta y el duende', 'SL00001'),
                    ('CC00003', 'Rutsí, el pequeño alucinado', 'SL00001'),
                    ('CC00004', 'La flor del tiempo', 'SL00001'),
                    ('CC00005', 'Mario Vargas Llosa', 'SL00002'),
                    ('CC00006', 'Interdisciplinaria', 'SL00002'),
                    ('CC00007', 'Lima y Mapa Literario', 'SL00002'),
                    ('CC00008', 'Historieta y Novela Gráfica', 'SL00002'),
                    ('CC00009', 'Literatura Juvenil ', 'SL00002'),
                    ('CC00010', 'Publicaciones Casa de la Literatura', 'SL00002'),
                    ('CC00011', 'Literatura Peruana', 'SL00002'),
                    ('CC00012', 'Literatura Hispanoamericana', 'SL00002'),
                    ('CC00013', 'Literatura Universal', 'SL00002'),
                    ('CC00014', 'Estudios Literarios', 'SL00002'),
                    ('CC00015', 'Colección José María Arguedas', 'SL00002'),
                    ('CC00016', 'Publicaciones Periódicas', 'SL00002');
                """);
    }

    private void insertarEditoriales() {
        jdbcTemplate.update("""
                INSERT INTO editorial(cod_editorial, descripcion) VALUES
                ('ED00001', 'Andina'),
                ('ED00002', 'Planeta'),
                ('ED00003', 'Santillana'),
                ('ED00004', 'Alfaguara'),
                ('ED00005', 'Anagrama'),
                ('ED00006', 'SM'),
                ('ED00007', 'Norma'),
                ('ED00008', 'Ediciones B'),
                ('ED00009', 'Peisa'),
                ('ED00010', 'Lumen');
            """);
    }

    private void insertarAutores() {
        jdbcTemplate.update("""
                INSERT INTO autor(cod_autor, nacionalidad, nombre) VALUES
                ('AT00001', 'Peruana', 'Mario Vargas Llosa'),
                ('AT00002', 'Chilena', 'Isabel Allende'),
                ('AT00003', 'Colombiana', 'Gabriel García Márquez'),
                ('AT00004', 'Argentina', 'Jorge Luis Borges'),
                ('AT00005', 'Mexicana', 'Juan Rulfo'),
                ('AT00006', 'Española', 'Carlos Ruiz Zafón'),
                ('AT00007', 'Peruana', 'Julio Ramón Ribeyro'),
                ('AT00008', 'Uruguaya', 'Eduardo Galeano'),
                ('AT00009', 'Peruana', 'César Vallejo'),
                ('AT00010', 'Nicaragüense', 'Rubén Darío');
            """);
    }

    private void insertarLibrosDetalle() {
        jdbcTemplate.update("""
                INSERT INTO libro_detalle(cod_libro_detalle, isbn, titulo, year, id_autor, id_coleccion, id_editorial) VALUES
                ('LB00001', '978-84-376-0494-7', 'La ciudad y los perros', 1963, 'AT00001', 'CC00005', 'ED00001'),
                ('LB00002', '978-84-204-6724-6', 'La casa de los espíritus', 1982, 'AT00002', 'CC00012', 'ED00002'),
                ('LB00003', '978-84-376-0495-4', 'Cien años de soledad', 1967, 'AT00003', 'CC00013', 'ED00003'),
                ('LB00004', '978-84-339-1222-1', 'Ficciones', 1944, 'AT00004', 'CC00013', 'ED00004'),
                ('LB00005', '978-607-11-2001-0', 'Pedro Páramo', 1955, 'AT00005', 'CC00012', 'ED00005'),
                ('LB00006', '978-84-08-03759-3', 'La sombra del viento', 2001, 'AT00006', 'CC00013', 'ED00006'),
                ('LB00007', '978-84-339-7226-3', 'La palabra del mudo', 1974, 'AT00007', 'CC00011', 'ED00007'),
                ('LB00008', '978-84-663-4450-9', 'Las venas abiertas de América Latina', 1971, 'AT00008', 'CC00014', 'ED00008'),
                ('LB00009', '978-84-206-3410-7', 'Los Heraldos Negros', 1919, 'AT00009', 'CC00015', 'ED00009'),
                ('LB00010', '978-84-397-2174-1', 'Azul...', 1888, 'AT00010', 'CC00012', 'ED00010'),
                ('LB00011', '978-84-376-0494-8', 'Conversación en La Catedral', 1969, 'AT00001', 'CC00006', 'ED00001'),
                ('LB00012', '978-84-204-6724-7', 'Paula', 1994, 'AT00002', 'CC00011', 'ED00002'),
                ('LB00013', '978-84-376-0495-5', 'El amor en los tiempos del cólera', 1985, 'AT00003', 'CC00013', 'ED00003'),
                ('LB00014', '978-84-339-1222-2', 'El Aleph', 1949, 'AT00004', 'CC00013', 'ED00004'),
                ('LB00015', '978-607-11-2001-1', 'El llano en llamas', 1953, 'AT00005', 'CC00014', 'ED00005'),
                ('LB00016', '978-84-08-03759-4', 'El juego del ángel', 2008, 'AT00006', 'CC00013', 'ED00006'),
                ('LB00017', '978-84-339-7226-4', 'Prosas apátridas', 1975, 'AT00007', 'CC00011', 'ED00007'),
                ('LB00018', '978-84-663-4450-0', 'Memoria del fuego', 1986, 'AT00008', 'CC00014', 'ED00008'),
                ('LB00019', '978-84-206-3410-8', 'Poemas humanos', 1939, 'AT00009', 'CC00015', 'ED00009'),
                ('LB00020', '978-84-397-2174-2', 'Cantos de vida y esperanza', 1905, 'AT00010', 'CC00012', 'ED00010');
            """);
    }

    private void insertarLibros() {
        jdbcTemplate.update("""
                INSERT INTO libro(estado, numero_copia, id_libro_detalle) VALUES
                ('DISPONIBLE', 1, 'LB00001'), ('PRESTADO', 2, 'LB00001'), ('SOLO_PARA_LECTURA_EN_SALA', 3, 'LB00001'), ('DISPONIBLE', 4, 'LB00001'), ('DISPONIBLE', 5, 'LB00001'),
                ('DISPONIBLE', 1, 'LB00002'), ('PRESTADO', 2, 'LB00002'), ('DISPONIBLE', 3, 'LB00002'), ('SOLO_PARA_LECTURA_EN_SALA', 4, 'LB00002'), ('PRESTADO', 5, 'LB00002'),
                ('SOLO_PARA_LECTURA_EN_SALA', 1, 'LB00003'), ('DISPONIBLE', 2, 'LB00003'), ('DISPONIBLE', 3, 'LB00003'), ('PRESTADO', 4, 'LB00003'), ('DISPONIBLE', 5, 'LB00003'),
                ('DISPONIBLE', 1, 'LB00004'), ('DISPONIBLE', 2, 'LB00004'), ('SOLO_PARA_LECTURA_EN_SALA', 3, 'LB00004'), ('DISPONIBLE', 4, 'LB00004'), ('PRESTADO', 5, 'LB00004'),
                ('PRESTADO', 1, 'LB00005'), ('DISPONIBLE', 2, 'LB00005'), ('DISPONIBLE', 3, 'LB00005'), ('DISPONIBLE', 4, 'LB00005'), ('SOLO_PARA_LECTURA_EN_SALA', 5, 'LB00005'),
                ('DISPONIBLE', 1, 'LB00006'), ('PRESTADO', 2, 'LB00006'), ('DISPONIBLE', 3, 'LB00006'), ('DISPONIBLE', 4, 'LB00006'), ('SOLO_PARA_LECTURA_EN_SALA', 5, 'LB00006'),
                ('DISPONIBLE', 1, 'LB00007'), ('SOLO_PARA_LECTURA_EN_SALA', 2, 'LB00007'), ('PRESTADO', 3, 'LB00007'), ('DISPONIBLE', 4, 'LB00007'), ('DISPONIBLE', 5, 'LB00007'),
                ('DISPONIBLE', 1, 'LB00008'), ('DISPONIBLE', 2, 'LB00008'), ('PRESTADO', 3, 'LB00008'), ('SOLO_PARA_LECTURA_EN_SALA', 4, 'LB00008'), ('DISPONIBLE', 5, 'LB00008'),
                ('PRESTADO', 1, 'LB00009'), ('DISPONIBLE', 2, 'LB00009'), ('DISPONIBLE', 3, 'LB00009'), ('SOLO_PARA_LECTURA_EN_SALA', 4, 'LB00009'), ('DISPONIBLE', 5, 'LB00009'),
                ('DISPONIBLE', 1, 'LB00010'), ('SOLO_PARA_LECTURA_EN_SALA', 2, 'LB00010'), ('DISPONIBLE', 3, 'LB00010'), ('DISPONIBLE', 4, 'LB00010'), ('PRESTADO', 5, 'LB00010'),
                ('DISPONIBLE', 1, 'LB00011'), ('PRESTADO', 2, 'LB00011'), ('DISPONIBLE', 3, 'LB00011'), ('SOLO_PARA_LECTURA_EN_SALA', 4, 'LB00011'), ('DISPONIBLE', 5, 'LB00011'),
                ('DISPONIBLE', 1, 'LB00012'), ('SOLO_PARA_LECTURA_EN_SALA', 2, 'LB00012'), ('DISPONIBLE', 3, 'LB00012'), ('PRESTADO', 4, 'LB00012'), ('DISPONIBLE', 5, 'LB00012'),
                ('PRESTADO', 1, 'LB00013'), ('DISPONIBLE', 2, 'LB00013'), ('DISPONIBLE', 3, 'LB00013'), ('SOLO_PARA_LECTURA_EN_SALA', 4, 'LB00013'), ('DISPONIBLE', 5, 'LB00013'),
                ('DISPONIBLE', 1, 'LB00014'), ('SOLO_PARA_LECTURA_EN_SALA', 2, 'LB00014'), ('DISPONIBLE', 3, 'LB00014'), ('PRESTADO', 4, 'LB00014'), ('DISPONIBLE', 5, 'LB00014'),
                ('PRESTADO', 1, 'LB00015'), ('DISPONIBLE', 2, 'LB00015'), ('DISPONIBLE', 3, 'LB00015'), ('DISPONIBLE', 4, 'LB00015'), ('SOLO_PARA_LECTURA_EN_SALA', 5, 'LB00015'),
                ('DISPONIBLE', 1, 'LB00016'), ('PRESTADO', 2, 'LB00016'), ('DISPONIBLE', 3, 'LB00016'), ('SOLO_PARA_LECTURA_EN_SALA', 4, 'LB00016'), ('DISPONIBLE', 5, 'LB00016'),
                ('PRESTADO', 1, 'LB00017'), ('DISPONIBLE', 2, 'LB00017'), ('DISPONIBLE', 3, 'LB00017'), ('SOLO_PARA_LECTURA_EN_SALA', 4, 'LB00017'), ('DISPONIBLE', 5, 'LB00017'),
                ('DISPONIBLE', 1, 'LB00018'), ('DISPONIBLE', 2, 'LB00018'), ('SOLO_PARA_LECTURA_EN_SALA', 3, 'LB00018'), ('PRESTADO', 4, 'LB00018'), ('DISPONIBLE', 5, 'LB00018'),
                ('PRESTADO', 1, 'LB00019'), ('DISPONIBLE', 2, 'LB00019'), ('SOLO_PARA_LECTURA_EN_SALA', 3, 'LB00019'), ('DISPONIBLE', 4, 'LB00019'), ('DISPONIBLE', 5, 'LB00019'),
                ('DISPONIBLE', 1, 'LB00020'), ('SOLO_PARA_LECTURA_EN_SALA', 2, 'LB00020'), ('DISPONIBLE', 3, 'LB00020'), ('PRESTADO', 4, 'LB00020'), ('DISPONIBLE', 5, 'LB00020');
            """);
    }

    private void insertarPrestamos() {
        jdbcTemplate.update("""
                INSERT INTO prestamo(cod_prestamo, fecha_prestamo, id_cliente, estado_devolucion) VALUES
                ('PS00001', '2015-05-15', 'CL00001', 'DEVOLUCION_PENDIENTE'),
                ('PS00002', '2015-05-27', 'CL00002', 'DEVOLUCION_PENDIENTE'),
                ('PS00003', '2015-05-29', 'CL00003', 'DEVOLUCION_PENDIENTE'),
                ('PS00004', '2015-06-12', 'CL00004', 'DEVOLUCION_PENDIENTE'),
                ('PS00005', '2015-06-26', 'CL00005', 'DEVOLUCION_PENDIENTE'),
                ('PS00006', '2015-07-10', 'CL00006', 'DEVOLUCION_PENDIENTE'),
                ('PS00007', '2015-07-24', 'CL00007', 'DEVOLUCION_PENDIENTE');
            """);
    }

    private void insertarDetallePrestamos() {
        jdbcTemplate.update("""
                INSERT INTO detalle_prestamo(id_libro, id_prestamo) VALUES
                        (2, 'PS00001'), (7, 'PS00001'), (10, 'PS00001'), (14, 'PS00001'),
                        (20, 'PS00002'), (21, 'PS00002'),
                        (27, 'PS00003'),
                        (33, 'PS00004'), (38, 'PS00004'), (41, 'PS00004'), (50, 'PS00004'),
                        (52, 'PS00005'), (59, 'PS00005'), (61, 'PS00005'),
                        (69, 'PS00006'), (71, 'PS00006'), (77, 'PS00006'),
                        (81, 'PS00007'), (89, 'PS00007'), (91, 'PS00007'), (99, 'PS00007');
        """);
    }
}
