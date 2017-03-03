package com.madera.app.web.rest;

import com.cloudinary.*;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import javax.xml.bind.DatatypeConverter;
import org.springframework.web.multipart.*;
import javax.servlet.http.*; 
import java.io.Serializable.*;

import java.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Date; 
import java.util.Map;
import com.cloudinary.utils.ObjectUtils;

import javax.inject.Inject;
import java.net.URI;
import javax.validation.Valid; 
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Image.
 */
@RestController
@RequestMapping("/api")
public class _ImageResource {

    private final Logger log = LoggerFactory.getLogger(_ImageResource.class);

    private Environment env; 

    @Autowired
    private HttpServletRequest request;

    // save l'image
    @RequestMapping(value="/images/store",
        method = RequestMethod.POST,
        produces = MediaType.MULTIPART_FORM_DATA_VALUE )
    @Transactional
    @Timed 
    public String saveImage(@Valid @RequestPart(value = "fileUploadImage")  MultipartFile file)
    {    
         log.debug("Working Directory = " + System.getProperty("user.dir"));
       
         Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
          "cloud_name", "hejvjmwx8",
          "api_key", "355957355283844",
          "api_secret", "FB35qqf13TbKevIqPb2Y_THfAwI"));




         // log.debug(env.getProperty("pictureFolder.url")); 
        Date date = new Date(); 
        Long time = date.getTime(); 
        String fileName = time.toString(); 
        // Part myFile = request.getPart("fileUploadImage");
        // String filePath =  "http://static.eqh.gamit.ninja/"+ fileName + ".png";
        String filePath =  fileName + ".png";
        // String filePath = request.getServletContext().getRealPath("/content/assets/")+ fileName + ".png";
        log.debug(filePath); 
        Map uploadResult = null;
        try 
        {
           // File toUpload = new File("daisy.png"); 
            //File dest = new File(filePath);
            File convFile = convert(file);
            uploadResult = cloudinary.uploader().upload(convFile, ObjectUtils.emptyMap());
            //file.transferTo(dest);    
        }
        catch(Exception e)
        {
            e.printStackTrace(); 
        }
        
        log.debug("REST request to save image on folder");
       
        return "{\"url\":\"" + ((String) uploadResult.get("public_id")) + "\"}";
    }

    public File convert(MultipartFile file)
    {    
         try 
        {
            File convFile = new File(file.getOriginalFilename());
            convFile.createNewFile(); 
            FileOutputStream fos = new FileOutputStream(convFile); 
            fos.write(file.getBytes());
            fos.close(); 
             return convFile;
        }
        catch(Exception e)
        {
            e.printStackTrace(); 
        }
        return null;
       
    }


    @RequestMapping(value="/images/remove/{url}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public void removeImage(@PathVariable String url)
    {
        log.debug(url);
        String pathImage = url;
        //Remise à défaut de l'ensemble des pois
      /* _poiRepository.resetPoi(image.getId());

        imageRepository.delete(image.getId());*/
          Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
         "cloud_name", "hejvjmwx8",
          "api_key", "355957355283844",
          "api_secret", "FB35qqf13TbKevIqPb2Y_THfAwI"));

        try
        {
           cloudinary.uploader().destroy(url, ObjectUtils.emptyMap()); 
        }catch(Exception e)
        {
            // if any error occurs
             e.printStackTrace();
        }
        
    }
}
