-- brand table
INSERT INTO brand
VALUES (2, 'Brand X'),
       (3, 'Brand Y');

-- user table (users authenticated by "pass)
INSERT INTO `user` (`id`, `username`, `password_hash`, `role`, `brand_id`)
VALUES (2, 'john', '$2a$10$zrJYYEgM/P81c9YXFBcBvuFVQvjPpjbrfdfac92UqNh0.AI2ptrsq', 'ROLE_USER', 2),
       (3, 'george', '$2a$10$zrJYYEgM/P81c9YXFBcBvuFVQvjPpjbrfdfac92UqNh0.AI2ptrsq', 'ROLE_USER', 2);

-- product table
INSERT INTO product (`id`, `name`, `price`, `payment_interval`, `term_length`, `brand_id`)
VALUES (1, 'Hosting package', 9.99, 2592000000000000,
        X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770d0e00000002000000000000000078', 2),
       (2, 'Premium hosting package', 14.99, 2592000000000000,
        X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770d0e00000002000000000000000078', 2),
       (3, 'VPS', 7.99, 604800000000000,
        X'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770d0e00000001000000000000000078', 3);