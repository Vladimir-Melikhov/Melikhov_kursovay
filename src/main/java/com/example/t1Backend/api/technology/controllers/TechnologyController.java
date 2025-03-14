package com.example.t1Backend.api.technology.controllers;

import com.example.t1Backend.api.technology.contracts.TechnologyDTO;
import com.example.t1Backend.dal.entities.Technology;
import com.example.t1Backend.dal.entities.TechnologyArchived;
import com.example.t1Backend.api.technology.servises.TechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "technology_controller")
@RestController
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @Operation(summary = "Добавляет новую технологию в базу.")
    @PostMapping("/api/add")
    public void AddTechnology(@RequestBody TechnologyDTO technologyDTO) {
        technologyService.AddTechnology(technologyDTO);
    }

    @Operation(summary = "Обновновляет уже существующую технологию.")
    @PutMapping("/api/update")
    public void UpdateTechnology(@RequestBody TechnologyDTO technology) {
        technologyService.UpdateTechnology(technology);
    }

    @Operation(summary = "Ищет технологию по кольцу.")
    @GetMapping("api/findByRing")
    public List<Technology> FindByRing(@RequestParam String ring) {
        return technologyService.FindByRing(ring);
    }

    @Operation(summary = "Ищет технологию по секции.")
    @GetMapping("api/findBySection")
    public List<Technology> FindBySection(@RequestParam String section) {
        return technologyService.FindBySection(section);
    }

    @Operation(summary = "Ищет технологию по категории.")
    @GetMapping("api/findByCategory")
    public List<Technology> FindByCategory(@RequestParam String category) {
        return technologyService.FindByCategory(category);
    }

    @Operation(summary = "Удаляет технологию по id.")
    @DeleteMapping("/api/deleteById")
    public void DeleteTechnology(@RequestParam int id) {
        technologyService.DeleteTechnologyId(id);
    }

    @Operation(summary = "Удаляет технологию по имени.")
    @DeleteMapping("/api/deleteByName")
    public void DeleteTechnology(@RequestParam String name) {
        technologyService.DeleteTechnologyName(name);
    }

    @Operation(summary = "Архивирует технологию по id.")
    @PostMapping("/api/archiveById")
    public void Archive(@RequestParam int id) {
        technologyService.ArchiveId(id);
    }

    @Operation(summary = "Архивирует технологию по имени.")
    @PostMapping("/api/archiveByName")
    public void Archive(@RequestParam String name) {
        technologyService.ArchiveName(name);
    }

    @Operation(summary = "Возвращает список архивированных технологий.")
    @GetMapping("/api/getArchive")
    public List<TechnologyArchived> GetArchive() {
        return technologyService.GetArchive();
    }
}
