#!/usr/bin/env bash

set -e

export PGPASSWORD=postgres
psql -U postgres -d holder << EOF
CREATE SCHEMA staged AUTHORIZATION program;
GRANT ALL PRIVILEGES ON SCHEMA public TO program;
EOF
