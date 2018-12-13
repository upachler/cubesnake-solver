/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cubesnake.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author count
 */
public class Solver {
	
	// from the 3x3 image from
	// https://myshumidotnet.files.wordpress.com/2011/10/snake_cube_schematic_3x3x3_4x4x4-scaled1000.png
	static final int[] segments = {
		3,1,1,2,1,2,1,1,2,2,1,1,1,2,2,2,2
	};
	
	List<int[]> solutions;
	
	public void solve() {
		if(solutions != null) {
			throw new IllegalStateException("already solved.");
		}
		solutions = new ArrayList<>();
		FillSpace fs = new FillSpace(new Vec(3,3,3));
		checkTurn(0, 1, new int[segments.length], fs, new Vec(-1, 0, 0));
		
	}
	/**
	 * 
	 * @param turn	the current turn (segment) that we're checking the direction
	 * for
	 * @param direction	the direction we're attempting for this turn. The direction
	 *	is the ±(axis + 1). The axis itself runs from 0..n-1 for n dimensions. 
	 *	The ± sign indicates that the direction can be positive or negative, which
	 *	specifies whether we're going forward or backward on the underlying axis.
	 * @param turns
	 * @param fs
	 * @param position
	 */
	void checkTurn(int turn, int direction, int[] turns, FillSpace fs, Vec position) {
		
		int length = segments[turn];
		
		fs = fs.copy();
		turns = Arrays.copyOf(turns, turns.length);
		
		Vec startPosition = position.plus(Vec.unitOfDirection(position.length(), direction));
		boolean filledWithoutObjstruction = fs.fillLine(startPosition, direction, length);
		if(filledWithoutObjstruction) {
			turns[turn] = direction;
			if(turn+1 == segments.length) {
				solutions.add(turns);
			} else {
				int dimensions = position.length();
				int currentAxis = Vec.axisOfDirection(direction);
				// attempt all perpendiculars of the current direction with the next turn
				Vec target = Vec.unitOfDirection(dimensions, direction).multiply(length-1);
				Vec endPosition = startPosition.plus(target);
				for(int n=1; n<endPosition.length(); ++n) {
					int axis = (currentAxis + n) % dimensions;
					
					int forwardDirection = Vec.directionOnAxis(axis, true);
					checkTurn(turn+1, forwardDirection, turns, fs, endPosition);
					
					int backwardDirection = Vec.directionOnAxis(axis, false);
					checkTurn(turn+1, backwardDirection, turns, fs, endPosition);
				}
			}
		}
	}
}
