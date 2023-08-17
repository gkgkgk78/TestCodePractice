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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)

public class ControllerMyTest {

    @InjectMocks //@Mock 선언이 된 객체들은 @InjectMocks에 들어가게 된다
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
        when(productService.getProduct(123L))
                .thenReturn((new ProductResponseDto(123L, "hong", 13000, 456)));
        Long test = 123L;
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

        //when
        Long test = 123l;
        Gson gson = new Gson();
        String content = gson.toJson(productDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/product").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        ProductResponseDto productResponseDto1 = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), ProductResponseDto.class);

        //then
        Assertions.assertEquals(productResponseDto1.getNumber(), 123L);
        Assertions.assertEquals(productResponseDto1.getName(), "hong");
        Assertions.assertEquals(productResponseDto1.getPrice(), 13000);
        Assertions.assertEquals(productResponseDto1.getStock(), 456);

    }

//    //음 뭔가 이상하다, 컨트롤러 단에서 에러를 던져야 하는데 음...
//    @Test
//    @DisplayName("제품등록 가격 테스트")
//    void 제품등록시_가격이음수() throws Exception {
//        ProductDto productDto = new ProductDto("hong", -1, 456);
//
//        //when
//        Long test = 123l;
//        Gson gson = new Gson();
//        String content = gson.toJson(productDto);
//        ResultActions resultActions = mockMvc.perform
//                (MockMvcRequestBuilders.post("/product")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON));
//        //then
//        resultActions.andExpect(status().isBadRequest());
//
//    }


}
