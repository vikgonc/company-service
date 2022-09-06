package com.zuzex.carshowroom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzex.carshowroom.model.Model;
import com.zuzex.carshowroom.service.ModelService;
import com.zuzex.common.dto.ErrorMessageDto;
import com.zuzex.common.dto.ModelDto;
import com.zuzex.common.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.zuzex.common.util.ResponseConstant.MODEL_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(ModelController.class)
public class ModelControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ErrorMessageDto errorMessageDto = new ErrorMessageDto(MODEL_NOT_FOUND);
    private final Model okResponse = Model.builder()
            .id(1L)
            .brand("Mercedes")
            .modelName("AMG")
            .description("Premium auto")
            .build();

    @MockBean
    private ModelService modelService;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void findAllModelsTest() throws JsonProcessingException {
        when(modelService.findAllModels())
                .thenReturn(Flux.just(okResponse));

        webClient
                .get()
                .uri("/models")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("[" + objectMapper.writeValueAsString(okResponse) + "]");

        when(modelService.findAllModels())
                .thenReturn(Flux.empty());

        webClient
                .get()
                .uri("/models")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json("[]");
    }

    @Test
    public void findModelByIdTest() throws JsonProcessingException {
        when(modelService.findModelById(anyLong()))
                .thenReturn(Mono.just(okResponse));

        webClient
                .get()
                .uri("/models/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(okResponse));

        when(modelService.findModelById(anyLong()))
                .thenThrow(new NotFoundException(MODEL_NOT_FOUND));

        webClient
                .get()
                .uri("/models/1")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .json(objectMapper.writeValueAsString(errorMessageDto));

        webClient
                .get()
                .uri("/models/qwe")
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody();
    }

    @Test
    public void createNewModelTest() throws JsonProcessingException {
        when(modelService.createNewModel(any(ModelDto.class)))
                .thenReturn(Mono.just(okResponse));

        webClient
                .post()
                .uri("/models/create")
                .bodyValue(new ModelDto())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(okResponse));

        webClient
                .post()
                .uri("/models/create")
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody();
    }
}
