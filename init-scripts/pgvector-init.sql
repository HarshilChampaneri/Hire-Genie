CREATE DATABASE employee_recommendation_db;

GRANT ALL PRIVILEGES ON DATABASE employee_recommendation_db TO "user";
GRANT ALL PRIVILEGES ON DATABASE job_recommendation_db TO "user";

\connect job_recommendation_db
CREATE EXTENSION IF NOT EXISTS vector;

\connect employee_recommendation_db
CREATE EXTENSION IF NOT EXISTS vector;
