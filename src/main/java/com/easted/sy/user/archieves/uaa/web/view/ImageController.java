package com.easted.sy.user.archieves.uaa.web.view;


import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping(value = "/images")
public class ImageController {


    /*实名认证文件目录*/
    @Value("${realName.filePath}")
    private  String dirPath;

    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/{login}/{fileName:.+}", method = RequestMethod.GET)
    public byte[] getImage(@PathVariable("login")String login,@PathVariable("fileName")String fileName,HttpServletResponse response) throws IOException {

        String filePath=dirPath+"/"+login+"/"+fileName;
        Resource resource=new FileSystemResource(filePath);
        InputStream in = resource.getInputStream();

        IOUtils.copy(in, response.getOutputStream());
        return IOUtils.toByteArray(in);
    }
}
