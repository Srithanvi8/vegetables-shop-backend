package org.form.shopservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.form.shopservice.dto.response.GenericResponseDTO;
import org.form.shopservice.service.ShopService;
import org.form.shopservice.dao.repo.ShopRepository;
import org.form.shopservice.Model.ShopEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopRepository shopRepository;


    @Override
    public GenericResponseDTO<List<ShopEntity>> getAllShops() {
        GenericResponseDTO<List<ShopEntity>> response;
        try {
            List<ShopEntity> details=shopRepository.findAll();
            response=new GenericResponseDTO<>(Boolean.TRUE,"data fetch",new Date(),details);
        }catch (Exception e){
            log.error(e.getMessage());
            response = new GenericResponseDTO<>(Boolean.FALSE, "Failed to fetch data", new Date(), null);
        }
        return response;
    }

    @Override
    public GenericResponseDTO<ShopEntity> addVegetables(ShopEntity shopEntity) {
        GenericResponseDTO<ShopEntity> response;
        try {
            ShopEntity savedEntity = shopRepository.save(shopEntity);
            response = new GenericResponseDTO<>(Boolean.TRUE, "Vegetable added successfully", new Date(), savedEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new GenericResponseDTO<>(Boolean.FALSE, "Failed to add vegetables", new Date(), null);
        }
        return response;
    }

    @Override
    public GenericResponseDTO<ShopEntity> editVegetables(Long id, ShopEntity shopEntity) {
        ShopEntity shopentity  = shopRepository.findById(id).orElse(null);

        if (shopentity == null) {
            return new GenericResponseDTO<>(false, "Vegetable not found", new Date(), null);
        }

        shopentity.setName(shopEntity.getName());
        shopentity.setMfgDate(shopEntity.getMfgDate());
        shopentity.setExpiredDate(shopEntity.getExpiredDate());
        shopentity.setActive(shopEntity.isActive());
        shopentity.setModifiedDate(LocalDate.now());
        shopentity.setModifiedBy(shopEntity.getModifiedBy());

        ShopEntity updatedEntity = shopRepository.save(shopentity);
        return new GenericResponseDTO<>(true, "Vegetable updated successfully", new Date(), updatedEntity);
    }

    @Override
    public GenericResponseDTO<String> deleteVegetables(Long id) {
        if (!shopRepository.existsById(id)) {
            return new GenericResponseDTO<>(false, "Vegetables not found", new Date(), "ID not found: " + id);
        }

        shopRepository.deleteById(id);
        return new GenericResponseDTO<>(true, "Vegetables deleted successfully", new Date(), "Deleted ID: " + id);
    }
}
