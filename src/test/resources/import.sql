-- Usuarios
insert into usuario (codigo, clave, tipo_documento, numero_documento, nombres, apellidos, correo, telefono) values ('admin', 'admin', 'CC', '123123', 'Admin', 'Admin', 'henhav@gmail.com', '123456789');


-- Plantillas notificacion
insert into plantilla_notificacion (tipo, descripcion, plantilla) values ('generico', 'Mensaje de prueba genérico', '<html><body><h3 style="background-color: #d2d2ef; padding: 20px;">Hola <strong>${usuario.nombreCompleto}</strong>, bienvenido a la Oficina Virtual del Catastro!</h3><p>${mensaje}</p></body></html>');
insert into plantilla_notificacion (tipo, descripcion, plantilla) values ('usuario_registrado', 'Mensaje de prueba - Usuario registrado', '<html><body><h3 style="background-color: #d2d2ef; padding: 20px;">Hola <strong>${usuario.nombreCompleto}</strong>, bienvenido a la Oficina Virtual del Catastro!</h3><p>La cuenta ha sido creada satisfactoriamente y está asociada a tu dirección de correo <a href="mailto:${usuario.correo}">${usuario.correo}</a>.</p><p>${mensaje}</p></body></html>');


--Predio
insert into predio (numero_predio, municipio, numero_ficha, matricula, direccion, tipo_documento_usuario, id_propietario) values ('236','DonMatías' ,'3476','000956','cll 33 30 a 10 202','cc','123123');
insert into predio (numero_predio, municipio, numero_ficha, matricula, direccion, tipo_documento_usuario, id_propietario) values ('421','Venecia', '2817','000236','cr 25 27 18','cc','123123');
