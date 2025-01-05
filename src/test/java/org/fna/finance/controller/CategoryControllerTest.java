package org.fna.finance.controller;

import org.fna.finance.dto.CategoryResponse;
import org.fna.finance.exception.CategoryNotFoundException;
import org.fna.finance.mapper.CategoryMapper;
import org.fna.finance.model.Category;
import org.fna.finance.model.User;
import org.fna.finance.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    public void getAllCategories_Success() throws Exception {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Test Category", new User()));
        categories.add(new Category(2L, "Test Category 2", new User()));

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        categoryResponses.add(new CategoryResponse(1L, "Test Category"));
        categoryResponses.add(new CategoryResponse(2L, "Test Category 2"));

        when(categoryService.getAllCategories(any())).thenReturn(categories);
        when(categoryMapper.categoriesToCategoriesResponse(any())).thenReturn(categoryResponses);

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Category"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Test Category 2"));
    }

    @Test
    @WithMockUser
    public void createCategory_Success() throws Exception {
        Category category = new Category(1L, "Test Category", new User());
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Test Category");

        when(categoryMapper.categoryRequestToCategory(any())).thenReturn(category);
        when(categoryService.createCategory(any())).thenReturn(category);
        when(categoryMapper.categoryToCategoryResponse(any())).thenReturn(categoryResponse);

        mockMvc.perform(post("/category")
                .contentType("application/json")
                .content("{\"name\": \"Test Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    @WithMockUser
    public void createCategory_BadRequest() throws Exception {
        mockMvc.perform(post("/category")
                .contentType("application/json")
                .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void getCategory_Success() throws Exception {
        Category category = new Category(1L, "Test Category", new User());
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Test Category");

        when(categoryService.getCategory(any(), anyLong())).thenReturn(category);
        when(categoryMapper.categoryToCategoryResponse(any())).thenReturn(categoryResponse);

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    @WithMockUser
    public void getCategory_CategoryNotFoundException() throws Exception {
        when(categoryService.getCategory(any(), anyLong())).thenThrow(new CategoryNotFoundException(1L));

        mockMvc.perform(get("/category/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void updateCategoryName_Success() throws Exception {
        Category category = new Category(1L, "Test Category", new User());
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Test Category");

        when(categoryService.updateCategoryName(any(), anyLong(), anyString())).thenReturn(category);
        when(categoryMapper.categoryToCategoryResponse(any())).thenReturn(categoryResponse);

        mockMvc.perform(put("/category/1")
                .contentType("application/json")
                .content("{\"name\": \"Test Category\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    @WithMockUser
    public void updateCategoryName_CategoryNotFoundException() throws Exception {
        doThrow(new CategoryNotFoundException(1L)).when(categoryService).updateCategoryName(any(), anyLong(), anyString());

        mockMvc.perform(put("/category/1")
                .contentType("application/json")
                .content("{\"name\": \"Test Category\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void updateCategoryName_BadException() throws Exception {
        mockMvc.perform(put("/category/1")
                .contentType("application/json")
                .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void deleteCategory_Success() throws Exception {
        mockMvc.perform(delete("/category/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void deleteCategory_CategoryNotFoundException() throws Exception {
        doThrow(new CategoryNotFoundException(1L)).when(categoryService).deleteCategory(any(), anyLong());

        mockMvc.perform(delete("/category/1"))
                .andExpect(status().isNotFound());
    }
}
