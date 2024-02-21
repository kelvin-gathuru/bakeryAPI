package com.threepmanagerapi.threepmanagerapi.region.service;
import com.threepmanagerapi.threepmanagerapi.region.model.Region;
import com.threepmanagerapi.threepmanagerapi.region.repository.RegionRepository;
import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import com.threepmanagerapi.threepmanagerapi.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity createRegion(String token, Region region){
        try{
            Optional<Region> existingRegion = regionRepository.findByName(region.getName());
            Long userID = jwtService.extractuserID(token);
            if(existingRegion.isPresent()){
                return responseService.formulateResponse(
                        null,
                        "Region Number Already Exists",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }

            region.setUser(userRepository.findByUserID(userID));
            regionRepository.save(region);
            return responseService.formulateResponse(
                    null,
                    "Region added successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception creating region ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity getRegions(){
        try{
            List<Region> regions = regionRepository.findAll();
            return responseService.formulateResponse(
                    regions,
                    "Regions fetched successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception fetching regions ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
}
