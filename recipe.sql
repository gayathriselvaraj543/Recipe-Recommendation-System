DROP TABLE IF EXISTS RecipeIngredient;
DROP TABLE IF EXISTS Ingredient;
DROP TABLE IF EXISTS Recipe;

CREATE TABLE Recipe (
    recipe_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    instructions TEXT
);

CREATE TABLE Ingredient (
    ingredient_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE RecipeIngredient (
    recipe_id INT,
    ingredient_id INT,
    quantity DOUBLE,
    FOREIGN KEY (recipe_id) REFERENCES Recipe(recipe_id),
    FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id)
);


INSERT INTO Recipe (name, instructions) VALUES 
('Pasta', '1. Boil water in a large pot.\n2. Add pasta and cook until tender.\n3. Drain and set aside.\n4. In a saucepan, heat the tomato sauce with garlic and basil.\n5. Combine pasta with sauce, and mix well before serving.'),
('Salad', '1. Chop the lettuce and place it in a large bowl.\n2. Add diced tomatoes, cucumber, and cheese.\n3. Toss with olive oil, salt, and pepper.\n4. Serve fresh, topped with optional croutons.'),
('Grilled Cheese Sandwich', '1. Spread butter on one side of each bread slice.\n2. Place cheese slices between two slices of bread (butter side out).\n3. Heat a skillet and place the sandwich on it.\n4. Grill each side until golden brown and cheese is melted.'),
('Omelette', '1. Crack eggs into a bowl and whisk until fluffy.\n2. Add a pinch of salt and pepper.\n3. Heat a pan with a bit of oil or butter.\n4. Pour the eggs into the pan and add diced tomatoes and cheese.\n5. Fold the omelette and serve hot.'),
('Fruit Smoothie', '1. Add yogurt, banana, and mixed berries to a blender.\n2. Pour in orange juice and honey.\n3. Blend until smooth and creamy.\n4. Pour into a glass and enjoy as a refreshing drink.');


INSERT INTO Ingredient (name) VALUES 
('Pasta'), 
('Tomato'), 
('Lettuce'), 
('Cheese'), 
('Bread'), 
('Butter'), 
('Eggs'), 
('Cucumber'), 
('Olive Oil'), 
('Banana'), 
('Yogurt'), 
('Mixed Berries'), 
('Orange Juice'), 
('Honey');

-- Insert Recipe-Ingredient relationships with quantities
INSERT INTO RecipeIngredient (recipe_id, ingredient_id, quantity) VALUES 

(1, 1, 200),   
(1, 2, 50),
(1, 9, 10),


(2, 3, 100),  
(2, 2, 50),    
(2, 4, 30),    
(2, 8, 30),
(2, 9, 5),


(3, 5, 2),
(3, 4, 50),
(3, 6, 10),    


(4, 7, 2),    
(4, 2, 30),    
(4, 4, 20),


(5, 10, 1),
(5, 11, 100),  
(5, 12, 50),   
(5, 13, 150),  
(5, 14, 10);


SELECT DISTINCT r.recipe_id, r.name, r.instructions
FROM Recipe r
JOIN RecipeIngredient ri ON r.recipe_id = ri.recipe_id
JOIN Ingredient i ON ri.ingredient_id = i.ingredient_id
WHERE i.name IN ('Pasta', 'Tomato')
GROUP BY r.recipe_id;
select*from recipe;
select*from RecipeIngredient;
select*from Ingredient;