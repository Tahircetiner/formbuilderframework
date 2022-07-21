package com.example.formbuilderframework;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@CrossOrigin
public class FormularFilesController {
    ClassPathResource templateFormularManipulated = new ClassPathResource("/templates/templateformular.html");
    ClassPathResource templateFormularOriginal = new ClassPathResource("templates/templateformularOriginal.html");
    Document document = Jsoup.parse(templateFormularManipulated.getFile(), "UTF-8");

    public FormularFilesController() throws IOException {
    }

    @RequestMapping(value="/api/createFormularFileZip", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, method = RequestMethod.GET)
    public ResponseEntity<byte[]> createFormularFileZip() throws IOException {
        document.select("body").remove();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("content-disposition", "attachment; filename=" + "test.zip");


        ClassPathResource classPathResource = new ClassPathResource("/data/compressed.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(classPathResource.getFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(classPathResource.getInputStream().readAllBytes());
    }

    /* TODO: AUSLAGERN IN SERVICES */
    @RequestMapping(value = "/api/storeInputFields", method = RequestMethod.POST)
    public HttpStatus storeInputFields(@RequestBody List<String> fields) throws IOException {

        try{
            Element form = document.getElementsByClass("form").first();
            for (String field : fields) {
                String  replaced =field.replace("[", "").replace("]", "");
                form.append(replaced);
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(templateFormularManipulated.getFile()));
            writer.write(document.toString());
            writer.close();

            FileOutputStream fos = new FileOutputStream("target/classes/data/compressed.zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(templateFormularManipulated.getFile().getAbsolutePath());
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.close();
            fis.close();
            fos.close();
            Files.delete(templateFormularManipulated.getFile().toPath());
            FileOutputStream fileOutputStream2 = new FileOutputStream("target/classes/templates/templateformular.html");
            Files.copy(templateFormularOriginal.getFile().toPath(),fileOutputStream2);
        }catch (Exception e){
            e.printStackTrace();
        }

        return HttpStatus.OK;
    }
}
