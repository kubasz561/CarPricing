package com.szymanowski.carpricing.services;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import lpsolve.*;
@Service
public class LPService {

    @Autowired
    ApproximationStorage approximationStorage;

    public static void main(String[] args) {


        //describe the optimization problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] { 3, 5}, 0);

        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new double[] { 2, 8}, Relationship.LEQ, 13));
        constraints.add(new LinearConstraint(new double[] { 5, -1}, Relationship.LEQ, 11));

        constraints.add(new LinearConstraint(new double[] { 1, 0}, Relationship.GEQ, 0));
        constraints.add(new LinearConstraint(new double[] { 0, 1}, Relationship.GEQ, 0));

        //create and run solver
        PointValuePair solution = null;
        solution = new SimplexSolver().optimize(f, new LinearConstraintSet(constraints), GoalType.MAXIMIZE);

        if (solution != null) {
            //get solution
            double max = solution.getValue();
            System.out.println("Opt: " + max);

            //print decision variables
            for (int i = 0; i < 2; i++) {
                System.out.print(solution.getPoint()[i] + "\t");
            }
        }
    }/*

*//* demo.

    public int execute() throws LpSolveException {
        LpSolve lp;
        int Ncol, j, ret = 0;

          /* We will build the model row by row
             So we start with creating a model with 0 rows and 2 columns *//*
        Ncol = 2; *//* there are two variables in the model *//*

        *//* create space large enough for one row *//*
        int[] colno = new int[Ncol];
        double[] row = new double[Ncol];

        lp = LpSolve.makeLp(0, Ncol);
        if(lp.getLp() == 0)
            ret = 1; *//* couldn't construct a new model... *//*

        if(ret == 0) {
            *//* let us name our variables. Not required, but can be useful for debugging *//*
            lp.setColName(1, "x");
            lp.setColName(2, "y");

            lp.setAddRowmode(true);  *//* makes building the model faster if it is done rows by row *//*

            *//* construct first row (120 x + 210 y <= 15000) *//*
            j = 0;

            colno[j] = 1; *//* first column *//*
            row[j++] = 120;

            colno[j] = 2; *//* second column *//*
            row[j++] = 210;

            *//* add the row to lpsolve *//*
            lp.addConstraintex(j, row, colno, LpSolve.LE, 15000);
        }

        if(ret == 0) {
            *//* construct second row (110 x + 30 y <= 4000) *//*
            j = 0;

            colno[j] = 1; *//* first column *//*
            row[j++] = 110;

            colno[j] = 2; *//* second column *//*
            row[j++] = 30;

            *//* add the row to lpsolve *//*
            lp.addConstraintex(j, row, colno, LpSolve.LE, 4000);
        }

        if(ret == 0) {
            *//* construct third row (x + y <= 75) *//*
            j = 0;

            colno[j] = 1; *//* first column *//*
            row[j++] = 1;

            colno[j] = 2; *//* second column *//*
            row[j++] = 1;

            *//* add the row to lpsolve *//*
            lp.addConstraintex(j, row, colno, LpSolve.LE, 75);
        }

        if(ret == 0) {
            lp.setAddRowmode(false); *//* rowmode should be turned off again when done building the model *//*

            *//* set the objective function (143 x + 60 y) *//*
            j = 0;

            colno[j] = 1; *//* first column *//*
            row[j++] = 143;

            colno[j] = 2; *//* second column *//*
            row[j++] = 60;

            *//* set the objective in lpsolve *//*
            lp.setObjFnex(j, row, colno);
        }

        if(ret == 0) {
            *//* set the object direction to maximize *//*
            lp.setMaxim();

            *//* just out of curioucity, now generate the model in lp format in file model.lp *//*
            lp.writeLp("model.lp");

            *//* I only want to see important messages on screen while solving *//*
            lp.setVerbose(LpSolve.IMPORTANT);

            *//* Now let lpsolve calculate a solution *//*
            ret = lp.solve();
            if(ret == LpSolve.OPTIMAL)
                ret = 0;
            else
                ret = 5;
        }

        if(ret == 0) {
            *//* a solution is calculated, now lets get some results *//*

            *//* objective value *//*
            System.out.println("Objective value: " + lp.getObjective());

            *//* variable values *//*
            lp.getVariables(row);
            for(j = 0; j < Ncol; j++)
                System.out.println(lp.getColName(j + 1) + ": " + row[j]);

            *//* we are done now *//*
        }

        *//* clean up such that all used memory by lpsolve is freed *//*
        if(lp.getLp() != 0)
            lp.deleteLp();

        return(ret);
    }

    *//*public static void main(String[] args) {
        try {
            new Demo().execute();
        }
        catch (LpSolveException e) {
            e.printStackTrace();
        }
    }*/
}