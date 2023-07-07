CREATE TABLE if not exists public.topic (
	id bigserial NOT NULL,
	name varchar(200) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE if not exists public.session_voting(
	id bigserial not null,
	topic_id integer not null,
	voting_start timestamp not null,
	voting_end timestamp not null,
    result boolean,
	primary key (id),
	foreign key (topic_id) references topic(id)
);
