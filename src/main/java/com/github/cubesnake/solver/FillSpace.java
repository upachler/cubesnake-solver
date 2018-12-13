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
public class FillSpace {

	boolean[] space;
	Vec bounds;
	Vec[] extent;
	int[] spaceIndexIncrements;
	
	FillSpace(Vec bounds) {
		this.bounds = bounds;
		Vec spaceBounds = bounds.multiply(2);
		spaceIndexIncrements = new int[bounds.length()];
		int spaceSize = spaceBounds.component(0);
		spaceIndexIncrements[0] = 1;
		for(int n=1; n<spaceBounds.length(); ++n) {
			spaceSize = spaceSize * spaceBounds.component(n);
			spaceIndexIncrements[n] = spaceIndexIncrements[n-1] * spaceBounds.component(n-1);
		}
		this.space = new boolean[spaceSize];
	}

	private FillSpace(boolean[] space, Vec bounds, Vec[] extent, int[] spaceIndexIncrements) {
		this.space = space;
		this.bounds = bounds;
		this.extent = extent;
		this.spaceIndexIncrements = spaceIndexIncrements;
	}
	
	public FillSpace copy() {
		return new FillSpace(Arrays.copyOf(space, space.length), bounds, extent, spaceIndexIncrements);
	}
	
	private static Vec[] expandExtent(Vec[] extent, Vec position) {
		if(extent == null) {
			return new Vec[] {position, position};
		} else {
			int[] componentsMin = new int[position.length()];
			int[] componentsMax = new int[position.length()];
			for(int n=0; n<position.length(); ++n) {
				componentsMin[n] = Math.min(extent[0].component(n), position.component(n));
				componentsMax[n] = Math.max(extent[1].component(n), position.component(n));
			}
			return new Vec[]{
				new Vec(componentsMin),
				new Vec(componentsMax)
			};
		}
	}
	
	boolean fillLine(Vec position, int direction, int length) {
		
		Vec unit = Vec.unitOfDirection(position.length(), direction);
		Vec line = unit.multiply(length-1);
		Vec[] newExtent = extent;
		newExtent = expandExtent(newExtent, position);
		newExtent = expandExtent(newExtent, position.plus(line));
		
		// check if the new extent would exceed the specified bounds
		if(!extentInBounds(newExtent, bounds)) {
			return false;
		}
		
		// check if cells on our path are clear
		for(int n=0; n<length; ++n) {
			if(isFilled(position.plus(unit.multiply(n)))) {
				return false;
			}
		}
		
		// all clear, we can fill
		extent = newExtent;
		for(int n=0; n<length; ++n) {
			fill(position.plus(unit.multiply(n)));
		}
			
		return true;
	}
	
	private static boolean extentInBounds(Vec[] extent, Vec bounds) {
		if(extent == null) {
			// non-existent extents fit into any bounds
			return true;
		}
		
		// component-wise check max-min on each axis is less than the bounds on that axis
		for(int n=0; n<bounds.length(); ++n) {
			if(extent[1].component(n) - extent[0].component(n) >= bounds.component(n)) {
				return false;
			}
		}
		return true;
	}
	
	private int spaceIndexOf(Vec position) {
		
		// we know that our actual space is twice the size of our bounds 
		// (see constructor), so we add the bounds as offset
		Vec centerOffset = bounds;
		Vec location = position.plus(centerOffset);
		
		int index = location.component(0);
		for(int n=1; n<location.length(); ++n) {
			index += location.component(n) * spaceIndexIncrements[n];
		}
		return index;
	}
	
	public void fill(Vec position) {
		space[spaceIndexOf(position)] = true;
	}
	
	public boolean isFilled(Vec position) {
		return space[spaceIndexOf(position)];
	}
}
