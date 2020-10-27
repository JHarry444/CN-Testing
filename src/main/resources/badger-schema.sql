-- Remove table
DROP TABLE IF EXISTS `badger` CASCADE;

-- Recreate it
CREATE TABLE `badger` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `age` INTEGER NOT NULL,
    `habitat` VARCHAR(255),
    name VARCHAR(255)
);

-- Remember to set spring.jpa.show-sql=true in application.properties if you want an easy way to nab the correct SQL