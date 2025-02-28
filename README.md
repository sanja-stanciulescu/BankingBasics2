# Project Assignment POO  - Banking Basic - Phase Two

<div align="center"><img src="https://media1.tenor.com/m/l2KqipO86aAAAAAd/banking-that.gif" width="500px" alt=""></div>

## Personal Overview
This project is a complex banking system built to simulate various banking operations.

It follows object-oriented programming principles and, in addition to the latest project, uses design patterns, such as
Strategy and Factory.

## Skel Structure

* src/
    * accounts/
        * ClassicAccount - contains the standard accounts
        * SavingsAccount - contains the account that are of type savings and have the additional interestRate
        * <strong>BusinessAccount</strong> - contains the shared accounts inside of a company
    * app/
        * AppManager - contains the methods that handle the entire workflow of the project
        * Finder - helper class used for optimizing the search for users, accounts or cards
        * IBANRegistry - contains all the IBANs associated with themselves or an alias
    * <striong>business/</strong>
        * BusinessCommerciant - is the class that stores the information about the commerciant the employees have interacted with
        * Employee - tracks all the actions an employee has made within the business
        * Manager - managers have a lot more freedom, but they still need to be monitored
        * Owner - king over them all; they have all the permissions
    * cards/
        * Card - contains the methods for a standard card, with unlimited payment possibilities
        * OneTimeCard - contains the methods for an one-time card, that will be replaced after each payment
    * checker/ - checker files
    * <strong>commerciants/</strong>
        * CashbackStrategy - the interface for the Strategy design pattern 
        * Commerciant - contains the list of payments made to easily create Spending Reports
        * AmountThresholdCashback - here is the logic for giving users their money back in case of a 'spendingThreshold' commerciant
        * NumberOfTransactions - here is the logic for giving users their money back in case of a 'numberOfTransactions' commerciant; they both implement the strategy dp
        * Seller - this is the class that contains all the information about a commerciant, but also processes the possible cashback methods
    * exchangeRates/
       * Bnr - the Romanian National Bank where all exchange rates are shown in real time
       * ExchangeRate - contains the methods for managing rates from the BNR
       * ExchangeRateNode - class used in the conversion from one currency to the other
    * fileio/ - contains classes used to read data from the json files
      * main/
       * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
       * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
         to the out.txt file. Thus, you can compare this result with ref.
    * <strong>servicePlan/</strong>
       * Plan - the interface for the service plans
       * PlanFactory - the class for the Factory design pattern
       * StandardPlan - the basic plan that all users (who are not students) start with
       * StudentPlan - students can receive different perks through this service plan
       * SilverPlan and GoldPlan - the premium plans that can be bought through a certain fee
    * transactions/
       * Transaction - the interface that will be further implemented to create any time of transaction that should be available in the banking system
       * Contains maaany classes for each transaction
    * users/
       * User - contains the particular fields for a user
* input/ - contains the tests in JSON format
* ref/ - contains all reference output for the tests in JSON format

## Work flow

The flow of the banking system is designed to manage multiple banking transactions and user commands.

Here's how the operations generally unfold:
1. User initialization: users are instantiated, without any accounts or cards at the beginning;
2. Transaction Execution: users can execute various commands in the following way:
    * using the Factory design pattern, a new transaction object is instantiated;
    * using the Strategy design pattern, the output or other classes will suffer different changes, depending on the transaction
    * the array of commands is parsed until there are no command left;
3. Other design patterns that were used through the project:
    * Factory: for the creation and assignation of the service plans;
    * Strategy: for applying the wanted cashback depending on each commerciant;
    * Command: used for keeping track of the splitPayments and their accept / reject status;
    * Adapter: because the main logic is implemented with TransactionStrategy objects, the Command objects would have been incompatible, so, to solve this problem, i used this design pattern;
4. One of the hardest methods to code was the PayOnlineTransaction, and that can be seen in the sheer size of the method.

## Key features

* Users can manage their personal details and accounts, including multiple types of accounts or cards;
* There are many real-life functionalities developed within the project, so the entire assignment was a good practice ;
* The system provides error messages for invalid operations in the output file, as well as in the user or transaction reports.
* The system is built with extensibility in mind. The TransactionStrategy interface allows the introduction of new transactions types with minimal impact on the overall system.

## Key Takeaways

1. This project deepened my understanding of object-oriented programming, particularly in managing complex data structures like users, accounts, and transactions.
2. I applied design patterns, especially in terms of transaction handling and error management, which made the system modular and easier to extend.
3. Developing this project helped me better understand how banking systems function and the complexities involved in managing multiple accounts, currencies, and transactions.
4. Because of the focus on expanding a already made project, I realized that I actually implemented a scalable project in the first part, which made me proud of myself.
5. Because of the faulty tests, I also learned how to handle confusing sets of inputs and refs.


