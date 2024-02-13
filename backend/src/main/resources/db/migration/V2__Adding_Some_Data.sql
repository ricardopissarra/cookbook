INSERT INTO recipe ("name", createdate, updatedate) VALUES('classic ragu blognese', NOW(), NULL);

INSERT INTO steps (description, createdate, idrecipe) VALUES('PREPARE your ingredients by finely chopping your pancetta, carrot,  celery and onion.<br>HEAT a large frying pan over a medium heat and fry the pancetta for 2 to 3 minutes until crisp.<br>STIR in the onion, carrot and celery and leave to sweat for 10 mins or until soft.<br>ADD the beef to the pan, stirring occasionally until it has browned all over.<br>POUR in the red wine and keep stirring for 2 minutes until the alcohol has evaporated.<br>ADD the tomato purée and stir into the sauce, then add 500ml of boiling water from the kettle and crumble in the beef stock cubes and the bay leaves.<br>SIMMER slowly for 3 hours with the lid ON to let the flavours intensify and the beef break down.<br>SEASON with salt and pepper if necessary', NOW(), (select max(idrecipe) from recipe r));

INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('100g smoked pancetta', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('1 carrot', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('2 sticks celery', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('1kg good-quality beef mince', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('a glass red wine', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('2 beef stock cubes', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('2 bay leaves', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('1 red onion', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('140g tomato purée', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES( '500ml boiling water', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('salt & freshly ground pepper', NOW(), (select max(idrecipe) from recipe r));

INSERT INTO recipe ("name", createdate, updatedate) VALUES('spinach pesto', NOW(), NULL);

INSERT INTO steps (description, createdate, idrecipe) VALUES('Prepare your ingredients: Measure out your ingredients.<br>Place the measured almonds in the food processor, start by pulsing the food processor to break up the pine almonds. <br>Place the rest of the ingredients in the bottom of the food processor with the blade attachment.<br>Then, blend until the pesto is smooth but still gritty.', NOW(), (select max(idrecipe) from recipe r));

INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('100g parmesan cheese', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('50g almonds', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('2 garlic cloves', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('80g spinach', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('20g basil', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('200g olive oil', NOW(), (select max(idrecipe) from recipe r));
INSERT INTO ingredients (name_ingredient, createdate, idrecipe) VALUES('1/2 tbsp of salt', NOW(), (select max(idrecipe) from recipe r));