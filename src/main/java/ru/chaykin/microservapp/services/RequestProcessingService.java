package ru.chaykin.microservapp.services;

import dto.requestservice.CreateRequestDto;
import dto.requestservice.CreateRequestResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class RequestProcessingService {

    private final RequestService requestService;

    public CompletableFuture<CreateRequestResponseDto> createRequest(CreateRequestDto requestDto) {
        return CompletableFuture.completedFuture(requestDto)
                .thenCompose(requestService::createRequest);
    }

}
