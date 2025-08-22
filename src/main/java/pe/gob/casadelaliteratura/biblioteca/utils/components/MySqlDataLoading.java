package pe.gob.casadelaliteratura.biblioteca.utils.components;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MySqlDataLoading implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public MySqlDataLoading(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM persona", Integer.class);

        if (count != null && count == 0) {

            // (Triggers)
            crearTriggerSet14diasPrestamo();
            crearTriggerSet14diasRenovacion();
            crearTriggerSetStateDetallePrestamo();
            crearTriggerSetSancionDemora();

            // Tabla que almacena los ids
            insertarCodigosIniciales();

            // Carga de Data
            insertarPersonas();
            insertarClientes();
            insertarUsuarios();
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

    // Se ejecuta antes de cada insert en la tabla prestamo.
    // Asigna automaticamente la fecha de vencimiento de cada prestamo registrado (14 dias).
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

    // Se ejecuta antes de cada insert en la tabla renovación.
    // En caso de renovación, toma la última fecha de vencimiento que se tenga registrada
    // y asigna ese valor en la tabla renovación.
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
                WHERE fk_cod_prestamo = NEW.fk_cod_prestamo;

                IF fecha_actual_vencimiento IS NULL THEN
                    SELECT fecha_vencimiento
                    INTO fecha_actual_vencimiento
                    FROM prestamo
                    WHERE cod_prestamo = NEW.fk_cod_prestamo;
                END IF;

                IF NEW.nueva_fecha_vencimiento IS NULL THEN
                    SET NEW.nueva_fecha_vencimiento = DATE_ADD(fecha_actual_vencimiento, INTERVAL 14 DAY);
                END IF;
            END
        """);
    }

    // Se ejecuta antes de cada insert en la tabla detalle_prestamo.
    // Actualiza el estado a 'PRESTADO' en cada libro que se solicitó su prestamo.
    private void crearTriggerSetStateDetallePrestamo() {
        jdbcTemplate.execute("""
                CREATE TRIGGER trg_update_estado_libro
                BEFORE INSERT ON detalle_prestamo
                FOR EACH ROW
                BEGIN
                    UPDATE libro SET
                    estado = 'PRESTADO'
                    WHERE id_libro = NEW.fk_cod_libro;
                END
                """);
    }

    // Se ejecuta antes de cada update en la tabla prestamo.
    // Aplica una sanción de 2 días por cada día de demora en caso se
    // detecte un retraso en la devolución del préstamo.
    private void crearTriggerSetSancionDemora() {
        jdbcTemplate.execute("""
                CREATE TRIGGER trg_sancion_demora
                BEFORE UPDATE ON prestamo
                FOR EACH ROW
                BEGIN
                
                    DECLARE fecha_actual DATE;
                    DECLARE ultima_fecha_vencimiento_prestamo DATE;
                    DECLARE fecha_devolucion_reg DATE;
                    DECLARE dias_retraso INT;
                    DECLARE dias_suspension INT;
                
                    SET fecha_actual = CURDATE();
                
                    -- OBTENER LA ULTIMA FECHA VENCIMIENTO REGISTRADA EN LA TABLA RENOVACION
                    SELECT MAX(nueva_fecha_vencimiento) INTO ultima_fecha_vencimiento_prestamo
                	FROM RENOVACION
                	WHERE fk_cod_prestamo = OLD.cod_prestamo;
                
                    -- SI ES NULL ENTONCES NO SE HAN REGISTRADO RENOVACIONES, POR LO QUE SE TOMA
                    -- LA FECHA VENCIMIENTO ORIGINAL QUE SE REGISTRO EN EL PRESTAMO
                	IF ultima_fecha_vencimiento_prestamo IS NULL THEN
                		SET ultima_fecha_vencimiento_prestamo = OLD.fecha_vencimiento;
                	END IF;
                
                	-- DEVOLUCION REGULAR
                	IF NEW.estado_devolucion = 'DEVOLUCION_COMPLETA' AND
                	   OLD.estado_devolucion = 'DEVOLUCION_PENDIENTE' THEN
                
                        SELECT fecha_devolucion INTO fecha_devolucion_reg
                        FROM devolucion
                        WHERE fk_cod_prestamo =  OLD.cod_prestamo;
                
                    -- DEVOLUCION CON PROBLEMA Y SOLUCION REGISTRADA
                    ELSEIF NEW.estado_devolucion = 'DEVOLUCION_COMPLETA' AND
                	     OLD.estado_devolucion = 'DEVOLUCION_PARCIAL' THEN
                
                        SELECT MAX(sd.fecha_solucion) INTO fecha_devolucion_reg
                        FROM solucion_devolucion sd
                		INNER JOIN problema_devolucion pd ON pd.cod_problema_devolucion = sd.fk_cod_problema_devolucion
                		INNER JOIN detalle_devolucion dd ON dd.id_detalle_devolucion = pd.fk_cod_detalle_devolucion
                		INNER JOIN devolucion dv ON dv.cod_devolucion = dd.fk_cod_devolucion
                		WHERE dv.fk_cod_prestamo = OLD.cod_prestamo;
                
                    END IF;
                
                    IF fecha_devolucion_reg > ultima_fecha_vencimiento_prestamo THEN
                
                		SET dias_retraso = DATEDIFF(fecha_devolucion_reg, ultima_fecha_vencimiento_prestamo);
                		SET dias_suspension = dias_retraso * 2;
                
                		INSERT INTO sancion_demora (dias_suspension, fecha_inicio_sancion, fecha_fin_sancion, fk_cod_devolucion)
                		VALUES (
                			dias_suspension,
                			fecha_actual,
                			DATE_ADD(fecha_actual, INTERVAL dias_suspension DAY),
                			(SELECT cod_devolucion FROM devolucion WHERE fk_cod_prestamo = OLD.cod_prestamo)
                		);
                
                	END IF;
                
                END
                """);
    }

    // Carga los valores iniciales de la tabla almacen_codigos, la cual
    // almacena los ultimos id's de algunas tablas. Otras tablas generan sus
    // propios id's al ser incrementales.
    private void insertarCodigosIniciales() {
        jdbcTemplate.update("""
                INSERT INTO almacen_codigos (codigo, numero)
                VALUES
                ('PA', 14), ('US', 4),
                ('CL', 11), ('SL', 3), ('CC', 17),
                ('AT', 11), ('ED', 11), ('LB', 21),
                ('DV', 1), ('PS', 8), ('PD', 1),
                ('SD', 1);
                """);
    }

    private void insertarPersonas() {
        jdbcTemplate.update("""
                INSERT INTO persona (cod_persona, apellidos, correo, direccion, fecha_nacimiento, nombres, numero_doc, numero_principal, tipo_doc)
                VALUES
                ('PA00001', 'Ramírez Huamán', 'carlos.ramirez@example.com', 'Av. Los Álamos 123, San Juan de Lurigancho, Lima', '1995-04-15', 'Carlos Andrés', '12345678', '987654321', 'DNI'),
                ('PA00002', 'Gonzales Quispe', 'mariana.gonzales@example.com', 'Jr. Ayacucho 456, Cercado de Lima, Lima', '1992-08-22', 'Mariana Luisa', '23456789', '912345678', 'DNI'),
                ('PA00003', 'Torres Salazar', 'jorge.torres@example.com', 'Av. Brasil 789, Magdalena del Mar, Lima', '1988-03-09', 'Jorge Luis', '34567890', '987321456', 'DNI'),
                ('PA00004', 'Flores Chávez', 'laura.flores@example.com', 'Calle Las Violetas 234, Surco, Lima', '1996-11-30', 'Laura Beatriz', '45678901', '911222333', 'DNI'),
                ('PA00005', 'Morales Ríos', 'diego.morales@example.com', 'Av. Los Próceres 101, San Miguel, Lima', '1990-07-18', 'Diego Armando', '56789012', '923456789', 'DNI'),
                ('PA00006', 'Vásquez Mendoza', 'elena.vasquez@example.com', 'Jr. Cahuide 321, Pueblo Libre, Lima', '1993-12-12', 'Elena Mercedes', '67890123', '934567890', 'DNI'),
                ('PA00007', 'Rojas Cárdenas', 'mario.rojas@example.com', 'Av. La Marina 456, La Perla, Callao', '1987-02-25', 'Mario Javier', '78901234', '945678901', 'DNI'),
                ('PA00008', 'Sánchez Paredes', 'sofia.sanchez@example.com', 'Calle Los Girasoles 145, Los Olivos, Lima', '1998-06-05', 'Sofía Isabel', '89012345', '956789012', 'DNI'),
                ('PA00009', 'Delgado Núñez', 'andres.delgado@example.com', 'Av. Las Torres 654, Independencia, Lima', '1991-09-14', 'Andrés Felipe', '90123456', '967890123', 'DNI'),
                ('PA00010', 'Pérez Romero', 'karla.perez@example.com', 'Jr. Moquegua 123, Breña, Lima', '1994-05-11', 'Karla Cecilia', '01234567', '978901234', 'DNI'),
                ('PA00011', 'Chávez Soto', 'luis.chavez@example.com', 'Calle Arica 777, Barranco, Lima', '1989-10-20', 'Luis Enrique', '11223344', '989012345', 'DNI'),
                ('PA00012', 'Castro León', 'valeria.castro@example.com', 'Av. Javier Prado 2222, San Borja, Lima', '1997-01-28', 'Valeria Fernanda', '22334455', '990123456', 'DNI'),
                ('PA00013', 'Mendoza Campos', 'juan.mendoza@example.com', 'Jr. Tarapacá 555, Jesús María, Lima', '1985-04-03', 'Juan Manuel', '33445566', '901234567', 'DNI');
                """);
    }

    private void insertarClientes() {
        jdbcTemplate.update("""
                INSERT INTO cliente(cod_cliente, numero_secundario, url_doc_identidad, url_rec_servicio, fk_cod_persona)
                VALUES
                ('CL00001', '912345678', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00001'),
                ('CL00002', '913456789', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00002'),
                ('CL00003', '914567890', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00003'),
                ('CL00004', '915678901', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00004'),
                ('CL00005', '916789012', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00005'),
                ('CL00006', '917890123', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00006'),
                ('CL00007', '918901234', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00007'),
                ('CL00008', '919012345', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00008'),
                ('CL00009', '920123456', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00009'),
                ('CL00010', '921234567', 'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390652/biblioteca/i7mnysaohbdq57hphv1w.jpg',
                'https://res.cloudinary.com/dgsgtffmx/image/upload/v1753390650/biblioteca/d2mhldldyr7yypusqxqf.png', 'PA00010');
                """);
    }

    private void insertarUsuarios() {
        jdbcTemplate.update("""
                INSERT INTO usuario(cod_usuario, password, rol, fk_cod_persona, estado)
                VALUES
                ('US00001', '$2a$10$pCzDI6ga12ZnqnyP26hF.Os7.Ep83CvovZkDOknOrWVmgA2mx3OAy', 'ADMINISTRADOR', 'PA00011', 'ACTIVO'),
                ('US00002', '$2a$10$pCzDI6ga12ZnqnyP26hF.Os7.Ep83CvovZkDOknOrWVmgA2mx3OAy', 'BIBLIOTECARIO', 'PA00012', 'ACTIVO'),
                ('US00003', '$2a$10$pCzDI6ga12ZnqnyP26hF.Os7.Ep83CvovZkDOknOrWVmgA2mx3OAy', 'BIBLIOTECARIO', 'PA00013', 'INACTIVO');
                """);
    }

    private void insertarSalas() {
        jdbcTemplate.update("""
                INSERT INTO sala(cod_sala, nombre_sala)
                VALUES
                ('SL00001', 'Sala de Literatura Infantil Cota Carvallo'),
                ('SL00002', 'Biblioteca Mario Vargas Llosa');
                """);
    }

    private void insertarColecciones() {
        jdbcTemplate.update("""
                INSERT INTO coleccion(cod_coleccion, descripcion, fk_cod_sala)
                VALUES
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
                INSERT INTO editorial(cod_editorial, descripcion)
                VALUES
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
                INSERT INTO autor(cod_autor, nacionalidad, nombre)
                VALUES
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
                INSERT INTO libro_detalle(cod_libro_detalle, isbn, titulo, year, fk_cod_autor, fk_cod_coleccion, fk_cod_editorial)
                VALUES
                ('LB00001', '9788437604947', 'La ciudad y los perros', 1963, 'AT00001', 'CC00005', 'ED00001'),
                ('LB00002', '9788420467246', 'La casa de los espíritus', 1982, 'AT00002', 'CC00012', 'ED00002'),
                ('LB00003', '9788437604954', 'Cien años de soledad', 1967, 'AT00003', 'CC00013', 'ED00003'),
                ('LB00004', '9788433912221', 'Ficciones', 1944, 'AT00004', 'CC00013', 'ED00004'),
                ('LB00005', '9786071120010', 'Pedro Páramo', 1955, 'AT00005', 'CC00012', 'ED00005'),
                ('LB00006', '9788408037593', 'La sombra del viento', 2001, 'AT00006', 'CC00013', 'ED00006'),
                ('LB00007', '9788433972263', 'La palabra del mudo', 1974, 'AT00007', 'CC00011', 'ED00007'),
                ('LB00008', '9788466344509', 'Las venas abiertas de América Latina', 1971, 'AT00008', 'CC00014', 'ED00008'),
                ('LB00009', '9788420634107', 'Los Heraldos Negros', 1919, 'AT00009', 'CC00015', 'ED00009'),
                ('LB00010', '9788439721741', 'Azul...', 1888, 'AT00010', 'CC00012', 'ED00010'),
                ('LB00011', '9788437604948', 'Conversación en La Catedral', 1969, 'AT00001', 'CC00006', 'ED00001'),
                ('LB00012', '9788420467247', 'Paula', 1994, 'AT00002', 'CC00011', 'ED00002'),
                ('LB00013', '9788437604955', 'El amor en los tiempos del cólera', 1985, 'AT00003', 'CC00013', 'ED00003'),
                ('LB00014', '9788433912222', 'El Aleph', 1949, 'AT00004', 'CC00013', 'ED00004'),
                ('LB00015', '9786071120011', 'El llano en llamas', 1953, 'AT00005', 'CC00014', 'ED00005'),
                ('LB00016', '9788408037594', 'El juego del ángel', 2008, 'AT00006', 'CC00013', 'ED00006'),
                ('LB00017', '9788433972264', 'Prosas apátridas', 1975, 'AT00007', 'CC00011', 'ED00007'),
                ('LB00018', '9788466344500', 'Memoria del fuego', 1986, 'AT00008', 'CC00014', 'ED00008'),
                ('LB00019', '9788420634108', 'Poemas humanos', 1939, 'AT00009', 'CC00015', 'ED00009'),
                ('LB00020', '9788439721742', 'Cantos de vida y esperanza', 1905, 'AT00010', 'CC00012', 'ED00010');
                """);
    }

    private void insertarLibros() {
        jdbcTemplate.update("""
                INSERT INTO libro(estado, numero_copia, fk_cod_libro_detalle)
                VALUES
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
                INSERT INTO prestamo(cod_prestamo, fecha_prestamo, fk_cod_cliente, estado_devolucion, fk_cod_usuario)
                VALUES
                ('PS00001', '2025-07-15', 'CL00001', 'DEVOLUCION_PENDIENTE', 'US00002'),
                ('PS00002', '2025-07-27', 'CL00002', 'DEVOLUCION_PENDIENTE', 'US00002'),
                ('PS00003', '2025-07-29', 'CL00003', 'DEVOLUCION_PENDIENTE', 'US00002'),
                ('PS00004', '2025-07-31', 'CL00004', 'DEVOLUCION_PENDIENTE', 'US00002'),
                ('PS00005', '2025-08-01', 'CL00005', 'DEVOLUCION_PENDIENTE', 'US00002'),
                ('PS00006', '2025-08-06', 'CL00006', 'DEVOLUCION_PENDIENTE', 'US00002'),
                ('PS00007', '2025-08-10', 'CL00007', 'DEVOLUCION_PENDIENTE', 'US00002');
                """);
    }

    private void insertarDetallePrestamos() {
        jdbcTemplate.update("""
                INSERT INTO detalle_prestamo(fk_cod_libro, fk_cod_prestamo)
                VALUES
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
