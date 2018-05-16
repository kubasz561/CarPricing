package com.szymanowski.carpricing.services;

import com.ampl.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class LPService {

    @Autowired
    ApproximationStorage approximationStorage;

    public static void main(String[] args) {
        AMPL ampl = new AMPL();

        try {
            // Interpret the two filesC:\Users\Admin\Desktop\CarPricing\src\main\java\com\szymanowski\carpricing\services\LPService.java
            ampl.read("diet.mod");
            ampl.readData("diet.dat");

            // Solve
            ampl.solve();

            // Get objective entity by AMPL name
            Objective totalcost = ampl.getObjective("total_cost");
            // Print it
            System.out.format("Objective is: %f%n", totalcost.value());

            // Reassign data - specific instances
            Parameter cost = ampl.getParameter("cost");
            cost.setValues(new Object[] { "BEEF", "HAM" }, new double[] { 5.01, 4.55 });
            System.out.println("Increased costs of beef and ham.");

            // Resolve and display objective
            ampl.solve();
            System.out.format("New objective value: %f%n", totalcost.value());

            // Reassign data - all instances
            cost.setValues(new double[] { 3, 5, 5, 6, 1, 2, 5.01, 4.55 });
            System.out.println("Updated all costs");

            // Resolve and display objective
            ampl.solve();
            System.out.format("New objective value: %f%n", totalcost.value());

            // Get the values of the variable Buy in a dataframe object
            Variable buy = ampl.getVariable("Buy");
            DataFrame df = buy.getValues();
            // Print them
            System.out.println(df);

            // Get the values of an expression into a DataFrame object
            DataFrame df2 = ampl.getData("{j in FOOD} 100*Buy[j]/Buy[j].ub");
            // Print them
            System.out.println(df2);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            ampl.close();
        }
    }
}