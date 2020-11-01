package com.product.rest.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.rest.dto.ProductDTO;
import com.product.rest.entity.ProductEntity;
import com.product.rest.repo.ProductRepo;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepo productRepo;

	public List<ProductDTO> findProductList() {
		return productRepo.findAll().stream().map(this::copyProductEntityToDto).collect(Collectors.toList());
	}

	public ProductDTO findByProductId(Long pId) {
		ProductEntity productEntity = productRepo.findByProductId(pId);
		return copyProductEntityToDto(productEntity);
	}

	public void createOrUpdateProduct(ProductDTO productDTO) {
		ProductEntity productEntity = copyProductDtoToEntity(productDTO);
		productRepo.save(productEntity);
	}

	public void deleteProduct(Long empId) {
		productRepo.deleteById(empId);
	}

	private ProductDTO copyProductEntityToDto(ProductEntity productEntity) {
		ProductDTO productDTO = new ProductDTO();
		BeanUtils.copyProperties(productEntity, productDTO);
		return productDTO;
	}

	private ProductEntity copyProductDtoToEntity(ProductDTO ProductDTO) {
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(ProductDTO, productEntity);
		return productEntity;
	}

}
