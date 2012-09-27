import pCandide.PCandide;

float rx, ry;
PCandide candide;

void setup() {
  size(600, 600, P3D);
  smooth();
  noStroke();
  fill(255, 250, 200);
  rx = 0;
  ry = 0;
  candide = new PCandide(this, "candide3c.wfm");
}

void draw() {
  background(0);
  lights();
  translate(width/2, height/2, 100);
  rotateX(radians(rx));
  rotateY(radians(ry));
  candide.render(200);
}

void mouseDragged() {
  ry = (mouseX - width/2)/4.0;
  rx = -(mouseY - height/2)/4.0;
}

void keyPressed() {
  candide.applyRandom();
}

void keyReleased() {
  candide.reset();
}

