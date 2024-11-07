package com.eventostec.api.services;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.config.AWSConfig;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.repositories.EventRepository;
import com.sun.jdi.request.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    public AmazonS3 s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;


    public Event createEvent(EventRequestDTO data) {
        String imgUrl = null;

        //assim, recuperamos a URL da imagem.
        if (data.image() != null) {
            imgUrl = this.uploadImg(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.descripton());
        newEvent.setEvent_url(data.eventUrl());
        //pega o Date em long que veio do frontend e transforma em Date
        newEvent.setDate(new Date(data.date()));
        newEvent.setImg_url(imgUrl);

        return newEvent;
    }

    private String uploadImg(MultipartFile multipartFile) {
        //nome que a imagem será salva
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        //conversao de arquivo para um File de verdade
        try {
            File file = this.convertMultipartToFile(multipartFile);
            s3Client.putObject(bucketName, fileName, file);

            //deleta esse file de cima (arquivo temporário para fazer o upload)
            file.delete();

            //pegando a url criada
            return s3Client.getUrl(bucketName, fileName).toString();

        } catch (Exception e) {
            System.out.println("erro ao subir arquivo");
            return null;
        }
    }

    //pegamos o que recebemos no request, criamos um arquivo local na máquina
    private File convertMultipartToFile(MultipartFile multipartFile) throws Exception {
        //dessa forma ele terá certeza que o objeto não será nulo
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();

        return convFile;
    }
}
