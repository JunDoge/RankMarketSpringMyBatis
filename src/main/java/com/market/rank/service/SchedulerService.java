package com.market.rank.service;

import com.market.rank.mapper.usr.UsrProcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final UsrProcMapper usrProcMapper;

    @Scheduled(cron = "0 0 0 * * ?")
    public void run(){
        usrProcMapper.autoWinSave();
    }
}
