CREATE DATABASE IF NOT EXISTS `products_db`
DEFAULT CHARACTER SET = 'utf8mb4'
COLLATE = 'utf8mb4_unicode_ci';

USE `products_db`;

CREATE TABLE IF NOT EXISTS `fixed_term_deposit` (
                                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                    `account_id` BIGINT NOT NULL,
                                                    `amount` NUMERIC(15, 2) NOT NULL,
    `term_in_days` INT NOT NULL,
    `annual_interest_rate` NUMERIC(5, 2) NOT NULL,
    `expected_return` NUMERIC(15, 2) DEFAULT 0,
    `start_date` DATE NOT NULL,
    `maturity_date` DATE NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` DATETIME NULL,
    `updated_at` DATETIME NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_fixed_term_account` (`account_id`),
    INDEX `idx_fixed_term_status` (`status`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `fixed_term_deposit`
(`account_id`, `amount`, `term_in_days`, `annual_interest_rate`, `expected_return`,
 `start_date`, `maturity_date`, `status`, `active`, `created_at`, `updated_at`)
VALUES
    (1, 100000.00, 30, 32.50, 102671.23, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'ACTIVE', TRUE, NOW(), NOW()),
    (2, 250000.00, 60, 35.00, 264383.56, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 60 DAY), 'ACTIVE', TRUE, NOW(), NOW()),
    (3, 50000.00, 90, 30.00, 53698.63, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 90 DAY), 'ACTIVE', TRUE, NOW(), NOW());