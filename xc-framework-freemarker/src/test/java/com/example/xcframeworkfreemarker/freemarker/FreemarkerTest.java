package com.example.xcframeworkfreemarker.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.HashMap;

@SpringBootTest
public class FreemarkerTest {

    @Test
    public void generateHtmlTest() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        String path = this.getClass().getResource("/templates").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("test1.ftl");
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","Jack");
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(s);
        InputStream inputStream = IOUtils.toInputStream(s);
        OutputStream outputStream = new FileOutputStream("C:\\Users\\s_xun\\Desktop\\freemarker.html");
        int copy = IOUtils.copy(inputStream, outputStream);
        System.out.println(copy);
    }
}
