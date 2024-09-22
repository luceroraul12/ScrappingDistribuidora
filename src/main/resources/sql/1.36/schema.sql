
--SCHEMA
-- Creo tabla para etiquetas
create table label(
	id serial primary key,
	date_created timestamp not null default now(),
	name varchar(500) not null,
	description varchar(500),
	client_id int not null,
	constraint label_client_fk foreign key(client_id) references client(id)
);

-- Creo tabla para efectos positivos y negativos
create table product_effect(
	id serial primary key,
	lv_type_id int not null,
	label_id int null,
	product_id int not null,
	description varchar(2000) not null,
	constraint pe_type_fk foreign key(lv_type_id) references lookup_valor(id),
	constraint pe_label_fk foreign key(label_id) references label(id),
	constraint pe_product_fk foreign key(product_id) references productos_internos(id)
);

