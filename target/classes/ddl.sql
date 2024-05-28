CREATE TABLE IF NOT EXISTS article (
    id              SERIAL PRIMARY KEY NOT NULL,
    title           VARCHAR (1024) NOT NULL,
    origin_content  TEXT,
    html_content TEXT,
    author          VARCHAR (100) DEFAULT NULL,
    directory_id    INT DEFAULT NULL,
    update_time     TIMESTAMP NULL DEFAULT NULL,
    create_time     TIMESTAMP NULL DEFAULT NULL,
    user_id         INT NOT NULL,
    delete_flag     SMALLINT NOT NULL DEFAULT 0,
    keyword         VARCHAR (1024) DEFAULT NULL,
    description     VARCHAR (1024) DEFAULT NULL,
    absolute_path    VARCHAR (256) DEFAULT NULL,
    search_content  TSVECTOR GENERATED ALWAYS AS (
        SETWEIGHT (
            TO_TSVECTOR ('chinese_zh', title),
            'A'
        ) || SETWEIGHT (
            TO_TSVECTOR ('chinese_zh', origin_content),
            'B'
        )
    ) STORED
);
CREATE INDEX IF NOT EXISTS article_search_idx ON article USING GIN (search_content);

CREATE TABLE if not exists directory
(
    id            SERIAL   NOT NULL PRIMARY KEY  ,
    parent_id     INT                    DEFAULT NULL,
    name          VARCHAR(1024) NOT NULL ,
    description   VARCHAR(1024)          DEFAULT NULL ,
    remark        VARCHAR(1024)          DEFAULT NULL ,
    sort_num      BIGINT        NOT NULL DEFAULT 0,
    create_time   TIMESTAMP     NULL     DEFAULT NULL,
    update_time   TIMESTAMP     NULL     DEFAULT NULL,
    absolute_path  VARCHAR (256) DEFAULT NULL
);


