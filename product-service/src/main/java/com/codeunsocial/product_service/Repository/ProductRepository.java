package com.codeunsocial.product_service.Repository;

import com.codeunsocial.product_service.Model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
