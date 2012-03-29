/*

  Adapted into Processing library 5th Feb 2006 Tom Carden
  from "simple arcball use template" 9.16.03 Simon Greenwold
   
  Copyright (c) 2003 Simon Greenwold
  Copyright (c) 2006 Tom Carden

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA

*/

package com.processinghacks.arcball;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.event.MouseEvent;

public class ArcBall {

  PApplet parent;
  
  float radius;
  PVector center;
  PVector v_down, v_drag;
  Quat q_now, q_down, q_drag;

  /** defaults to radius of min(width/2,height/2) and center.z of -radius */
  public ArcBall(PApplet parent) {
    this( null, 0, parent);
  }

  public ArcBall(PVector center, float radius, PApplet parent) {

    if (center == null) {
      float x = parent.g.width / 2.0f;
      float y = parent.g.height / 2.0f;
      if (radius == 0) {
        radius = PApplet.min(x, y);
      }
      float z = -radius;
      center = new PVector(x, y, z);
    }

    this.parent = parent;

    parent.registerMouseEvent(this);
    parent.registerPre(this);

    this.center = center;
    this.radius = radius;

    v_down = new PVector();
    v_drag = new PVector();

    q_now = new Quat();
    q_down = new Quat();
    q_drag = new Quat();
  }

  public void mouseEvent(MouseEvent event) {
    int id = event.getID();
    if (id == MouseEvent.MOUSE_DRAGGED) {
      mouseDragged();
    } 
    else if (id == MouseEvent.MOUSE_PRESSED) {
      mousePressed();
    }
  }

  public void mousePressed() {
    v_down = mouse_to_sphere(parent.mouseX, parent.mouseY);
    q_down.set(q_now);
    q_drag.reset();
  }

  public void mouseDragged() {
    v_drag = mouse_to_sphere(parent.mouseX, parent.mouseY);
    q_drag.set(v_down.dot(v_drag), v_down.cross(v_drag));
  }

  public void pre() {
    parent.translate(center.x, center.y, center.z);
    q_now = Quat.mul(q_drag, q_down);
    applyQuat2Matrix(q_now);
    parent.translate(-center.x, -center.y, -center.z);
  }

  PVector mouse_to_sphere(float x, float y) {
    PVector v = new PVector();
    v.x = (x - center.x) / radius;
    v.y = (y - center.y) / radius;

    float mag = v.x * v.x + v.y * v.y;
    if (mag > 1.0f) {
      v.normalize();
    }
    else {
      v.z = PApplet.sqrt(1.0f - mag);
    }
    return v;
  }

  void applyQuat2Matrix(Quat q) {
    // instead of transforming q into a matrix and applying it...
    float[] aa = q.getValue();
    parent.rotate(aa[0], aa[1], aa[2], aa[3]);
  }

  static class Quat {
        
    float w, x, y, z;

    Quat() {
      reset();
    }

    Quat(float w, float x, float y, float z) {
      this.w = w;
      this.x = x;
      this.y = y;
      this.z = z;
    }

    void reset() {
      w = 1.0f;
      x = 0.0f;
      y = 0.0f;
      z = 0.0f;
    }

    void set(float w, PVector v) {
      this.w = w;
      x = v.x;
      y = v.y;
      z = v.z;
    }

    void set(Quat q) {
      w = q.w;
      x = q.x;
      y = q.y;
      z = q.z;
    }

    static Quat mul(Quat q1, Quat q2) {
      Quat res = new Quat();
      res.w = q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z;
      res.x = q1.w * q2.x + q1.x * q2.w + q1.y * q2.z - q1.z * q2.y;
      res.y = q1.w * q2.y + q1.y * q2.w + q1.z * q2.x - q1.x * q2.z;
      res.z = q1.w * q2.z + q1.z * q2.w + q1.x * q2.y - q1.y * q2.x;
      return res;
    }

    float[] getValue() {
      // transforming this quat into an angle and an axis vector...

      float[] res = new float[4];

      float sa = (float) Math.sqrt(1.0f - w * w);
      if (sa < PApplet.EPSILON) {
        sa = 1.0f;
      }

      res[0] = (float) Math.acos(w) * 2.0f;
      res[1] = x / sa;
      res[2] = y / sa;
      res[3] = z / sa;

      return res;
    }
    
  } // Quat

}

