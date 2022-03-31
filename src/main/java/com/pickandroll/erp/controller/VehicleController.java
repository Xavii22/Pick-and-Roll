package com.pickandroll.erp.controller;

import com.pickandroll.erp.dao.VehicleDAO;
import com.pickandroll.erp.model.Cart;
import com.pickandroll.erp.model.Order;
import com.pickandroll.erp.model.Vehicle;
import com.pickandroll.erp.service.OrderService;
import com.pickandroll.erp.service.VehicleService;
import com.pickandroll.erp.utils.FileUploadUtil;
import com.pickandroll.erp.utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VehicleController {

    public List<Vehicle> vehicles = new ArrayList<Vehicle>();

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/vehicles")
    public String users(Vehicle vehicle, Model model) {

        //List<Vehicle> vehicles = vehicleDao.findAll();
        List<Vehicle> vehicles = vehicleService.listVehicles();
        model.addAttribute("vehicles", vehicles);

        return "vehicles";
    }

    @PostMapping("/editVehicle")
    public String editVehicle(@ModelAttribute Vehicle vehicle, Model model) {
        List<Vehicle> vehicles = vehicleService.listVehicles();
        model.addAttribute("vehicles", vehicles);

        // Cargar el vehiculo seleccionado en el formulario
        vehicle = vehicleService.findbyName(vehicle.getName());

        model.addAttribute("vehicle", vehicle);
        return "vehicles";
    }

    @PostMapping("/saveVehicle")
    public String saveData(@Valid Vehicle vehicle, Errors errors, Model model, RedirectAttributes msg, @RequestParam("image") MultipartFile file) throws IOException, InterruptedException {

        // Control de errores
        // MultipartFile siempre da error aunque fundione
//        if (errors.hasErrors()) {
//            List<Vehicle> vehicles = vehicleService.listVehicles();
//            model.addAttribute("vehicles", vehicles);
//            return "vehicles";
//        }
        
        // Subir la imagen al servidor si el input no está vacío
        if (!file.getOriginalFilename().isBlank()) {
            String fileName = StringUtils.cleanPath(vehicle.getId() + "_thumbnail.png");   
            try {
                String uploadDir = "src/main/resources/static/img/vehicles";
                FileUploadUtil.saveFile(uploadDir, fileName, file);
                Thread.sleep(3000); // Delay para que le de tiempo a subir la imagen
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        // Guardar los cambios en la DDBB
        vehicleService.addVehicle(vehicle);

        Utils u = new Utils();
        msg.addFlashAttribute("success", u.alert("profile.success"));

        return "redirect:/vehicles";
    }

//    @PostMapping("/deleteVehicle")
//    public String deleteVehicle(@Valid Vehicle vehicle, Errors errors, RedirectAttributes msg) {        
//        
//        Utils u = new Utils();
//        
//        vehicle = vehicleService.findById(vehicle.getId());
//        
//       vehicle.setEnabled(false);
//        
//        msg.addFlashAttribute("success", u.alert("profile.success"));
//        return "redirect:/vehicles";
//    }
    @RequestMapping(value = "/addVehicle/{id}")
    public String addVehicle(Vehicle v) {
        v = vehicleService.findVehicle(v);
        if (!vehicles.contains(v)) {
            vehicles.add(v);
        }

        return "redirect:/vehicles";
    }

    private Cart cart = new Cart();

    @GetMapping("/cart")
    public String cart(Model model) {

        cart.setPriceU(vehicles);

        cart.setSubPrice();
        cart.setTotalPrice();

        model.addAttribute("cart", cart);
        model.addAttribute("vehicles", vehicles);
        return "cart";
    }

    @RequestMapping(value = "/minus_day")
    public String minusDay() {
        cart.setDays(cart.getDays() - 1);
        return "redirect:/cart";
    }

    @RequestMapping(value = "/plus_day")
    public String plusDay() {
        cart.setDays(cart.getDays() + 1);
        return "redirect:/cart";
    }

    @RequestMapping(value = "/removeVehicle/{id}")
    public String removeVehicle(Vehicle v) {
        cart.removeVehicles(v, cart, vehicles);
        return "redirect:/cart";
    }

//    static boolean isCorrect(Vehicle vehicle) {
//
//        String[] image = vehicle.getImage_path().split("/");
//
//        if (vehicle.getDescription().isBlank() || vehicle.getName().isBlank() || vehicle.getPrice() < 0 || vehicle.getType().isBlank() || image.length < 3) {
//            return false;
//        }
//        return true;
//    }
}
