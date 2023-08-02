package com.salespage.salespageservice.domains.storages;

import com.salespage.salespageservice.domains.entities.StatisticCheckpoint;
import org.springframework.stereotype.Component;

@Component
public class StatisticCheckpointStorage extends BaseStorage{
  public StatisticCheckpoint findById(String transactionCheckpointId) {
    return statisticCheckpointRepository.findById(transactionCheckpointId).get();
  }

  public void save(StatisticCheckpoint statisticCheckpoint) {
    statisticCheckpointRepository.save(statisticCheckpoint);
  }
}
