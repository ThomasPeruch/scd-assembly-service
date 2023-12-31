CREATE TABLE if not exists public.topic (
	id bigserial NOT NULL,
	name varchar(200) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE if not exists public.session_voting(
	id bigserial not null,
	topic_id bigserial not null,
	voting_start timestamp not null,
	voting_end timestamp not null,
    is_finished boolean,
	primary key (id),
	foreign key (topic_id) references topic(id)
);
