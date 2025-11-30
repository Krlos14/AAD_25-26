INSERT INTO alumno (nif, nombre, email)
VALUES
    ('11111111A', 'Miriam', 'miriam@g.educaand.es'),
    ('22222222B', 'Carlos', 'carlos@g.educaand.es'),
    ('33333333C', 'Laura', 'laura@g.educaand.es');

INSERT INTO modulo (codigo, nombre, horas)
VALUES
    ('0485', 'Programación', 250),
    ('0484', 'Bases de Datos', 200),
    ('0483', 'Lenguajes de Marcas', 120);

INSERT INTO matricula (id_alumno, id_modulo, fecha)
VALUES
    (1, 2, CURRENT_DATE),
    (2, 1, CURRENT_DATE),
    (2, 3, CURRENT_DATE),
    (3, 1, CURRENT_DATE);
