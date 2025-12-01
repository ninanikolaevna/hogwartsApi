-- 4 курс, 2 урок, 1 задание
-- Ограничения для таблиц Student и Faculty

-- 1. Возраст студента не может быть меньше 16 лет
ALTER TABLE students
    ADD CONSTRAINT age_check CHECK (age >= 16);

-- 2. Имена студентов должны быть уникальными и не равны нулю
ALTER TABLE students
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE students
    ADD CONSTRAINT unique_name UNIQUE (name);

-- 3. Пара "значение названия" - "цвет факультета" должна быть уникальной
ALTER TABLE faculty
    ADD CONSTRAINT unique_name_color UNIQUE (name, color);

-- 4. При создании студента без возраста ему автоматически должно присваиваться 20 лет
ALTER TABLE students
    ALTER COLUMN age SET DEFAULT 20;