float rx, ry;
Candide candide;

void setup() {
  size(600, 600, OPENGL);
  smooth();
  noStroke();
  fill(255, 250, 200);
  rx = 0;
  ry = 0;
  candide = new Candide("candide3b.wfm");
}

void draw() {
  background(0);
  lights();
  translate(width/2, height/2, 100);
  rotateX(radians(rx));
  rotateY(radians(ry));
//  int unit = floor(random(57));
//  candide.applyAU(unit);
  candide.render();
}

void mouseDragged() {
  ry = (mouseX - width/2)/4.0;
  rx = -(mouseY - height/2)/4.0;
}

void keyPressed() {
  switch (key) {
  case 'a':
    candide.applyAU(54);
    break;
  case 's':
    candide.applyAU(55);
    break;
  case 'd':
    candide.applyAU(56);
    break;
  case 'f':
    candide.applyAU(57);
    break;
  case 'g':
    candide.applyAU(58);
    break;
  case 'h':
    candide.applyAU(51);
    break;
  case 'j':
    candide.applyAU(52);
    break;
  case 'k':
    candide.applyAU(53);
    break;
  case 'l':
    candide.applyAU(7);
    break;
  case 'q':
    candide.applyAU(6);
    break;
  case 'w':
    candide.applyAU(5);
    break;
  default:
    candide.reset();
    break;
  }
}

void keyReleased() {
  candide.reset();
}

