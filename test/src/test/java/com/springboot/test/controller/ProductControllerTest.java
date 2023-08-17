package com.springboot.test.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.springboot.test.data.dto.ProductDto;
import com.springboot.test.data.dto.ProductResponseDto;
import com.springboot.test.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
//웹에서 사용되는 요청과 응답에 대한 테스트 수행 가능함
// 대상 클래스만 로드해 테스트 수행 한다.
// 만약 대상 클래스를 추가하지 않으며, @controller, @RestController, 등의 컨트롤러 관련 빈 객체가 다 로드됨
//WebMvcTest 는 슬라이드 테스트로써, 각 레이어 별로 나누어 테스트를 진행한다는 의미이다.
//컨트롤러 레이어는 웹과 맞닿아 있어서 슬라이스 테스트를 주로 진행을 한다.


class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ProductController에서 잡고 있는 Bean 객체에 대해 Mock 형태의 객체를 생성해줌
    @MockBean //Mock (가짜) : 실제 객체가 아니기 때문에 실제 행위를 수행하지 않는다.
    ProductServiceImpl productService;

    // 예제 7.6
    // http://localhost:8080/api/v1/product-api/product/{productId}
    @Test
    @DisplayName("MockMvc를 통한 Product 데이터 가져오기 테스트")
    void getProductTest() throws Exception {

        // given : Mock 객체가 특정 상황에서 해야하는 행위를 정의하는 메소드
        given(productService.getProduct(123L)).willReturn(
                new ProductResponseDto(123L, "pen", 5000, 2000));

        String productId = "123";

        // andExpect : 기대하는 값이 나왔는지 체크해볼 수 있는 메소드
        mockMvc.perform(//perform을 사용하면 서버로 url 요청을 보내는 것처럼 통신 테스트 코드 작성 가능.
                        get("/product?number=" + productId))
                //perform 메서드의 결과 값으로 ResultActions 객체가 리턴이 된다.
                //andExpect: 해당 기능을 통하여 결과값 검증을 수행 할 수 있다.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                // json path의 depth가 깊어지면 .을 추가하여 탐색할 수 있음 (ex : $.productId.productIdName)
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());//andDo 메서드를 사용하면 요청과 응답의 전체 내용 확인 가능하다.

        // verify : 해당 객체의 메소드가 실행되었는지 체크해줌
        //verify(productService).getProduct(123L);
    }

    // 예제 7.8
    // http://localhost:8080/api/v1/product-api/product
    @Test
    @DisplayName("Product 데이터 생성 테스트")
    void createProductTest() throws Exception {
        //Mock 객체에서 특정 메소드가 실행되는 경우 실제 Return을 줄 수 없기 때문에 아래와 같이 가정 사항을 만들어줌
        //RequestBody를 인자로 받는것이 가정 되어있어서 밑의 부분과 같이 작성을 함
        given(productService.saveProduct(new ProductDto("pen", 5000, 2000)))
                .willReturn(new ProductResponseDto(12315L, "pen", 5000, 2000));

        ProductDto productDto = ProductDto.builder()
                .name("pen")
                .price(5000)
                .stock(2000)
                .build();

        Gson gson = new Gson();
        String content = gson.toJson(productDto);

        mockMvc.perform(
                        post("/product")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());

        verify(productService).saveProduct(new ProductDto("pen", 5000, 2000));
    }


}