-- file: 10-create-user-and-db.sql
CREATE DATABASE holder;
CREATE USER program WITH PASSWORD 'test';
GRANT ALL PRIVILEGES ON DATABASE holder TO program;
