package com.zuzex.carshowroom.service;

public interface RollbackService {

    void acceptCarOrderChanges(Long carId);

    void declineCarOrderChanges(Long carId);
}
