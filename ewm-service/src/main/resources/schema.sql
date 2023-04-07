CREATE TABLE IF NOT EXISTS USERS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    EMAIL CHARACTER VARYING(45) UNIQUE NOT NULL,
    NAME CHARACTER VARYING(45) NOT NULL,
    CONSTRAINT "USERS_PK" PRIMARY KEY (ID)
    );

CREATE TABLE IF NOT EXISTS CATEGORIES
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    NAME CHARACTER VARYING(45) UNIQUE NOT NULL,
    CONSTRAINT "CATEGORIES_PK" PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS LOCATION
(
    LAT FLOAT NOT NULL,
    LON FLOAT NOT NULL,
    CONSTRAINT "LOCATION_PK" PRIMARY KEY (LAT, LON)
);

CREATE TABLE IF NOT EXISTS COMPILATIONS
(
    COMPILATION_ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    PINNED BOOLEAN NOT NULL,
    TITLE CHARACTER VARYING(400) NOT NULL,
    CONSTRAINT "COMPILATIONS_PK" PRIMARY KEY (COMPILATION_ID)
);

CREATE TABLE IF NOT EXISTS EVENTS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    ANNOTATION CHARACTER VARYING(2000) NOT NULL,
    CATEGORY_ID BIGINT NOT NULL,
    CREATED_ON TIMESTAMP WITHOUT TIME ZONE,
    DESCRIPTION CHARACTER VARYING(7000),
    EVENT_DATE TIMESTAMP WITHOUT TIME ZONE,
    INITIATOR_ID BIGINT NOT NULL,
    LOT FLOAT NOT NULL,
    LON FLOAT NOT NULL,
    PAID BOOLEAN,
    PARTICIPANT_LIMIT INTEGER,
    PUBLISHED_ON TIMESTAMP WITHOUT TIME ZONE,
    REQUEST_MODERATION BOOLEAN,
    STATE CHARACTER VARYING(40) NOT NULL,
    TITLE CHARACTER VARYING(120),
    COMPILATION_ID BIGINT,
    NOT_AVAILABLE BOOLEAN,
    CONSTRAINT "EVENT_PK" PRIMARY KEY(ID),
    CONSTRAINT "CATEGORY_EVENT_FK" FOREIGN KEY(CATEGORY_ID) REFERENCES CATEGORIES,
    CONSTRAINT "USER_EVENT_FK" FOREIGN KEY(INITIATOR_ID) REFERENCES USERS,
    CONSTRAINT "COMPILATION_EVENT_FK" FOREIGN KEY(COMPILATION_ID) REFERENCES COMPILATIONS,
    CONSTRAINT LOCATION_EVENT_FK FOREIGN KEY(LOT, LON) REFERENCES LOCATION

);

CREATE TABLE IF NOT EXISTS REQUESTS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE,
    EVENT_ID  BIGINT NOT NULL,
    REQUESTER_ID BIGINT NOT NULL,
    STATUS CHARACTER VARYING(40) NOT NULL,
    CONSTRAINT "REQUEST_PK" PRIMARY KEY (ID),
    CONSTRAINT "EVENT_REQUEST_FK" FOREIGN KEY(EVENT_ID) REFERENCES EVENTS,
    CONSTRAINT "USER_REQUEST_FK" FOREIGN KEY(REQUESTER_ID) REFERENCES USERS
);

CREATE TABLE IF NOT EXISTS EVENT_COMPILATION
(
    EVENT_ID BIGINT NOT NULL,
    COMPILATION_ID BIGINT NOT NULL,
    CONSTRAINT "EVENT_COMPILATION_PK" PRIMARY KEY(EVENT_ID, COMPILATION_ID)
);
