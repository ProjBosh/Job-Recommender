INSERT INTO users(id, first_name, experience) VALUES (gen_random_uuid(), 'alice', 3);

INSERT INTO jobs(id, job_name, company, experience) VALUES (gen_random_uuid(), 'Java Developer', 'VK', 3);

INSERT INTO skills(id, name) VALUES (1, 'java');
INSERT INTO skills(id, name) VALUES (2, 'linux');
INSERT INTO skills(id, name) VALUES (3, 'spring');