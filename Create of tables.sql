-- CREATE TABLE user_skills (
--     user_id UUID REFERENCES users(id) ON DELETE CASCADE,
--     skill_id INTEGER REFERENCES skills(id) ON DELETE CASCADE,
--     PRIMARY KEY (user_id, skill_id)
-- );

-- CREATE TABLE job_skills (
--     job_id UUID REFERENCES jobs(id) ON DELETE CASCADE,
--     skill_id INTEGER REFERENCES skills(id) ON DELETE CASCADE,
--     PRIMARY KEY (job_id, skill_id)
-- );

-- CREATE TABLE skills (
--     id SERIAL PRIMARY KEY,
--     name VARCHAR(100) UNIQUE NOT NULL
-- );

-- CREATE TABLE users (
--     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--     first_name VARCHAR(100) NOT NULL,
--     experience INTEGER DEFAULT 0 CHECK (experience >= 0)
-- );

-- CREATE TABLE jobs (
--     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--     job_name VARCHAR(100) NOT NULL,
--     company VARCHAR(255) NOT NULL,
--     experience INTEGER DEFAULT 0 CHECK (experience >= 0)
-- );