package ra.security.service.impl.manufacturerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.security.exception.ManufacturerException;
import ra.security.exception.MyCustomRuntimeException;
import ra.security.exception.ProductException;
import ra.security.model.domain.Category;
import ra.security.model.domain.Manufacturer;
import ra.security.model.domain.Product;
import ra.security.model.dto.request.ManufacturerRequestDTO;
import ra.security.model.dto.response.CategoryResponseDto;
import ra.security.model.dto.response.ManufacturerResponseDTO;
import ra.security.model.dto.response.ProductResponseDTO;
import ra.security.repository.ManufacturerRepository;
import ra.security.service.IManufacturerService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManufacturerService implements IManufacturerService {
@Autowired
    private ManufacturerRepository manufacturerRepository;

    @Override
    public List<ManufacturerResponseDTO> getAllManufacturers(String name, Pageable pageable) {
        Page<Manufacturer> manufacturers;
        if (name.isEmpty()) {
            manufacturers = manufacturerRepository.findAll(pageable);
        } else {
            manufacturers = manufacturerRepository.findAllByNameContainingIgnoreCase(name, pageable);
        }

        return manufacturers.map(this::convertToResponseDTO).getContent();
    }
    @Override
    public ManufacturerResponseDTO getManufacturerById(Long id) throws ManufacturerException {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(id);
        return optionalManufacturer.map(this::convertToResponseDTO).orElseThrow(()->new ManufacturerException("Không tìm thấy hãng sản xuất"));
    }
    @Override
    public ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO requestDTO) throws ManufacturerException {
        if(manufacturerRepository.findByName(requestDTO.getName())!=null){
            throw new ManufacturerException("hãng sản xuất đã tồn tại");
        }
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName(requestDTO.getName());


        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);
        return convertToResponseDTO(savedManufacturer);
    }
    @Override
    public ManufacturerResponseDTO updateManufacturer(Long id, ManufacturerRequestDTO requestDTO) throws ManufacturerException {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(id);
        if(manufacturerRepository.findByName(requestDTO.getName())!=null){
            throw new ManufacturerException("hãng sản xuất đã tồn tại");
        }
        if (optionalManufacturer.isPresent()) {
            Manufacturer manufacturer = optionalManufacturer.get();
            if(requestDTO.getName()!=null){
                manufacturer.setName(requestDTO.getName());
            }
            Manufacturer updatedManufacturer = manufacturerRepository.save(manufacturer);
            return convertToResponseDTO(updatedManufacturer);
        }
        else {
            throw new ManufacturerException("Không tìm thấy hãng sản xuât");
        }
    }
    @Override
    public ManufacturerResponseDTO lockManufacturer(Long id) throws ManufacturerException {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ManufacturerException("Không tìm thấy hãng sản xuất"));


        manufacturer.setStatus(false);

        return convertToResponseDTO( manufacturerRepository.save(manufacturer));
    }

    @Override
    public ManufacturerResponseDTO unlockManufacturer(Long id) throws ManufacturerException {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ManufacturerException("Không tìm thấy hãng sản xuất"));


        manufacturer.setStatus(true);

        return convertToResponseDTO( manufacturerRepository.save(manufacturer));
    }
    private ManufacturerResponseDTO convertToResponseDTO(Manufacturer manufacturer) {
        ManufacturerResponseDTO responseDTO = new ManufacturerResponseDTO();
        responseDTO.setId(manufacturer.getId());
        responseDTO.setName(manufacturer.getName());
        responseDTO.setStatus(manufacturer.isStatus());
        return responseDTO;
    }
}
