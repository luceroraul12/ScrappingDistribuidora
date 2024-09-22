--DATA
insert into lookup_tipo(codigo, descripcion) 
	values
		('EFFECT_TYPE', 'Tipo de efecto');
	
insert into lookup_valor(lookup_tipo_id ,codigo, descripcion) 
	values
		((select id from lookup_tipo where codigo = 'EFFECT_TYPE'),
		'EFFECT_POSITIVE', 
		'Efecto positivo');

insert into lookup_valor(lookup_tipo_id ,codigo, descripcion) 
	values
		((select id from lookup_tipo where codigo = 'EFFECT_TYPE'),
		'EFFECT_NEGATIVE',
		'Efecto negativo');
