package com.sales.rest.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.rest.dto.SalesDTO;
import com.sales.rest.entity.SalesEntity;
import com.sales.rest.repo.SalesRepo;

@Service
@Transactional
public class SalesService {

	@Autowired
	private SalesRepo salesRepo;

	public List<SalesDTO> findSalesList() {
		return salesRepo.findAll().stream().map(this::copySalesEntityToDto).collect(Collectors.toList());
	}

	public SalesDTO findBySalesId(Long userId) {
		SalesEntity userEntity = salesRepo.findBySaleId(userId);
		return copySalesEntityToDto(userEntity);
	}

	public void createOrUpdateSales(SalesDTO salesDTO) {
		SalesEntity salesEntity = copySalesDtoToEntity(salesDTO);
		salesRepo.save(salesEntity);
	}

	public void deleteSales(Long usrId) {
		salesRepo.deleteById(usrId);
	}

	private SalesDTO copySalesEntityToDto(SalesEntity salesEntity) {
		SalesDTO salesDTO = new SalesDTO();
		BeanUtils.copyProperties(salesEntity, salesDTO);
		return salesDTO;
	}

	private SalesEntity copySalesDtoToEntity(SalesDTO salesDTO) {
		SalesEntity userEntity = new SalesEntity();
		BeanUtils.copyProperties(salesDTO, userEntity);
		return userEntity;
	}

}
