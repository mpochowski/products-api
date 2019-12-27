-- there is always go daddy brand
INSERT INTO brand
VALUES (1, 'GoDaddy');

-- there is always "godaddy" admin user authenticated by "pass"
INSERT INTO `user` (`id`, `username`, `password_hash`, `role`, `brand_id`)
VALUES (1, 'godaddy', '$2a$10$zrJYYEgM/P81c9YXFBcBvuFVQvjPpjbrfdfac92UqNh0.AI2ptrsq', 'ROLE_ADMIN', 1);