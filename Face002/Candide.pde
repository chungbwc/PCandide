class Candide {

  private final int CNT = 59;
  private String [] wfm;
  private PVector [] vertices;
  private int [][] faces;
  private AnimationUnit [] auList;
  private int apply;

  public Candide(String _s) {
    wfm = loadStrings(_s);
    parseWfm();
    apply = -1;
    //   println((vertices[69].x+vertices[70].x+vertices[73].x+vertices[74].x)/4);
    //     (vertices[68].y+vertices[69].y+vertices[71].y+vertices[72].y)/4);
  }

  private void parseWfm() {
    auList = new AnimationUnit[CNT];
    int i = skipTo(0, "VERTEX LIST");
    if (i > -1) {
      String ln = trim(wfm[i]);
      int n = parseInt(ln);
      vertices = new PVector[n];
      i++;
      for (int j=0; j<n; j++) {
        String vStr = trim(wfm[i]);
        String [] pt = splitTokens(vStr, " ");
        vertices[j] = new PVector(0.0, 0.0, 0.0);
        vertices[j].x = parseFloat(pt[0]);
        vertices[j].y = parseFloat(pt[1]);
        vertices[j].z = parseFloat(pt[2]);
        i++;
      }
    }

    i = skipTo(i, "FACE LIST");
    if (i > -1) {
      String ln = trim(wfm[i]);
      int n = parseInt(ln);
      faces = new int[n][3];
      i++;
      for (int j=0; j<faces.length; j++) {
        String fStr = trim(wfm[i]);
        String [] fs = splitTokens(fStr, " ");
        faces[j] = new int[3];
        faces[j][0] = parseInt(fs[0]);
        faces[j][1] = parseInt(fs[1]);
        faces[j][2] = parseInt(fs[2]);
        i++;
      }
    }
    i = skipTo(i, "ANIMATION UNITS LIST");
    if (i < 0) {
      return;
    }
    String ln = trim(wfm[i]);
    int n = parseInt(ln);
    i++;

    for (int j=0; j<CNT; j++) {
      i = skipToAUV(i, "AUV");
      if (i < 0) {
        continue;
      }
      String ln2 = trim(wfm[i]);
      int n2 = parseInt(ln2);
      i++;
      auList[j] = new AnimationUnit(n2);
      for (int k=0; k<n2; k++) {
        String [] auStr = splitTokens(trim(wfm[i]), " ");
        int vt = parseInt(auStr[0]);
        float vx = parseFloat(auStr[1]);
        float vy = parseFloat(auStr[2]);
        float vz = parseFloat(auStr[3]);
        auList[j].addTo(vt, new PVector(vx, vy, vz));
        i++;
      }
    }
  }

  public void applyAU(int _i) {
    apply = _i;
  }

  public void reset() {
    apply = -1;
  }

  public void render() {
    pushMatrix();
    scale(200, -200, 200);
    for (int i=0; i<faces.length; i++) {
      beginShape(TRIANGLES);
      for (int j=0; j<3; j++) {
        int vt = faces[i][j];
        PVector dis;
        if (apply == -1) {
          dis = new PVector(0.0, 0.0, 0.0);
        } 
        else {
          dis = auList[apply].getMotion(vt);
        }
        vertex(vertices[vt].x + dis.x, 
        vertices[vt].y + dis.y, 
        vertices[vt].z + dis.z);
      }
      endShape();
    }
    popMatrix();
  }

  private int skipToAUV(int _s, String _p) {
    int i = _s;
    boolean found = false;
    while (i < wfm.length && !found) {
      String ln = trim(wfm[i]);
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
    } 
    else {
      return -1;
    }
  }

  private int skipTo(int _s, String _p) {
    int i = _s;
    boolean found = false;
    while (i < wfm.length && !found) {
      String ln = trim(wfm[i]);
      if (ln.equals("")) {
        i++;
        continue;
      }
      char c1 = ln.charAt(0);
      char c2 = ln.charAt(ln.length()-1);
      if (c1 == '#' && c2 == ':') {
        String p1 = ln.substring(2, ln.length()-1);
        if (p1.equals(_p)) {
          found = true;
        }
      } 
      else {
        i++;
        continue;
      }
      i++;
    }
    if (found) {
      return i;
    } 
    else {
      return -1;
    }
  }
}

