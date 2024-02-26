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
        Subscription s1=new Subscription();
        Optional<User> user=userRepository.findById(subscriptionEntryDto.getUserId());
        if(user.isEmpty())
        {
            throw new RuntimeException("User Not found");
        }
        s1.setUser(user.get());
        s1.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        s1.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        s1.setStartSubscriptionDate(new Date());

        int amount=0;
        if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.BASIC))
        {
            amount=500+200*subscriptionEntryDto.getNoOfScreensRequired();
        }
        else if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.PRO))
        {
            amount=800+250*subscriptionEntryDto.getNoOfScreensRequired();
        }
        else{
            amount=1000+350*subscriptionEntryDto.getNoOfScreensRequired();
        }
        s1.setTotalAmountPaid(amount);
        Subscription subscription=subscriptionRepository.save(s1);

        user.get().setSubscription(subscription);
        return amount;
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
        if(s1.getSubscriptionType().equals(SubscriptionType.BASIC))
        {
            s1.setSubscriptionType(SubscriptionType.PRO);
            int num=s1.getNoOfScreensSubscribed();
            int curr=s1.getTotalAmountPaid();
            int newprice= 800+250*num;
            diffOfPrices=newprice-curr;
            s1.setTotalAmountPaid(newprice);
            Subscription s2=subscriptionRepository.save(s1);
            user.get().setSubscription(s1);

        }
        else if(s1.getSubscriptionType().equals(SubscriptionType.PRO))
        {
            s1.setSubscriptionType(SubscriptionType.ELITE);
            int num=s1.getNoOfScreensSubscribed();
            int curr=s1.getTotalAmountPaid();
            int newprice= 1000+350*num;
            diffOfPrices=newprice-curr;
            s1.setTotalAmountPaid(newprice);
            Subscription s2=subscriptionRepository.save(s1);
            user.get().setSubscription(s1);

            //totalamount to be modified and calculation of difference
        }
        else{
            throw new Exception("Already the best Subscription");
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
