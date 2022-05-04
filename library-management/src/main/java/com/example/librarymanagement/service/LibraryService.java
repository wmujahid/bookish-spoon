package com.example.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LibraryService {

    private CategoryRepository categoryRepository;
    private RecipeRepository recipeRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Category> getCategories() {
        System.out.println("service calling getCategories ==>");
        return categoryRepository.findAll();
    }

    public Optional getCategory(Long categoryId) {
        System.out.println("service getCategory ==>");
        Optional category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return category;
        } else {
            throw new InformationNotFoundException("category with id " + categoryId + " not found");
        }
    }

    public Category createCategory(Category categoryObject) {
        System.out.println("service calling createCategory ==>");
        Category category = categoryRepository.findByName(categoryObject.getName());
        if (category != null) {
            throw new InformationExistException("category with name " + category.getName() + " already exists");
        } else {
            return categoryRepository.save(categoryObject);
        }
    }

    public Category updateCategory(Long categoryId, Category categoryObject) {
        System.out.println("service calling updateCategory ==>");
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            if (categoryObject.getName().equals(category.get().getName())) {
                throw new InformationExistException("category " + category.get().getName() + " is already exists");
            } else {
                Category updateCategory = categoryRepository.findById(categoryId).get();
                updateCategory.setName(categoryObject.getName());
                updateCategory.setDescription(categoryObject.getDescription());
                return categoryRepository.save(updateCategory);
            }
        } else {
            throw new InformationNotFoundException("category with id " + categoryId + " not found");
        }
    }

    public Optional<Category> deleteCategory(Long categoryId) {
        System.out.println("service calling deleteCategory ==>");
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            categoryRepository.deleteById(categoryId);
            return category;
        } else {
            throw new InformationNotFoundException("category with id " + categoryId + " not found");
        }
    }

    public Recipe createCategoryRecipe(Long categoryId, Recipe recipeObject) {
        System.out.println("service calling createCategoryRecipe ==>");
        try {
            Optional category = categoryRepository.findById(categoryId);
            recipeObject.setCategory((Category) category.get());
            return recipeRepository.save(recipeObject);
        } catch (NoSuchElementException e) {
            throw new InformationNotFoundException("category with id " + categoryId + " not found");
        }
    }

    public List<Recipe> getCategoryRecipes(Long categoryId) {
        System.out.println("service calling getCategoryRecipes ==>");
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return category.get().getRecipeList();
        } else {
            throw new InformationNotFoundException("category with id " + categoryId + " not found");
        }
    }

    public Recipe getCategoryRecipe(Long categoryId, Long recipeId) {
        System.out.println("service calling getCategoryRecipe ==>");
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            Optional<Recipe> recipe = recipeRepository.findByCategoryId(categoryId).stream().filter(
                    p -> p.getId().equals(recipeId)).findFirst();
            if (recipe.isEmpty()) {
                throw new InformationNotFoundException("recipe with id " + recipeId + " not found");
            } else {
                return recipe.get();
            }
        } else {
            throw new InformationNotFoundException("category with id " + categoryId + " not found");
        }
    }


    public Recipe updateCategoryRecipe(Long categoryId, Long recipeId, Recipe recipeObject) {
        System.out.println("service calling updateCategoryRecipe ==>");
        try {
            Recipe recipe = (recipeRepository.findByCategoryId(
                    categoryId).stream().filter(p -> p.getId().equals(recipeId)).findFirst()).get();
            recipe.setName(recipeObject.getName());
            recipe.setIngredients(recipeObject.getIngredients());
            recipe.setSteps(recipeObject.getSteps());
            recipe.setTime(recipeObject.getTime());
            recipe.setPortions(recipeObject.getPortions());
            return recipeRepository.save(recipe);
        } catch (NoSuchElementException e) {
            throw new InformationNotFoundException("recipe or category not found");
        }
    }

    public void deleteCategoryRecipe(Long categoryId, Long recipeId) {
        try {
            Recipe recipe = (recipeRepository.findByCategoryId(
                    categoryId).stream().filter(p -> p.getId().equals(recipeId)).findFirst()).get();
            recipeRepository.deleteById(recipe.getId());
        } catch (NoSuchElementException e) {
            throw new InformationNotFoundException("recipe or category not found");
        }
    }
}