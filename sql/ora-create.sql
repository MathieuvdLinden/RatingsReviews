create table rvw_review (
  id                      varchar2(255) not null,
  rating                  number(10),
  is_rating_only          number(1),
  product_id              varchar2(255),
  campaign_id             varchar2(255),
  submission_time         timestamp,
  rating_range            number(10),
  author_id               varchar2(255),
  user_nickname           varchar2(255),
  title                   varchar2(255),
  review_text             varchar2(4000),
  mod_status              number(10),
  last_moderated_time     timestamp,
  last_modification_time  timestamp not null,
  constraint ck_rvw_review_mod_status check (mod_status in (0,1,2)),
  constraint pk_rvw_review primary key (id))
;

create table rvw_response (
  id                      varchar2(255) not null,
  response_name           varchar2(255),
  response_source         varchar2(255),
  response_type           varchar2(255),
  response_date           timestamp,
  response                varchar2(255),
  department              varchar2(255),
  review_id               varchar2(255),
  last_modification_time  timestamp not null,
  constraint pk_rvw_response primary key (id))
;


create sequence rvw_review_seq;
