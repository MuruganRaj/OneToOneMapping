package com.example.demo;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.sun.javaws.exceptions.MissingFieldException;
import javassist.NotFoundException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.hibernate.action.internal.EntityActionVetoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DemoController {

    @Autowired
    WifeRepository wifeRepository;

    @Autowired
    DemoService demoService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    HusbandRepository husbandRepository;

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);


    @GetMapping(value = "/v1/getWife")
    public ApiResponse getAll() {
        return new ApiResponse(new Date(), "success", "200", wifeRepository.findAll());
    }



    @PostMapping("/sampe")
    public ResponseEntity<?> createValidate(@RequestBody @Valid UserValidation user){

        return  ResponseEntity.created(URI.create("/meaage/1")).body("1");
    }


    //fileupload
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        logger.info("started step1");
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        logger.info("started step2");

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }


//    multiple file upload

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }



//file download

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/addWife")
    public Wife addwife(@Valid @RequestBody Wife wife) {
        return wifeRepository.save(wife);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingPathVariableException.class)
    public ApiResponse exception(MissingPathVariableException e) {

        return new ApiResponse(new Date(), "Error", e.getMessage());

    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiResponse methodException(NotFoundException e) {
        return new ApiResponse("Error", e.getMessage());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse methodIntenral(InternalException e) {
        return new ApiResponse("Error", e.getMessage());
    }

    @RequestMapping("/getTest")
    public void getList() {

        throw new EntityNotFoundException("not dsdsddddd");
//         return new ApiResponse(new Date(),"success","200");

    }


    @PostMapping("/insert")
    public ApiResponse createWife(@RequestBody Wife wife) {
        if (wife == null) {
            return new ApiResponse(new Date(), "check input parameter", "200");

        } else {
            demoService.create(wife);
            return new ApiResponse(new Date(), "successfully created ", "200");
        }

    }


}
