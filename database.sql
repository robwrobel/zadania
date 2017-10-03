/* Database structure*/

CREATE DATABASE `zadania` DEFAULT CHARACTER SET utf8 COLLATE utf8_polish_ci;

CREATE TABLE `zadania`.`pracownik` ( `id` INT NOT NULL AUTO_INCREMENT , `imie` VARCHAR(30) NOT NULL ,
 `nazwisko` VARCHAR(30) NOT NULL , `numer_telefonu` VARCHAR(30) NULL , `login` VARCHAR(30) NOT NULL ,
 `haslo` VARCHAR(30) NOT NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;
 
CREATE TABLE `zadania`.`zadanie` ( `id` INT NOT NULL AUTO_INCREMENT , `data_stworzenia` DATETIME NOT NULL ,
 `data_wykonania` DATETIME NULL , `id_pracownik` INT NULL , `id_status` INT NOT NULL , PRIMARY KEY (`id`))
 ENGINE = InnoDB;
 
CREATE TABLE `zadania`.`status` ( `id` INT NOT NULL AUTO_INCREMENT , `nazwa` VARCHAR(30) NOT NULL , 
 PRIMARY KEY (`id`)) ENGINE = InnoDB;
 
ALTER TABLE `zadania`.`zadanie` ADD CONSTRAINT fk_id_pracownik FOREIGN KEY (id_pracownik) REFERENCES `zadania`.`pracownik`(id);
ALTER TABLE `zadania`.`zadanie` ADD CONSTRAINT fk_id_status FOREIGN KEY (id_status) REFERENCES `zadania`.`status`(id);
ALTER TABLE `zadania`.`pracownik` ADD UNIQUE( `login`);

CREATE VIEW `lista_zadan` AS SELECT z.id 'ID',z.data_stworzenia 'Data stworzenia',
z.data_wykonania 'Data wykonania', p.login 'Przypisane do',s.nazwa 'Status' 
FROM `zadanie` z LEFT JOIN `pracownik` p ON z.id_pracownik=p.id LEFT JOIN `status` s ON z.id_status=s.id

/*Database initialization with simple data*/ 
INSERT INTO `pracownik` (`id`, `imie`, `nazwisko`, `numer_telefonu`, `login`, `haslo`) 
                  VALUES (NULL, 'Robert', 'Wróbel', '+48444444444', 'robert', 'robert');
INSERT INTO `status` (`id`, `nazwa`) VALUES (NULL, 'Utworzone');
INSERT INTO `status` (`id`, `nazwa`) VALUES (NULL, 'Przypisane');
INSERT INTO `status` (`id`, `nazwa`) VALUES (NULL, 'Zakończone');

INSERT INTO `zadanie` (`id`, `data_stworzenia`, `data_wykonania`, `id_pracownik`, `id_status`) VALUES (NULL, '2016-12-13 00:00:00', NULL, NULL, '1');
INSERT INTO `zadanie` (`id`, `data_stworzenia`, `data_wykonania`, `id_pracownik`, `id_status`) VALUES (NULL, '2016-12-12 00:00:00', NULL, '1', '2');
INSERT INTO `zadanie` (`id`, `data_stworzenia`, `data_wykonania`, `id_pracownik`, `id_status`) VALUES (NULL, '2016-12-14 00:00:00', '2016-12-15 00:00:00', NULL, '3');

​				  

