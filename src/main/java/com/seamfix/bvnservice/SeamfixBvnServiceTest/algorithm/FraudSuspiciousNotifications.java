package com.seamfix.bvnservice.SeamfixBvnServiceTest.algorithm;

import java.util.List;

public class FraudSuspiciousNotifications {

    /**
     * Question 3
     * n = nunber of days
     * d = prior days of transactions

     */

    public int countDetectedAndSentFraudSuspiciousNotifications(List<Integer> expenditure, int n, int d){

        //check for edge cases first for efficiency

        //Are there zero number of days or are expendture lists empty?
        if(n == 0 || expenditure.size() == 0){
            return 0; // since there is nothing to calcualte from
        }
        //Are there enough prior days to send notifications?
        if (d > n || d == 0){
            return 0; // return zero since it's not possible to calcuate in these two cases
        }
        int numberOfNotifications = 0;
        List<Integer> priorDdaysExpenses = expenditure.subList(0,d);
        //sort to correctly get the median
        priorDdaysExpenses.sort((a,b)->a-b);
        int medianExp = 0;
        int medianIndex = priorDdaysExpenses.size()/2;
        if (priorDdaysExpenses.size()%2 ==0){ //Is it even numbers ?
            medianExp = (priorDdaysExpenses.get(medianIndex) + priorDdaysExpenses.get(medianIndex-1))/2;
        }
        else { // it must be odd
            medianExp = priorDdaysExpenses.get(medianIndex);
        }
        medianExp = 2 * medianExp; // according to the problem requirement

        for (int i = d; i < expenditure.size(); i++){
            if (medianExp <= expenditure.get(i)) //compare
                numberOfNotifications++; //increment
        }
        return numberOfNotifications;
    }
}
