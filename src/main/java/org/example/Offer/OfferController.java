package org.example.Offer;

import jakarta.validation.Valid;
import org.example.Mail.EmailService;
import org.example.Mail.EmailServiceImpl;
import org.example.Mail.SubscriptionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferService offerService;
    private final EmailService emailService;

    @Autowired
    public OfferController(OfferService offerService, EmailService emailService) {
        this.offerService = offerService;
        this.emailService = emailService;
    }

    @PostMapping()
    public ResponseEntity<String> createOffer(@Valid @RequestBody OfferDTO offerDTO) {
        Offer createdOffer = offerService.createOffer(offerDTO);
        return ResponseEntity.ok("ok");




    }
    @GetMapping
    public List<OfferResponse> getOffers(
            @RequestParam(required = false) Integer university,
            @RequestParam(required = false) Integer speciality,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Integer country,
            @RequestParam(required = false) Integer city,
            @RequestParam(required = false) List<Integer> duration,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer scholarshipMin,
            @RequestParam(required = false) Integer scholarshipMax
    ) {
        return offerService.getFilteredOffers(university, speciality, semester, searchTerm, country, city, duration, category, language, scholarshipMin, scholarshipMax);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable("id") Integer id) {
        OfferResponse offer = offerService.getOfferById(id);
        if (offer != null) {
            return ResponseEntity.ok(offer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToOffer(@Valid @RequestBody SubscriptionRequest request) {
        String response = emailService.sendMail(request);
        return ResponseEntity.ok(response);}
}
