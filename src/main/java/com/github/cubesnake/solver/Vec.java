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
public class Vec {
	private final int[] components;
	
	
	public static Vec zero(int numComponents) {
		return new Vec(new int[numComponents]);
	}
	
		
	public static final Vec unitOfAxis(int numComponents, int axis) {
		return unitOfAxisImpl(numComponents, axis, 1);
	}
	
	private static Vec unitOfAxisImpl(int numComponents, int axis, int unitComponent) {
		int[] cmp = new int[numComponents];
		cmp[axis] = unitComponent;
		return new Vec(cmp);
	}
	
	
	public static final Vec unitOfDirection(int numComponents, int direction) {
		int axis = axisOfDirection(direction);
		return Vec.unitOfAxisImpl(numComponents, axis, Integer.signum(direction));
	}

	
	public static int axisOfDirection(int direction) {
		return Math.abs(direction)-1;
	}
	
	public static int directionOnAxis(int axis, boolean backwards) {
		return (axis + 1) * (backwards ? -1 : 1);
	}
	
	public Vec(int... components) {
		this.components = components;
	}
	
	public final Vec plus(Vec increment) {
		checkSameDimensions(increment);
		int[] result = new int[components.length];
		for(int n=0; n<components.length;++n) {
			result[n] = components[n]+increment.components[n];
		}
		return new Vec(result);
	}
	
	public final Vec neg() {
		int[] result = new int[components.length];
		for(int n=0; n<components.length; ++n) {
			result[n] = -components[n];
		}
		return new Vec(result);
	}
	
	public int length() {
		return components.length;
	}
	
	private void checkSameDimensions(Vec increment) {
		if(components.length != increment.components.length) {
			throw new IllegalArgumentException("vectors must have the same dimension (number of components) for this operation");
		}
	}

	public Vec multiply(int i) {
		int[] result = new int[components.length];
		for(int n=0; n<result.length; ++n) {
			result[n] = components[n] * i;
		}
		return new Vec(result);
	}

	int component(int i) {
		return components[i];
	}

	@Override
	public String toString() {
		return "Vec{" + "components=" + Arrays.toString(components) + '}';
	}
	
	
}
