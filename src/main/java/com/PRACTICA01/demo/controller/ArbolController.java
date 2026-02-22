package com.PRACTICA01.demo.controller;

import com.PRACTICA01.demo.domain.Arbol;
import com.PRACTICA01.demo.service.ArbolService;
import jakarta.validation.Valid;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/arbol")
public class ArbolController {

    private final ArbolService arbolService;
    private final MessageSource messageSource;

    public ArbolController(ArbolService arbolService, MessageSource messageSource) {
        this.arbolService = arbolService;
        this.messageSource = messageSource;
    }

    // LISTADO
    @GetMapping("/listado")
    public String listado(Model model) {
        var arboles = arbolService.getArboles();
        model.addAttribute("arboles", arboles);
        model.addAttribute("totalArboles", arboles.size());
        model.addAttribute("arbol", new Arbol());
        return "/arbol/listado";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid Arbol arbol,
            @RequestParam(required = false) MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {

        try {
            arbolService.save(arbol, imagenFile);

            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo guardar. Revise los datos.");
        }

        return "redirect:/arbol/listado";
    }

    // MODIFICAR
    @GetMapping("/modificar/{idArbol}")
    public String modificar(@PathVariable Integer idArbol, Model model, RedirectAttributes ra) {
        var arbolOpt = arbolService.getArbol(idArbol);

        if (arbolOpt.isEmpty()) {
            ra.addFlashAttribute("error", "El árbol no existe.");
            return "redirect:/arbol/listado";
        }

        model.addAttribute("arbol", arbolOpt.get());
        return "/arbol/modifica";
    }

    // ELIMINAR
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idArbol, RedirectAttributes ra) {
        try {
            arbolService.delete(idArbol);
            ra.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.eliminado", null, Locale.getDefault()));
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se pudo eliminar (puede tener datos asociados).");
        }
        return "redirect:/arbol/listado";
    }
}
