# etl-stream-marco

## System Requirements
- Docker
- Docker Compose

## How the system works
- This is an ETL streaming processing system.
- The input is a series of events with 20ms delay between each subsequent event. Each event is a number.
- The streaming part listens for these events and transform them to a new data.
  + if input is 12345
  + then it is used for the 'id' part in the output stream: {"id":12345,"product":"foo","quantity":100,"price":50}
- After that, the data from the output stream is sent to a sink connect to a SQLite Database. Data is written to a table called 'orders'.

## How to run
    $ cd etl-stream-marco/                # go to the main directory
    $ docker-compose down                 # stop the previous run if exists
    $ docker-compose up -d                # start a new run
    $ docker-compose logs -f              # check log
    $ ./get_db.sh                         # copy the output sqlite database from the container to the host
    $ sqlite3 test.db                     # check the 'orders' table
    sqlite> select * from orders;
