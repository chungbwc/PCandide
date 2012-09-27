package pCandide;

import processing.core.PVector;

public class AnimationUnit {
	private PVector[] motions;
	private int[] nodes;
	private int idx;

	public AnimationUnit(int _n) {
		motions = new PVector[_n];
		nodes = new int[_n];
		for (int i = 0; i < motions.length; i++) {
			motions[i] = new PVector(0.0f, 0.0f, 0.0f);
			nodes[i] = -1;
		}
		idx = 0;
	}

	public void addTo(int _n, PVector _v) {
		if (idx < motions.length) {
			nodes[idx] = _n;
			motions[idx].x = _v.x;
			motions[idx].y = _v.y;
			motions[idx].z = _v.z;
			idx++;
		}
	}

	public int getSize() {
		return nodes.length;
	}

	public PVector getMotion(int _i) {
		int temp = -1;
		for (int i = 0; i < nodes.length; i++) {
			if (_i == nodes[i]) {
				temp = i;
				break;
			}
		}
		if (temp > -1) {
			return motions[temp];
		} else {
			return new PVector(0.0f, 0.0f, 0.0f);
		}
	}
}
