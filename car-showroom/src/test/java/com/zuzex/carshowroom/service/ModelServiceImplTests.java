package com.zuzex.carshowroom.service;

import com.zuzex.carshowroom.mapper.ModelMapper;
import com.zuzex.carshowroom.model.Model;
import com.zuzex.carshowroom.repository.ModelRepository;
import com.zuzex.carshowroom.service.Impl.ModelServiceImpl;
import com.zuzex.common.dto.ModelDto;
import com.zuzex.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.zuzex.common.util.ResponseConstant.MODEL_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModelServiceImplTests {

    @Mock
    private ModelRepository modelRepository;

    private ModelService modelService;
    private final Model response = Model.builder()
            .id(1L)
            .brand("Mercedes")
            .modelName("AMG")
            .description("Premium auto")
            .build();

    @BeforeAll
    void init() {
        MockitoAnnotations.openMocks(this);
        modelService = new ModelServiceImpl(modelRepository, new ModelMapper());
    }

    @Test
    public void findAllModelsTest() {
        when(modelRepository.findAll())
                .thenReturn(Flux.just(response));

        StepVerifier
                .create(modelService.findAllModels())
                .expectSubscription()
                .expectNext(response)
                .verifyComplete();

        when(modelRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier
                .create(modelService.findAllModels())
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void findModelByIdTest() {
        when(modelRepository.findById(anyLong()))
                .thenReturn(Mono.just(response));

        StepVerifier
                .create(modelService.findModelById(1L))
                .expectSubscription()
                .expectNext(response)
                .verifyComplete();

        when(modelRepository.findById(anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier
                .create(modelService.findModelById(1L))
                .expectSubscription()
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException
                        && throwable.getMessage().equals(MODEL_NOT_FOUND))
                .verify();
    }

    @Test
    public void createNewModelTest() {
        when(modelRepository.save(any(Model.class)))
                .thenReturn(Mono.just(response));

        StepVerifier
                .create(modelService.createNewModel(new ModelDto()))
                .expectSubscription()
                .expectNext(response)
                .verifyComplete();
    }
}
