package br.com.andrei.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.andrei.entity.Country;
import br.com.andrei.error.CustomErrorType;
import br.com.andrei.service.CountryService;

@RestController
@EnableWebMvc
@RequestMapping("/countries")
@CrossOrigin("*")
public class RestCountryController {


    public static final Logger logger = LoggerFactory.getLogger(RestCountryController.class);
 
    @Autowired
    private CountryService countryService;
 
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Country>> listAllCountries() {
    	logger.info("Fetching All countries");
        
    	List<Country> countries = countryService.getCountries();
        
        if (countries.isEmpty()) {
        	logger.error("No country found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<List<Country>>(countries, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/country/{id}", method = RequestMethod.GET)
    public ResponseEntity<Optional<Country>> getCountry(@PathVariable("id") long id) {
        logger.info("Fetching Country with id {}", id);
        Optional<Country> country = countryService.getCountry(id);
        if (country == null) {
            logger.error("Country with id {} not found.", id);
            return new ResponseEntity("Country with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(country, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addCountry(@RequestBody Country country) {
        logger.info("Creating Country : {}", country);
 
		if (countryService.isCountryExist(country)) {
			logger.error("Unable to create. The COuntry with name {} already exist", country.getName());
			return new ResponseEntity(new CustomErrorType("Unable to create. The Country with name "+country.getName()+" already exist"), HttpStatus.CONFLICT);
		}
		countryService.saveCountry(country);
		
		return new ResponseEntity("Country successfully created", HttpStatus.CREATED);

    }
 
    // ------------------- Update a Country ------------------------------------------------
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCountry(@PathVariable("id") String id, @Valid @RequestBody Country country) {
        logger.info("Updating Country with id {}", id);
 
        Optional<Country> countryExist = countryService.getCountry(Long.parseLong(id));
        Country currentCountry = countryExist.get();
 
        if (currentCountry == null) {
            logger.error("Unable to update. Country with id {} not found.", id);
            return new ResponseEntity<>("Unable to update. Country with id " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        
        currentCountry.setName(country.getName());
 
        countryService.saveCountry(currentCountry);
        return new ResponseEntity<>(currentCountry, HttpStatus.OK);
    }
 
    // ------------------- Delete a Country-----------------------------------------
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCountry(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting Country with id {}", id);
 
        Optional<Country> country = countryService.getCountry(id);
        if (country == null) {
            logger.error("Unable to delete. Country with id {} not found.", id);
            return new ResponseEntity<String>("Unable to delete. Country with id " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        countryService.deleteCountry(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
 
    // ------------------- Delete All Countries-----------------------------
    @RequestMapping(value = "/deleteAll/", method = RequestMethod.DELETE)
    public ResponseEntity<Country> deleteAllCountries() {
        logger.info("Deleting All Countries");
        countryService.deleteAllCountries();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
