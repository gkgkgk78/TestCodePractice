package com.springboot.test.controller;


import com.google.gson.Gson;
import com.springboot.test.data.dto.ProductDto;
import com.springboot.test.data.dto.ProductResponseDto;
import com.springboot.test.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerLayerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    private void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("제품조회 정상 테스트")
    void getProduct() throws Exception {

        //given
        when(productService.getProduct(123L)).thenReturn((new ProductResponseDto(123L, "hong", 13000, 456)));

        Long test = 123l;
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/product?number=" + test))
                .andExpect(status().isOk()).andReturn();
        ProductResponseDto productResponseDto1 = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ProductResponseDto.class);

        Assertions.assertEquals(productResponseDto1.getNumber(), 123L);
        Assertions.assertEquals(productResponseDto1.getName(), "hong");
        Assertions.assertEquals(productResponseDto1.getPrice(), 13000);
        Assertions.assertEquals(productResponseDto1.getStock(), 456);

    }


    @Test
    @DisplayName("제품등록 정상 테스트")
    void saveProduct() throws Exception {

        ProductDto productDto = new ProductDto("hong", 13000, 456);
        ProductResponseDto productResponseDto = new ProductResponseDto(123L, "hong", 13000, 456);
        //given
        when(productService.saveProduct(productDto)).thenReturn(productResponseDto);

        Long test = 123l;
        Gson gson = new Gson();
        String content = gson.toJson(productDto);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/product").content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        ProductResponseDto productResponseDto1 = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ProductResponseDto.class);

        Assertions.assertEquals(productResponseDto1.getNumber(), 123L);
        Assertions.assertEquals(productResponseDto1.getName(), "hong");
        Assertions.assertEquals(productResponseDto1.getPrice(), 13000);
        Assertions.assertEquals(productResponseDto1.getStock(), 456);

    }



}
