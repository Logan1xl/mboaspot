package com.example.backend.controllers;


import com.example.backend.dto.EquipementDTO;
import com.example.backend.services.implementations.EquipementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;




@RestController
@RequestMapping("/equipement")


public class EquipementController {
    private EquipementService equipementService;

    public EquipementController(EquipementService equipementService) {
            this.equipementService = equipementService;

    }
    @PostMapping
    public ResponseEntity<?> save(@RequestBody EquipementDTO equipementDTO) {

        try {

            return new ResponseEntity<>(equipementService.save(equipementDTO), HttpStatus.CREATED);
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/{id}")
public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EquipementDTO equipementDTO) {
    try {
        return new ResponseEntity<>(equipementService.getAll(), HttpStatus.OK);

    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
    @DeleteMapping("/{id}")
public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
        EquipementService localisationService;
        return new ResponseEntity<>(equipementService.getAll(), HttpStatus.OK);

    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    }
    @GetMapping("/{id}")
public ResponseEntity<?> getById(@PathVariable Long id) {
    try {
        return new ResponseEntity<>(equipementService.getAll(), HttpStatus.OK);


    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}  
    @GetMapping("/all")
public ResponseEntity<?> getAll() {
    try {
        EquipementService localisationService;
        return new ResponseEntity<>(equipementService.getAll(), HttpStatus.OK);


    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
}




