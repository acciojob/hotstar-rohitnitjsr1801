package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

//        return null;
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        SubscriptionType subscriptionType = subscription.getSubscriptionType();
        int noOfScreen = subscription.getNoOfScreensSubscribed();

        int priceOdSubscription = 0;

        if(subscriptionType.equals(SubscriptionType.BASIC)){
            priceOdSubscription = 500 + (200 * noOfScreen);
        }else if(subscriptionType.equals(SubscriptionType.PRO)){
            priceOdSubscription = 800 + (250 * noOfScreen);
        }else{
            priceOdSubscription = 1000 + (350 * noOfScreen);
        }
        subscription.setTotalAmountPaid(priceOdSubscription);
        subscription.setUser(user);
        Date date = new Date();
        subscription.setStartSubscriptionDate(date);

        user.setSubscription(subscription);

        return subscription.getTotalAmountPaid();
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User> user=userRepository.findById(userId);
        if(user.isEmpty())
        {
            return 0;
        }
        Optional<Subscription> subscriber=subscriptionRepository.findByUser(user.get());
        Subscription s1=subscriber.get();
        int diffOfPrices=0;
        int num=s1.getNoOfScreensSubscribed();
        int curr=s1.getTotalAmountPaid();
        if(s1.getSubscriptionType().equals(SubscriptionType.ELITE))
        {
            throw new Exception("Already the best Subscription");
        }
        else if(s1.getSubscriptionType().equals(SubscriptionType.BASIC))
        {
            s1.setSubscriptionType(SubscriptionType.PRO);
            int newprice= 800+250*num;
            diffOfPrices=newprice-curr;
            s1.setTotalAmountPaid(newprice);
            user.get().setSubscription(s1);
            Subscription s2=subscriptionRepository.save(s1);
           }
        else if(s1.getSubscriptionType().equals(SubscriptionType.PRO))
        {
            s1.setSubscriptionType(SubscriptionType.ELITE);
            int newprice= 1000+350*num;
            diffOfPrices=newprice-curr;
            s1.setTotalAmountPaid(newprice);
            user.get().setSubscription(s1);
            Subscription s2=subscriptionRepository.save(s1);
            //totalamount to be modified and calculation of difference
        }

        return diffOfPrices;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int revenue=0;
        List<Subscription> totalsubs=subscriptionRepository.findAll();
        for(Subscription subscription : totalsubs){
            revenue += subscription.getTotalAmountPaid();
        }
        return revenue;
    }

}
