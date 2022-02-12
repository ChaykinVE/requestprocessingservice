package ru.chaykin.microservapp.services;

import dto.requestservice.*;
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

    public CompletableFuture<GetRequestResponseDto> getRequest(GetRequestDto requestDto) {
        return CompletableFuture.completedFuture(requestDto)
                .thenCompose(requestService::getRequest);
    }

    public CompletableFuture<UpdateRequestResponseDto> updateRequest(UpdateRequestDto requestDto) {
        return CompletableFuture.completedFuture(requestDto)
                .thenCompose(requestService::updateRequest);
    }

    public CompletableFuture<DeleteRequestResponseDto> deleteRequest(DeleteRequestDto requestDto) {
        return CompletableFuture.completedFuture(requestDto)
                .thenCompose(requestService::deleteRequest);
    }
}
