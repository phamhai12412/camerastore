package ra.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.security.exception.ManufacturerException;
import ra.security.exception.ProductException;
import ra.security.model.dto.request.ManufacturerRequestDTO;
import ra.security.model.dto.response.CategoryResponseDto;
import ra.security.model.dto.response.ManufacturerResponseDTO;
import ra.security.model.dto.response.ProductResponseDTO;

import java.util.List;

public interface IManufacturerService {

    List<ManufacturerResponseDTO> getAllManufacturers(String name, Pageable pageable);

    ManufacturerResponseDTO getManufacturerById(Long id) throws ManufacturerException;

    ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO requestDTO) throws ManufacturerException;

    ManufacturerResponseDTO updateManufacturer(Long id, ManufacturerRequestDTO requestDTO) throws ManufacturerException;
    ManufacturerResponseDTO lockManufacturer(Long id) throws ManufacturerException;

    ManufacturerResponseDTO unlockManufacturer(Long id) throws ManufacturerException;

}
