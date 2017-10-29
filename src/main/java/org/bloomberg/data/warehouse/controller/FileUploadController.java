package org.bloomberg.data.warehouse.controller;

import org.bloomberg.data.warehouse.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Handles requests for the file upload page.
 */
@Controller
public class FileUploadController {

    @Autowired
    private WarehouseService warehouseService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showUploadForm(HttpServletRequest request) {
        return "Upload";
    }

    @RequestMapping(value = "/doUpload", method = RequestMethod.POST)
    public String handleFileUpload(HttpServletRequest request, @RequestParam("fileUpload") MultipartFile file) throws Exception {
        String redirectPage = null;
        if (file != null) {
            System.out.println("Saving file: " + file.getOriginalFilename());
            Boolean isFile = warehouseService.getByFilename(file.getOriginalFilename());
            if (isFile) {
                System.out.println("File already processed");
                redirectPage = "Error";
            } else {
                warehouseService.processFile(file);
                redirectPage = "Success";
            }

        }
        return redirectPage;
    }
}
