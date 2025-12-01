-- 4 курс, 2 урок, 2 задание
-- Создание таблиц для людей и машин

-- Создание таблицы "машина" (car)
CREATE TABLE car (
                     id BIGSERIAL PRIMARY KEY,
                     brand VARCHAR(100) NOT NULL,
                     model VARCHAR(100) NOT NULL,
                     price DECIMAL(10, 2) NOT NULL CHECK (price > 0)
);

-- Создание таблицы "человек" (person)
CREATE TABLE person (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        age INTEGER NOT NULL CHECK (age >= 0),
                        has_license BOOLEAN NOT NULL DEFAULT FALSE,
                        car_id BIGINT,
                        CONSTRAINT fk_person_car FOREIGN KEY (car_id)
                            REFERENCES car(id) ON DELETE SET NULL
);
