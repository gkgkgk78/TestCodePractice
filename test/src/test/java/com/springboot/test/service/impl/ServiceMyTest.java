package com.springboot.test.service.impl;

import com.springboot.test.data.dto.ProductDto;
import com.springboot.test.data.dto.ProductResponseDto;
import com.springboot.test.data.entity.Product;
import com.springboot.test.data.repository.ProductRepository;
import com.springboot.test.exception.BaseException;
import com.springboot.test.exception.ErrorMessage;
import com.springboot.test.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceMyTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {//이렇게 하면 spring에 bean을 등록시키지 않고 가짜 객체를 만들어 주입 가능 하다.
        productService = new ProductServiceImpl(productRepository);
    }

    @DisplayName("제품 저장")
    @Test
    void save() {
        //given
        when(productRepository.save(any(Product.class))).then(returnsFirstArg());

        // when
        ProductResponseDto productResponseDto = productService.saveProduct(
                new ProductDto("펜", 1000, 1234));

        // then
        Assertions.assertEquals(productResponseDto.getName(), "펜");
        Assertions.assertEquals(productResponseDto.getPrice(), 1000);
        Assertions.assertEquals(productResponseDto.getStock(), 1234);

        verify(productRepository).save(any());
    }


    @DisplayName("제품 저장시 price 값이 음수일 경우 제대로 처리 하는지")
    @Test
    void save_price값_음수일경우_에러처리() {
        //given
        Product product = new Product();
        product.setName("홍길동");
        product.setPrice(-1);
        product.setStock(13000);
//        when(productRepository.save(any(Product.class))).thenReturn(product); =>여기선 얘가 필요가 없다 왜냐하면, REPOSITORY 계층을 접근하지 않으니
        

        //.thenReturn(Optional.empty());

        // when
        BaseException baseException = assertThrows(BaseException.class, () -> productService.saveProduct(new ProductDto("홍길동", -1, 13000)));

        // then
        assertThat(baseException.getErrorMessage()).isEqualTo(ErrorMessage.NOT_ALLOW_PARAMETER.getErrMsg());

    }

    @DisplayName("제품 제품조회시_없는_번호일시")
    @Test
    void get_product_제품조회시_없는_번호일시() {
        //given
        when(productRepository.findById(-10l)).thenReturn(Optional.empty());//당연히 조회 하는건데 없는거라서 저렇게 return이 될거임

        //.thenReturn(Optional.empty());
        // when
        BaseException baseException = assertThrows(BaseException.class, () -> productService.getProduct(-10l));

        // then
        assertThat(baseException.getErrorMessage()).isEqualTo(ErrorMessage.NOT_EXIST_PHOTO.getErrMsg());
    }


}
