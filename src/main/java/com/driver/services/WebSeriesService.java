package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        Optional<WebSeries> exists= Optional.ofNullable(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()));
        if(!exists.isEmpty())
        {
            throw new Exception("Series is already present");
        }
        WebSeries webseries=new WebSeries();
        webseries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webseries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webseries.setRating(webSeriesEntryDto.getRating());
        webseries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        Optional<ProductionHouse> p1=productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        webseries.setProductionHouse(p1.get());
        WebSeries obj1=webSeriesRepository.save(webseries);
        p1.get().setRatings(webSeriesEntryDto.getRating());
        ProductionHouse p2=productionHouseRepository.save(p1.get());
        return obj1.getId();
    }

}
