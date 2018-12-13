/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cubesnake.solver;

import java.util.Arrays;

/**
 *
 * @author count
 */
public class Main {
	public static void main(String[] args) {
		Solver s = new Solver();
		s.solve();
		for(int[] turns : s.solutions) {
			System.out.println(Arrays.toString(turns));
		}
	}
}
