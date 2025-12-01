-- 4 курс, 2 урок, 3 задание
-- JOIN-запросы для получения информации о студентах

-- 1. Получить информацию обо всех студентах школы Хогвартс
--    (имя, возраст студента) вместе с названиями факультетов
SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM students s
         LEFT JOIN faculty f ON s.faculty_id = f.id
ORDER BY s.name;

-- 2. Получить только тех студентов, у которых есть аватарки
SELECT
    s.name AS student_name,
    s.age AS student_age,
    a.file_path AS avatar_path,
    a.media_type AS avatar_type
FROM students s
         INNER JOIN avatars a ON s.id = a.student_id
ORDER BY s.name;