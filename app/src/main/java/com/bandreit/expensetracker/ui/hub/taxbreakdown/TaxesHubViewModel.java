package com.bandreit.expensetracker.ui.hub.taxbreakdown;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TaxesHubViewModel extends ViewModel {

    private Double suAmount;
    private Double salaryAmount;
    public MutableLiveData<Double> incomeSuA;
    public MutableLiveData<Double> incomeSalaryA;
    public MutableLiveData<Double> incomeSuB;
    public MutableLiveData<Double> incomeSalaryB;
    public MutableLiveData<Double> taxIncomeA;
    public MutableLiveData<Double> taxIncomeB;
    private final int fradrag = 4548;

    public TaxesHubViewModel() {
        suAmount = (double) 0;
        salaryAmount = (double) 0;
        incomeSuA = new MutableLiveData<>((double) 0);
        incomeSalaryA = new MutableLiveData<>((double) 0);
        incomeSuB = new MutableLiveData<>((double) 0);
        incomeSalaryB = new MutableLiveData<>((double) 0);
        taxIncomeA = new MutableLiveData<>((double) 0);
        taxIncomeB = new MutableLiveData<>((double) 0);

    }

    public void setSuAmount(Double suAmount) {
        incomeSuA.setValue(((suAmount * 0.92) - 4548) * 0.62 + fradrag);
        incomeSuB.setValue(suAmount * 0.62);
        setTotalIncome();
    }

    public void setSalaryAmount(Double salaryAmount) {
        incomeSalaryA.setValue(((salaryAmount * 0.92) - 4548) * 0.62 + fradrag);
        incomeSalaryB.setValue(salaryAmount * 0.62);
        setTotalIncome();
    }

    private void setTotalIncome() {
        taxIncomeA.setValue(incomeSalaryA.getValue() + incomeSuB.getValue());
        taxIncomeB.setValue(incomeSuA.getValue() + incomeSalaryB.getValue());
    }

}
