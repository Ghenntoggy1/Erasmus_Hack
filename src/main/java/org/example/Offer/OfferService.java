package org.example.Offer;

import org.example.Specialization.SpecializationDTO;
import org.example.University.University;
import org.example.University.UniversityRepository;
import org.example.Specialization.Specialization;
import org.example.Specialization.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UniversityRepository universityRepository;
    private final SpecializationRepository specializationRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository, UniversityRepository universityRepository, SpecializationRepository specializationRepository) {
        this.offerRepository = offerRepository;
        this.universityRepository = universityRepository;
        this.specializationRepository = specializationRepository;
    }

    public Offer createOffer(OfferDTO offerDTO) {
        University sender = universityRepository.findById(offerDTO.getUniversity_sender())
                .orElseThrow(() -> new RuntimeException("Sender University not found"));
        University receiver = universityRepository.findById(offerDTO.getUniversity_receiver())
                .orElseThrow(() -> new RuntimeException("Receiver University not found"));

        List<Specialization> specializations = offerDTO.getSpecializations().stream()
                .map(specializationDTO -> specializationRepository.findById(specializationDTO.getSpecialization_id())
                        .orElseThrow(() -> new RuntimeException("Specialization not found")))
                .collect(Collectors.toList());

        Offer offer = Offer.builder()
                .offer_name(offerDTO.getOffer_name())
                .description(offerDTO.getDescription())
                .offer_start_date(offerDTO.getOffer_start_date())
                .offer_end_date(offerDTO.getOffer_end_date())
                .program_start(offerDTO.getProgram_start())
                .program_end(offerDTO.getProgram_end())
                .scholarship(offerDTO.getScholarship())
                .sender_id(sender)
                .receiver_id(receiver)
                .specializations(specializations)
                .build();

        return offerRepository.save(offer);
    }

    public OfferResponse getOfferById(Integer id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with ID: " + id));

        return mapToDTO(offer);
    }


    public List<OfferResponse> getFilteredOffers(Integer university, Integer speciality, Integer semester, String searchTerm, Integer country, Integer city, List<Integer> duration, Integer category, String language, Integer scholarshipMin, Integer scholarshipMax) {
        Specification<Offer> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (university != null) {
                predicates.add(criteriaBuilder.equal(root.get("receiver_id"), university));
            }
            if (speciality != null) {
                predicates.add(criteriaBuilder.isMember(speciality, root.get("specializations")));
            }
            if (semester != null) {
                predicates.add(criteriaBuilder.equal(root.get("course").get("course_semester"), semester));
            }
            if (searchTerm != null && !searchTerm.isEmpty()) {
                Predicate searchPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("offer_name"), "%" + searchTerm + "%"),
                        criteriaBuilder.like(root.get("description"), "%" + searchTerm + "%")
                );
                predicates.add(searchPredicate);
            }
            if (country != null) {
                predicates.add(criteriaBuilder.equal(root.get("university").get("country").get("country_id"), country));
            }
            if (city != null) {
                predicates.add(criteriaBuilder.equal(root.get("university").get("city").get("city_id"), city));
            }
            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("category_id"), category));
            }
            if (language != null && !language.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("language"), language));
            }
            if (scholarshipMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("scholarship"), scholarshipMin));
            }
            if (scholarshipMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("scholarship"), scholarshipMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Offer> filteredOffers = offerRepository.findAll(specification);

        // Calculate duration and filter offers programmatically
        List<Offer> offersWithDuration = filteredOffers.stream()
                .filter(offer -> calculateDuration(offer) != null && duration.contains(calculateDuration(offer)))
                .toList();

        return offersWithDuration.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private Integer calculateDuration(Offer offer) {
        if (offer.getProgram_start() != null && offer.getProgram_end() != null) {
            long diffMillis = offer.getProgram_end().getTime() - offer.getProgram_start().getTime();
            long diffMonths = TimeUnit.MILLISECONDS.toDays(diffMillis) / 30;
            return (int) diffMonths;
        }
        return null;
    }


    private OfferResponse mapToDTO(Offer offer) {
        OfferResponse offerResponse = OfferResponse.builder()
                .id(offer.getOffer_id())
                .offer_name(offer.getOffer_name())
                .description(offer.getDescription())
                .offer_end_date(formatDate(offer.getOffer_end_date()))
                .offer_start_date(formatDate(offer.getOffer_start_date()))
                .program_start(offer.getProgram_start())
                .program_end(offer.getProgram_end())
                .scholarship(offer.getScholarship())
                .language(offer.getLanguage())
                .receiver_name(offer.getReceiver_id().getUniversity_name())
                .country_name(offer.getSender_id().getCity_id().getCountry_id().getCountry_name())
                .city_name(offer.getSender_id().getCity_id().getCity_name())
                .university_name(offer.getSender_id().getUniversity_name())
                .specializations(mapSpecializationsToDTO(offer.getSpecializations()))
                .build();

        return offerResponse;
    }
    private List<SpecializationDTO> mapSpecializationsToDTO(List<Specialization> specializations) {
        return specializations.stream()
                .map(specialization -> SpecializationDTO.builder()
                        .specialization_id(specialization.getSpecialization_id())
                        .specialization_name(specialization.getSpecialization_name())
                        .build())
                .collect(Collectors.toList());
    }
    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }
}