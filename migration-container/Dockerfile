FROM flyway/flyway:9.15

COPY migration-service/src/main/resources/db/migration/* /flyway/sql
COPY migration-container/drivers/postgresql-driver.jar /flyway/drivers
COPY migration-container/config/flyway.conf /flyway/conf
