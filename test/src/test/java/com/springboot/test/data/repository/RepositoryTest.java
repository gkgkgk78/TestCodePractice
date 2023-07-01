package com.springboot.test.data.repository;


import com.springboot.test.data.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Product저장 테스트")
    void save()
    {

        Product product=new Product();
        product.setName("hong");
        product.setPrice(1000);
        product.setStock(144);
        Product product1=productRepository.save(product);


        Assertions.assertEquals(product.getName(),product1.getName());
        Assertions.assertEquals(product.getPrice(),product1.getPrice());
        Assertions.assertEquals(product.getStock(),product1.getStock());


    }



}
