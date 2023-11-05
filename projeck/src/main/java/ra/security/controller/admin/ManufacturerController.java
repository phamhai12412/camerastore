package ra.security.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.security.exception.ManufacturerException;
import ra.security.exception.ProductException;
import ra.security.model.dto.request.ManufacturerRequestDTO;
import ra.security.model.dto.response.ManufacturerResponseDTO;
import ra.security.model.dto.response.ProductResponseDTO;
import ra.security.service.IManufacturerService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/admin/manufacturers")
@CrossOrigin("*")
public class ManufacturerController {

    @Autowired
    private IManufacturerService manufacturerService;

    @GetMapping


    public ResponseEntity<List<ManufacturerResponseDTO>> getPaginatedManufacturers(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String by) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(by), sort);
        List<ManufacturerResponseDTO> manufacturers = manufacturerService.getAllManufacturers(name, pageable);
        return new ResponseEntity<>(manufacturers, HttpStatus.OK);
    }


    @GetMapping("/get/{id}")


    public ResponseEntity<ManufacturerResponseDTO> getManufacturerById(@PathVariable Long id) throws ManufacturerException {
        ManufacturerResponseDTO manufacturer = manufacturerService.getManufacturerById(id);
            return ResponseEntity.ok(manufacturer);
    }

    @PostMapping("/add")


    public ResponseEntity<ManufacturerResponseDTO> createManufacturer(@RequestBody @Valid ManufacturerRequestDTO requestDTO) throws ManufacturerException {
        ManufacturerResponseDTO createdManufacturer = manufacturerService.createManufacturer(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdManufacturer);
    }

    @PutMapping("/edit/{id}")


    public ResponseEntity<ManufacturerResponseDTO> updateManufacturer(
            @PathVariable Long id,
            @RequestBody ManufacturerRequestDTO requestDTO
    ) throws ManufacturerException {
        ManufacturerResponseDTO updatedManufacturer = manufacturerService.updateManufacturer(id, requestDTO);
            return ResponseEntity.ok(updatedManufacturer);
    }

    @PutMapping("/lock/{id}")


    public ResponseEntity<String> lockUserAccount(@PathVariable Long id) throws ManufacturerException {
       manufacturerService.lockManufacturer(id);
        return ResponseEntity.ok("Thiết lập trạng thái khóa thành công");
    }
    @PutMapping("/unlock/{id}")


    public ResponseEntity<String> unlockUserAccount(@PathVariable Long id) throws ManufacturerException {
   manufacturerService.unlockManufacturer(id);
        return ResponseEntity.ok("Thiết lập trạng thái mở khóa thành công");
    }
    }

