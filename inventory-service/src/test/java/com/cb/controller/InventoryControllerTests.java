package com.cb.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
class InventoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getItemReturnsDummyItem() throws Exception {
        mockMvc.perform(get("/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Dummy Item"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void getItemWithEmptyIdReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/inventory/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemWithNonNumericIdReturnsDummyItem() throws Exception {
        mockMvc.perform(get("/inventory/abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc"))
                .andExpect(jsonPath("$.name").value("Dummy Item"))
                .andExpect(jsonPath("$.quantity").value(10));
    }
}