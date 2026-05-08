CREATE TABLE user_skills (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    skill_id INTEGER REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, skill_id)
);

CREATE TABLE job_skills (
    job_id UUID REFERENCES jobs(id) ON DELETE CASCADE,
    skill_id INTEGER REFERENCES skills(id) ON DELETE CASCADE,
    PRIMARY KEY (job_id, skill_id)
);

CREATE TABLE skills (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(100) NOT NULL,
    experience INTEGER DEFAULT 0 CHECK (experience >= 0)
);

CREATE TABLE jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_name VARCHAR(100) NOT NULL,
    company VARCHAR(255) NOT NULL,
    experience INTEGER DEFAULT 0 CHECK (experience >= 0)
);

-- Indexes for linking table
CREATE INDEX index_user_skills_user_id ON user_skills(user_id);
CREATE INDEX index_user_skills_skill_id ON user_skills(skill_id);
CREATE INDEX index_job_skills_job_id ON job_skills(job_id);
CREATE INDEX index_job_skills_skill_id ON job_skills(skill_id);

-- Unique index for name skills
CREATE UNIQUE INDEX index_skills_name_lower ON skills(LOWER(name));

-- The index for searching by first name user
CREATE INDEX index_users_first_name ON users(first_name);
    CREATE INDEX index_jobs_job_name ON jobs(job_name);

-- The index for searching by experience user
CREATE INDEX index_users_experience ON users(experience);
CREATE INDEX index_jobs_experience ON jobs(experience);