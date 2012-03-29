
## About

Arcball library adapted from code by Simon Greenwold for Yale's Model Based Design class.

Use the Arcball to allow the user to freely rotate the world using a virtual trackball.

Originally distributed on the [Processing wiki](http://wiki.processing.org/w/Arcball), somewhat
adapted since then to be less "magic" and more versatile. 

## Usage

Create an instance of the `ArcBall` class, initialize it in your setup function and apply it
in your draw function, either to the camera (if you don't want it to affect your lighting) or
directly to the model if you prefer. Apologies for the mysterious sign issues, at some point
I will sit down and derive the correct equations for Processing's default camera so that the
angle and axis can be applied directly.


```processing

    import com.processinghacks.arcball.*;

    ArcBall arcBall;
    boolean applyToCamera = true;

    void setup() {
      size(640,480,P3D);
      // for a default arcball
      // centered on width/2, height/2, -min(width/2,height/2)
      // with radius min(width/2,height/2)
      arcBall = new ArcBall(this);
    }

    void draw() {
      background(100);
      
      beginCamera();
      camera();
      if (applyToCamera) {
        translate(width/2,height/2);
        PVector axis = arcBall.getAxis();
        rotate( arcBall.getAngle(), axis.x, axis.y, -axis.z );  
        translate(-width/2,-height/2);
      }
      endCamera();

      lights();
      pushMatrix();
      noStroke();
      fill(#ff9900);
      translate(width/2,height/2);
      if (!applyToCamera) {
        PVector axis = arcBall.getAxis();
        rotate( -arcBall.getAngle(), -axis.x, -axis.y, axis.z );      
      }
      box(200.0);
      popMatrix();
      
      camera();
      noLights();
      fill(255);
      textAlign(LEFT, TOP);
      text(applyToCamera ? "camera" : "model", 10, 10);  
    }

    void keyPressed() {
      if (key == ' ') {
        applyToCamera = !applyToCamera;
      } 
    }
```

## Installation

[Download](https://github.com/RandomEtc/processing-arcball/zipball/master) or clone the project.

Unzip, rename the folder to `arcball` and copy it into the `libraries` folder of your Processing sketch folder, then restart Processing.


## Bugs / Requests

Please [file an issue on github](https://github.com/RandomEtc/processing-arcball/issues)

Enjoy! 

