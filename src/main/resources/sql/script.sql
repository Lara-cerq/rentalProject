use rental;
drop table users;
drop table rentals;
drop table messages;
CREATE TABLE if not exists `USERS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255),
  `name` varchar(255),
  `password` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp
);
CREATE TABLE if not exists `RENTALS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `surface` numeric,
  `price` numeric,
  `picture` varchar(255),
  `description` varchar(2000),
  `owner_id` integer NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE if not exists `MESSAGES` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `rental_id` integer,
  `user_id` integer,
  `message` varchar(2000),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);

ALTER TABLE `USERS` ADD constraint owner_id FOREIGN KEY (`id`) REFERENCES `RENTALS` (`id`);

ALTER TABLE `USERS` ADD constraint user_id FOREIGN KEY (`id`) REFERENCES `MESSAGES` (`id`);

ALTER TABLE `RENTALS` ADD constraint rental_id FOREIGN KEY (`id`) REFERENCES `MESSAGES` (`id`);

insert into `USERS` values (1,'ra@gmail.com', 'Lara', 'lara',"2023-11_21", "2023-11_21");

insert into `RENTALS` values (1,"appart",45, 520, "png", "2pieces vue mer", 1, "2023-11_21", "2023-11_21");

insert into`MESSAGES` values (1, 1, 1, "message", "2023-11_21", "2023-11_21");