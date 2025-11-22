-- Create databases for each service
CREATE DATABASE IF NOT EXISTS usersdb;
CREATE DATABASE IF NOT EXISTS patientsdb;
CREATE DATABASE IF NOT EXISTS doctorsdb;
CREATE DATABASE IF NOT EXISTS ehrdb;

-- Create admin user if not exists
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY '123456';

-- Grant privileges
GRANT ALL PRIVILEGES ON usersdb.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON patientsdb.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON doctorsdb.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON doctorsdb.* TO 'admin'@'%';
GRANT ALL PRIVILEGES ON ehrdb.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON ehrdb.* TO 'admin'@'%';

FLUSH PRIVILEGES;



