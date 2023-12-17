CREATE DATABASE cookbook;

CREATE TABLE cookbook.RECIPE(
    IDRECIPE BIGSERIAL PRIMARY KEY,
    NAME TEXT NOT NULL,
    CREATEDATE timestamp not null,
    UPDATEDATE timestamp NULL
);

CREATE TABLE cookbook.ingredients (
	idingredient bigserial NOT NULL,
	name_ingredient text NOT NULL,
	createdate timestamp NOT NULL,
	idrecipe bigserial NOT NULL,
	CONSTRAINT ingredients_pkey PRIMARY KEY (idingredient),
	CONSTRAINT ingredients_fk FOREIGN KEY (idrecipe) REFERENCES cookbook.recipe(idrecipe)
);

CREATE TABLE cookbook.steps (
	idstep bigserial NOT NULL,
	description text NOT NULL,
	createdate timestamp NOT NULL,
	idrecipe bigserial NOT NULL,
	CONSTRAINT steps_pkey PRIMARY KEY (idstep),
	CONSTRAINT steps_fk FOREIGN KEY (idrecipe) REFERENCES cookbook.recipe(idrecipe)
);