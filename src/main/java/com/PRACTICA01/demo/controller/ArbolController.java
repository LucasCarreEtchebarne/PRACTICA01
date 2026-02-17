package com.PRACTICA01.demo.controller;

import com.PRACTICA01.demo.domain.Arbol;
import com.PRACTICA01.demo.service.ArbolService;
import jakarta.validation.Valid;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(
            @Valid Arbol arbol,
            BindingResult br,
            @RequestParam(required = false) MultipartFile imagenFile,
            RedirectAttributes ra,
            Model model) {

        if (br.hasErrors()) {
            var arboles = arbolService.getArboles();
            model.addAttribute("arboles", arboles);
            model.addAttribute("totalArboles", arboles.size());
            model.addAttribute("arbol", arbol);
            return "/arbol/listado";
        }

        arbolService.save(arbol, imagenFile);

        ra.addFlashAttribute("todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
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
        return "/arbol/modifica"; // 
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
