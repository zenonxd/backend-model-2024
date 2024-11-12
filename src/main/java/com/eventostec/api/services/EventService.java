package com.eventostec.api.services;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.config.AWSConfig;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.domain.event.EventWithAddressDTO;
import com.eventostec.api.repositories.AddressRepository;
import com.eventostec.api.repositories.EventRepository;
import com.sun.jdi.request.EventRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    public AmazonS3 s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;


    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        String imgUrl = null;

        //assim, recuperamos a URL da imagem.
        if (eventRequestDTO.image() != null) {
            imgUrl = this.uploadImg(eventRequestDTO.image());
        }

        //instanciando um Adress e settando o que vamos usar
        Address address = new Address();
        address.setCity(eventRequestDTO.city());
        address.setUf(eventRequestDTO.state());
        //persistindo no banco
        addressRepository.save(address);

        //criando Event, settando os dados do requestDTO
        Event newEvent = new Event();
        newEvent.setTitle(eventRequestDTO.title());
        newEvent.setDescription(eventRequestDTO.description());
        newEvent.setEvent_url(eventRequestDTO.eventUrl());
        //pega o Date em long que veio do frontend e transforma em Date
        newEvent.setDate(new Date(eventRequestDTO.date()));
        newEvent.setImg_url(imgUrl);
        newEvent.setRemote(eventRequestDTO.remote());

        //descobrir pq isso ta dando erro
        eventRepository.save(newEvent);

        return new EventResponseDTO(
                newEvent.getId(),
                newEvent.getTitle(),
                newEvent.getDescription(),
                newEvent.getDate(),
                newEvent.getAddress().getCity(),
                newEvent.getAddress().getUf(),
                newEvent.getRemote(),
                newEvent.getEvent_url(),
                newEvent.getImg_url()
        );
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

    public Page<EventResponseDTO> findAllPaged(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);


        return events.map(event -> {

            return new EventResponseDTO(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getAddress() != null ? event.getAddress().getCity() : "",
                    event.getAddress() != null ? event.getAddress().getUf() : "",
                    event.getRemote(),
                    event.getEvent_url(),
                    event.getImg_url()
            );
        });
    }

    public Page<EventWithAddressDTO> getUpComingEvents(LocalDate currentDate, Pageable pageable) {
        Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Page<Event> upcomingEvents = eventRepository.findUpcomingEvents(date, pageable);

        return upcomingEvents.map(event -> {


            return new EventWithAddressDTO(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getAddress() != null ? event.getAddress().getCity() : "",
                    event.getAddress() != null ? event.getAddress().getUf() : "",
                    event.getRemote(),
                    event.getEvent_url(),
                    event.getImg_url()
            );
        });
    }

    public Page<EventResponseDTO> findEventsWithFilters(String titulo, String city, String uf,
                                                        Date startDate, Date endDate,
                                                        Pageable pageable) {
        titulo = (titulo != null) ? titulo : "";
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        //se data inicial for nula começará em 1970
        startDate = (startDate != null) ? startDate : new Date(0);
        //se data final for nula começará agora
        endDate = (endDate != null) ? endDate : new Date();

        Page<Event> events = eventRepository.findAllEventsByFilters(titulo, city, uf, startDate, endDate, pageable);

        return events.map(event -> {

            return new EventResponseDTO(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getAddress() != null ? event.getAddress().getCity() : "",
                    event.getAddress() != null ? event.getAddress().getUf() : "",
                    event.getRemote(),
                    event.getEvent_url(),
                    event.getImg_url()
            );
        });
    }
}
