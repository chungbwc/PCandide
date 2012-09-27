/**
 * you can put a one sentence description of your library here.
 *
 * ##copyright##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author		##author##
 * @modified	##date##
 * @version		##version##
 */

package pCandide;

import processing.core.PConstants;
import processing.core.PVector;
import processing.core.PApplet;

/**
 * This is a template class and can be used to start a new processing library or
 * tool. Make sure you rename this class as well as the name of the example
 * package 'template' to your own library or tool naming convention.
 * 
 * @example Hello
 * 
 *          (the tag @example followed by the name of an example included in
 *          folder 'examples' will automatically include the example in the
 *          javadoc.)
 * 
 */

public class PCandide implements PConstants {

	// myParent is a reference to the parent sketch
	private PApplet parent;
	private String[] wfm;
	private PVector[] vertices;
	private int[][] faces;
	private AnimationUnit[] auList;
	private int apply;

	private final static int CNT = 59;
	public final static String VERSION = "1.1.0";

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	public PCandide(PApplet _p, String _s) {
		parent = _p;
		wfm = parent.loadStrings(_s);
		parseWfm();
		apply = -1;
		welcome();
	}

	private void welcome() {
		System.out.println("PCandide 1.1.0 by Bryan Chung");
	}

	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	private void parseWfm() {
		auList = new AnimationUnit[CNT];
		int i = skipTo(0, "VERTEX LIST");
		if (i > -1) {
			String ln = PApplet.trim(wfm[i]);
			int n = Integer.parseInt(ln);
			vertices = new PVector[n];
			i++;
			for (int j = 0; j < n; j++) {
				String vStr = PApplet.trim(wfm[i]);
				String[] pt = PApplet.splitTokens(vStr, " ");
				vertices[j] = new PVector(0.0f, 0.0f, 0.0f);
				vertices[j].x = Float.parseFloat(pt[0]);
				vertices[j].y = Float.parseFloat(pt[1]);
				vertices[j].z = Float.parseFloat(pt[2]);
				i++;
			}
		}

		i = skipTo(i, "FACE LIST");
		if (i > -1) {
			String ln = PApplet.trim(wfm[i]);
			int n = Integer.parseInt(ln);
			faces = new int[n][3];
			i++;
			for (int j = 0; j < faces.length; j++) {
				String fStr = PApplet.trim(wfm[i]);
				String[] fs = PApplet.splitTokens(fStr, " ");
				faces[j] = new int[3];
				faces[j][0] = Integer.parseInt(fs[0]);
				faces[j][1] = Integer.parseInt(fs[1]);
				faces[j][2] = Integer.parseInt(fs[2]);
				i++;
			}
		}
		i = skipTo(i, "ANIMATION UNITS LIST");
		if (i < 0) {
			return;
		}
//		String ln = PApplet.trim(wfm[i]);
//		int n = Integer.parseInt(ln);
		i++;

		for (int j = 0; j < CNT; j++) {
			i = skipToAUV(i, "AUV");
			if (i < 0) {
				continue;
			}
			String ln2 = PApplet.trim(wfm[i]);
			int n2 = Integer.parseInt(ln2);
			i++;
			auList[j] = new AnimationUnit(n2);
			for (int k = 0; k < n2; k++) {
				String[] auStr = PApplet.splitTokens(PApplet.trim(wfm[i]), " ");
				int vt = Integer.parseInt(auStr[0]);
				float vx = Float.parseFloat(auStr[1]);
				float vy = Float.parseFloat(auStr[2]);
				float vz = Float.parseFloat(auStr[3]);
				auList[j].addTo(vt, new PVector(vx, vy, vz));
				i++;
			}
		}
	}

	public void applyAU(int _i) {
		apply = _i;
	}
	
	public void applyRandom() {
		apply = PApplet.floor(parent.random(CNT));
	}

	public void reset() {
		apply = -1;
	}

	public void render(float _f) {
		parent.pushMatrix();
		parent.scale(_f, -_f, _f);
		for (int i = 0; i < faces.length; i++) {
			parent.beginShape(TRIANGLES);
			for (int j = 0; j < 3; j++) {
				int vt = faces[i][j];
				PVector dis;
				if (apply == -1) {
					dis = new PVector(0.0f, 0.0f, 0.0f);
				} else {
					dis = auList[apply].getMotion(vt);
				}
				parent.vertex(vertices[vt].x + dis.x, vertices[vt].y + dis.y,
						vertices[vt].z + dis.z);
			}
			parent.endShape();
		}
		parent.popMatrix();
	}

	private int skipToAUV(int _s, String _p) {
		int i = _s;
		boolean found = false;
		while (i < wfm.length && !found) {
			String ln = PApplet.trim(wfm[i]);
			if (ln.equals("")) {
				i++;
				continue;
			}
			String token = wfm[i].substring(2, 5);
			if (token.equals(_p)) {
				found = true;
			}
			i++;
		}
		if (found) {
			return i;
		} else {
			return -1;
		}
	}

	private int skipTo(int _s, String _p) {
		int i = _s;
		boolean found = false;
		while (i < wfm.length && !found) {
			String ln = PApplet.trim(wfm[i]);
			if (ln.equals("")) {
				i++;
				continue;
			}
			char c1 = ln.charAt(0);
			char c2 = ln.charAt(ln.length() - 1);
			if (c1 == '#' && c2 == ':') {
				String p1 = ln.substring(2, ln.length() - 1);
				if (p1.equals(_p)) {
					found = true;
				}
			} else {
				i++;
				continue;
			}
			i++;
		}
		if (found) {
			return i;
		} else {
			return -1;
		}
	}
}
