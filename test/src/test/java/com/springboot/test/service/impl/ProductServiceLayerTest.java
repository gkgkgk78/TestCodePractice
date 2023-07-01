package com.springboot.test.service.impl;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.springboot.test.data.dto.ProductDto;
import com.springboot.test.data.dto.ProductResponseDto;
import com.springboot.test.data.entity.Product;
import com.springboot.test.data.repository.ProductRepository;
import com.springboot.test.exception.BaseException;
import com.springboot.test.exception.ErrorMessage;
import com.springboot.test.service.ProductService;
import com.springboot.test.service.ProductServiceImpl1;
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

@ExtendWith(MockitoExtension.class)
public class ProductServiceLayerTest {


    private ProductService productService;


    @Mock
    private ProductRepository productRepository;

    @InjectMocks //그냥 이렇게만 생성하려면 class 로 되어 있어야함
    //class 안에서 필요한 객체들은 @mock으로 선언이 된 객체들로 주입이 될거임
    private ProductServiceImpl1 productService1;


    @BeforeEach
    //service 객체를 만들시에 이렇게 해서 mock으로 생성이된 repository 넣을수 있음
    public void setUpTest() {

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

    @DisplayName("제품 저장1")
    @Test
    void save1() {

        //given
        when(productRepository.save(any(Product.class))).then(returnsFirstArg());


        // when
        ProductResponseDto productResponseDto = productService1.saveProduct(
                new ProductDto("펜", 1000, 1234));

        // then
        Assertions.assertEquals(productResponseDto.getName(), "펜");
        Assertions.assertEquals(productResponseDto.getPrice(), 1000);
        Assertions.assertEquals(productResponseDto.getStock(), 1234);

        verify(productRepository).save(any());

    }


//    @DisplayName("제품 저장 실패 - 이미 등록된 제품")
//    @Test
//    void save_fail() {
//
//
//        //given
//        when(productRepository.save(any(Product.class))).then(returnsFirstArg());
//
//
//        // when
//        ProductResponseDto productResponseDto = productService1.saveProduct(new ProductDto("펜", 1000, 1234));
//
//        // then
//        Assertions.assertEquals(productResponseDto.getName(), "펜");
//        Assertions.assertEquals(productResponseDto.getPrice(), 1000);
//        Assertions.assertEquals(productResponseDto.getStock(), 1234);
//
//        verify(productRepository).save(any());
//
//    }
    

    @DisplayName("제품 읽기")
    @Test
    void get() {


        //given
        Product givenProduct = new Product();
        givenProduct.setNumber(123L);
        givenProduct.setName("펜");
        givenProduct.setPrice(1000);
        givenProduct.setStock(1234);

        //주어진 data에 해당 되는 값이 주어졌을때, null 값이 아니며(optionalof) givenproduct 형태의 데이터로 리턴을 해주면 된다.
        //즉 mock 객체로 지정이 된 객체들이 동작을 하려고 할시에, 직접 db 접근을 하게 하지 않아, 독립된 환경에서의 테스트 환경 조성하며
        //mock 객체의 결과 값을 지정해주어 service layer에서 동작 여부를 테스트 하는 작업.(db접근 하지 않고 service layer 만 테스트 하기에)
        when(productRepository.findById(givenProduct.getNumber())).thenReturn(Optional.of(givenProduct));


        // when
        ProductResponseDto productResponseDto = productService1.getProduct(givenProduct.getNumber());

        // then
        Assertions.assertEquals(productResponseDto.getName(), "펜");
        Assertions.assertEquals(productResponseDto.getPrice(), 1000);
        Assertions.assertEquals(productResponseDto.getStock(), 1234);

        //verify(productRepository).findById(givenProduct.getNumber());

    }


    @DisplayName("제품 읽기 실패 - 존재하지 않는 데이터")
    @Test
    void get_error1() {

        //given
        Product givenProduct = new Product();
        givenProduct.setNumber(124L);
        givenProduct.setName("펜");
        givenProduct.setPrice(1000);
        givenProduct.setStock(1234);

        when(productRepository.findById(124L)).thenReturn(Optional.empty());

        //when
        //assertThrows(MembershipException.class, () -> target.accumulateMembershipPoint(membershipId, "notowner", 10000));
        BaseException baseException = assertThrows(BaseException.class, () -> productService.getProduct(124L));


        //then
        assertThat(baseException.getErrorMessage()).isEqualTo(ErrorMessage.NOT_EXIST_PHOTO.getErrMsg());



    }

    @DisplayName("제품 읽기 실패 - 요구한 데이터와 읽어온 데이터가 일치 하지 않는 상황")
    @Test
    void get_error2() {

        //given
        Product givenProduct = new Product();
        givenProduct.setNumber(124L);
        givenProduct.setName("펜");
        givenProduct.setPrice(1000);
        givenProduct.setStock(1234);

        when(productRepository.findById(123L)).thenReturn(Optional.of(givenProduct));

        //when
        //assertThrows(MembershipException.class, () -> target.accumulateMembershipPoint(membershipId, "notowner", 10000));
        BaseException baseException = assertThrows(BaseException.class, () -> productService.getProduct(123L));


        //then
        assertThat(baseException.getErrorMessage()).isEqualTo(ErrorMessage.NOT_EXIST_PHOTO.getErrMsg());


    }


}
