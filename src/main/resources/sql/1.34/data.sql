INSERT INTO public.datos_distribuidora 
    (active, 
    lv_distribuidora_id,
    fecha_actualizacion,
    "size",
    web, 
    excel, 
    web_url, 
    has_paginator)
VALUES
    (true, 1, '2024-05-12 19:45:27.174', 0, true, false, 'http://gglobal.net.ar/bernal/?cliente', false),
    (true, 3, '2024-05-12 19:45:27.174', 0, true, false, 'https://pidorapido.com/dongasparsj', false),
    (true, 5, '2024-05-13 08:06:27.306', 0, true, false, 'https://lagranjadelcentro.com.ar/productos.php?pagina=', true),
    (true, 2, '2024-05-13 08:55:57.125', 0, false, true, '', false),
    (true, 6, NULL, 0, false, true, '', false);


-- Le asigno todos los productos actuales a Pasionaria
update productos_internos 
	set client_id = 1

-- elimino los external producto que no fueron vinculados
delete from external_product  
where id not in (
	select ep.id from external_product ep 
		inner join  productos_internos p on p.external_product_id = ep.id
);

