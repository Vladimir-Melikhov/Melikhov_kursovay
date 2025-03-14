package com.example.t1Backend.api.technology.servises;

import com.example.t1Backend.api.technology.contracts.TechnologyDTO;
import com.example.t1Backend.dal.entities.Technology;
import com.example.t1Backend.dal.entities.TechnologyArchived;
import com.example.t1Backend.dal.repositories.TechnologyArchivedRepo;
import com.example.t1Backend.dal.repositories.TechnologyRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepo technologyRepo;
    private final TechnologyArchivedRepo technologyArchivedRepo;

    @Transactional
    public void AddTechnology(TechnologyDTO tech){
        Technology technology = TechnologyMap(tech);
        if (technologyRepo.existsByName(technology.getName()))
            throw new CustomBadRequestException("Похоже вы пытайтесь создать уже существующую технологию");
        else {
            Date date = new Date();
            technology.setRingChanges(date + " - " + technology.getRing());
            technologyRepo.save(technology);
        }
    }

    @Transactional
    public void UpdateTechnology(TechnologyDTO tech){
        if (technologyRepo.existsByName(tech.getName())) {
            Technology technology = technologyRepo.findByName(tech.getName());
            technology.setCategory(tech.getCategory());
            technology.setSection(tech.getSection());
            technology.setDescription(tech.getDescription());
            if (!Objects.equals(technology.getRing(), tech.getRing())) {
                Date date = new Date();
                technology.setRing(technology.getRing());
                technology.setRingChanges(technology.getRingChanges() + "\n" + date + " - " + technology.getRing());
            }
            technologyRepo.save(technology);
        }
        else {
            throw new CustomBadRequestException("Похоже вы пытались обновить несуществующую технологию");
        }
    }

    public List<Technology> FindByRing(String ring) {
        List<Technology> technologies = technologyRepo.findAllByRing(ring);
        technologies.sort(Comparator.comparing(Technology::getId)); //Отсортировал для более удобного восприятия
        return technologies;
    }

    public List<Technology> FindBySection(String section) {
        List<Technology> technologies = technologyRepo.findAllBySection(section);
        technologies.sort(Comparator.comparing(Technology::getId)); //Отсортировал для более удобного восприятия
        return technologies;
    }

    public List<Technology> FindByCategory(String category) {
        List<Technology> technologies = technologyRepo.findAllByCategory(category);
        technologies.sort(Comparator.comparing(Technology::getId)); //Отсортировал для более удобного восприятия
        return technologies;
    }

    @Transactional
    public void DeleteTechnologyId(int id){
        if (technologyRepo.existsById(id)) {
            technologyRepo.deleteById(id);
        }
        else{
            throw new CustomBadRequestException("Похоже вы пытайтесь удалить несуществующую технологию");
        }
    }

    @Transactional
    public void DeleteTechnologyName(String name){
        if (technologyRepo.existsByName(name)) {
            technologyRepo.deleteByName(name);
        }
        else{
            throw new CustomBadRequestException("Похоже вы пытайтесь удалить несуществующую технологию");
        }
    }

    @Transactional
    public void ArchiveId(int id){
        if (technologyRepo.existsById(id)) {
            Technology tech = technologyRepo.findById(id);
            WriteArchivedParameters(tech);
            technologyRepo.delete(technologyRepo.findById(id));
        }
        else{
            throw new CustomBadRequestException("Похоже вы пытайтесь архивиовать несуществующую технологию");
        }
    }

    @Transactional
    public void ArchiveName(String name){
        if (technologyRepo.existsByName(name)) {
            Technology tech = technologyRepo.findByName(name);
            WriteArchivedParameters(tech);
            technologyRepo.delete(technologyRepo.findByName(name));
        }
        else{
            throw new CustomBadRequestException("Похоже вы пытайтесь архивиовать несуществующую технологию");
        }
    }

    public List<TechnologyArchived> GetArchive() {
        return technologyArchivedRepo.findAll();
    }

    private Technology TechnologyMap(TechnologyDTO technology) {
        Technology tech = new Technology();
        tech.setName(technology.getName());
        tech.setCategory(technology.getCategory());
        tech.setSection(technology.getSection());
        tech.setDescription(technology.getDescription());
        tech.setRing(technology.getRing());

        return tech;
    }

    private void WriteArchivedParameters(Technology tech) {
        TechnologyArchived archived = new TechnologyArchived();
        archived.setId(tech.getId());
        archived.setName(tech.getName());
        archived.setDescription(tech.getDescription());
        archived.setCategory(tech.getCategory());
        archived.setSection(tech.getSection());
        archived.setRing(tech.getRing());
        archived.setRingChanges(tech.getRingChanges());
        technologyArchivedRepo.save(archived);
    }

    public static class CustomBadRequestException extends RuntimeException {
        public CustomBadRequestException(String message) {
            super(message);
        }
    }

    record ErrorResponse(int status, String message) { }
    @ControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(CustomBadRequestException.class)
        private ResponseEntity<ErrorResponse> handleCustomBadRequest(CustomBadRequestException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
