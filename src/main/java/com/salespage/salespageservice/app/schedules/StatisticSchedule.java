package com.salespage.salespageservice.app.schedules;

import com.salespage.salespageservice.domains.services.TransactionStatisticService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class StatisticSchedule {

    @Autowired
    private TransactionStatisticService transactionStatisticService;

    @Scheduled(fixedDelay = 1000 * 30) //30s 1 lần
    public void asyncTransactionStatisticToday(){
        transactionStatisticService.statisticToday();
    }

    @Scheduled(fixedDelay = 1000 * 30) //30s 1 lần
    public void asyncTransactionStatisticPeriodDay(){
        transactionStatisticService.statisticPeriodDate();
    }

    @Scheduled(fixedDelay = 1000 * 30) //30s 1 lần
    public void asyncTransactionStatisticWeek(){
        transactionStatisticService.statisticWeek();
    }

    @Scheduled(fixedDelay = 1000 * 30) //30s 1 lần
    public void asyncTransactionStatisticPeriodWeek(){
        transactionStatisticService.statisticPeriodWeek();
    }

    @Scheduled(fixedDelay = 1000 * 30) //30s 1 lần
    public void asyncTransactionStatisticMonth(){
        transactionStatisticService.statisticMonth();
    }

    @Scheduled(fixedDelay = 1000 * 30) //30s 1 lần
    public void asyncTransactionStatisticPeriodMonth(){
        transactionStatisticService.statisticPeriodMonth();
    }

}
