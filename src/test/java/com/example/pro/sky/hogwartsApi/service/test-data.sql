-- test-data.sql
INSERT INTO faculty (id, name, color) VALUES
                                          (1, 'Gryffindor', 'Red'),
                                          (2, 'Slytherin', 'Green');

INSERT INTO student (id, name, age, faculty_id) VALUES
                                                    (1, 'Harry Potter', 11, 1),
                                                    (2, 'Hermione Granger', 11, 1),
                                                    (3, 'Ron Weasley', 11, 1),
                                                    (4, 'Draco Malfoy', 11, 2),
                                                    (5, 'Neville Longbottom', 11, 1),
                                                    (6, 'Luna Lovegood', 11, 1);