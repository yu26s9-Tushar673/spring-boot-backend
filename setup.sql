-- Database setup script for Internship Platform
-- Run this to create the database and user

CREATE DATABASE IF NOT EXISTS internships;

USE internships;

-- Optional: Insert sample data (uncomment to use)
-- Note: Run this AFTER starting the app once so tables are created

-- INSERT INTO internships (title, company, description, location, published) VALUES
-- ('Java Developer Intern', 'Tech Corp', 'Build amazing Spring Boot applications', 'Amsterdam', true),
-- ('Frontend Developer Intern', 'WebDev BV', 'Create responsive React applications', 'Rotterdam', true),
-- ('Data Analyst Intern', 'Data Solutions', 'Analyze datasets and create visualizations', 'Utrecht', true),
-- ('DevOps Intern', 'Cloud Systems', 'Learn CI/CD and cloud infrastructure', 'Eindhoven', false);

-- INSERT INTO candidates (name, email, field_of_study) VALUES
-- ('Jan de Vries', 'jan@example.com', 'Computer Science'),
-- ('Emma Jansen', 'emma@example.com', 'Data Science'),
-- ('Lucas van Dam', 'lucas@example.com', 'Software Engineering');
