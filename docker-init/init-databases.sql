-- Create databases for each service
CREATE DATABASE IF NOT EXISTS usersdb;
CREATE DATABASE IF NOT EXISTS patientsdb;

-- Grant privileges
GRANT ALL PRIVILEGES ON usersdb.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON patientsdb.* TO 'root'@'%';

FLUSH PRIVILEGES;



