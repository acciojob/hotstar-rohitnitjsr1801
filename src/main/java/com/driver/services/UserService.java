package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){
        User user1=userRepository.save(user);
        //Jut simply add the user to the Db and return the userId returned by the repository
        return user1.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        Optional<User> user=userRepository.findById(userId);
        if(user.isEmpty())
        {
            throw new RuntimeException("User Id invalid");
        }
        User u1=user.get();
        Subscription s1=u1.getSubscription();
        List<WebSeries> webseries=webSeriesRepository.findByAgeLimitAndSubscriptionType(u1.getAge(),s1.getSubscriptionType());
        return webseries.size();
    }


}
