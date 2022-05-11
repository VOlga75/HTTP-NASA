import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.misc.JavaNetUriAccess;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=OI4rupXiCPEidpj5Y50EzqfC2gCsUn3lMuvAjd77";
    public static final ObjectMapper mapper = new ObjectMapper();//Объект для де/сериализации json

    public static void main(String[] args) throws IOException, InterruptedException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
              //  .setUserAgent("My Test Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
// создание объекта запроса с произвольными заголовками
        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
// отправка запроса
        CloseableHttpResponse response = httpClient.execute(request);
// вывод полученных заголовков
   //    Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
//// чтение тела ответа
        ////String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        //// System.out.println(body);
        PostNASA postNasa = mapper.readValue(response.getEntity().getContent(), new TypeReference<PostNASA>() {
        });
        System.out.println(postNasa.getUrl()) ;
        String newURL;
        if (postNasa.getMedia_type()!=null && postNasa.getMedia_type().equals("image")) {
            newURL = postNasa.getUrl();
        } else {
            String[] s = postNasa.getUrl().split("/");
            String s1 = s[s.length-1];
            String[] res = s1.split("\\?");
            newURL = "https://img.youtube.com/vi/" + res[0] + "/default.jpg";
        }

        HttpGet requestContent = new HttpGet(newURL);
        requestContent.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse responseContent = httpClient.execute(requestContent);
        int status = responseContent.getStatusLine().getStatusCode();
        if (status >= 200 && status <300 ) {
            System.out.printf("Отлично! Статус  запроса %s", responseContent.getStatusLine().getStatusCode());

            InputStream inputStream = responseContent.getEntity().getContent();

            String fileName = postNasa.getFileName();
            FileOutputStream fos = new FileOutputStream("d:\\" + fileName);

            int i;
            while ((i = inputStream.read()) != -1) {
                fos.write(i);
            }
            fos.close();
            System.out.printf("\nФайл %s c %s сохранен на диске D:\\", fileName, newURL);
            System.out.println("Пробуем отобразить через 4 секунды");
            Thread.sleep(4000);

       /* FileInputStream fis = new FileInputStream("d:\\"+fileName);
        while((i=fis.read())!= -1){
            System.out.print((char)i);
        }*/ //так некрасиво...
            File jpgFile = new File("d:\\" + fileName);
            if (jpgFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(jpgFile);
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }

            } else {
                System.out.println("File is not exists!");
            }
        } else {
            System.out.printf ("Статус %s. Что-то пошло не так!", status);
        }

        responseContent.close();
        response.close();
        httpClient.close();
    }
}

