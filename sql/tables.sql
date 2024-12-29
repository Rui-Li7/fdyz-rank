create table class
(
    id      bigint unsigned              not null
        primary key,
    grade   enum ('ONE', 'TWO', 'THREE') not null,
    `order` tinyint                      not null,
    constraint class_pk_2
        unique (id)
);

create table permission
(
    user_id    bigint unsigned not null,
    permission varchar(15)     not null
);

create table score
(
    user_id    bigint unsigned not null,
    teacher_id bigint unsigned not null,
    score      tinyint         not null
);

create table teacher
(
    id        bigint unsigned not null
        primary key,
    global_id bigint unsigned not null,
    is_male   tinyint(1)      not null,
    name      varchar(10)     not null,
    subject   varchar(10)     not null,
    class_id  bigint unsigned null,
    score     tinyint         not null,
    constraint teacher_pk_2
        unique (id)
);

create table user
(
    id       bigint unsigned not null
        primary key,
    nickname varchar(10)     not null,
    role     varchar(10)     not null,
    constraint user_pk_2
        unique (id)
);

