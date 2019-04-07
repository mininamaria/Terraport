package com.example.maria.terraport;

import android.widget.TextView;

class Robot {
    private int motorA;
    private int motorB;
    public int angle;
    public int strength;
    private int deltaX;
    private int deltaY;



    public Robot() {
        this.strength=0;
        this.angle=90;
        this.deltaX=10;
        this.deltaY=10;
    }
    public Robot(int strenght,int angle, int deltaX,int deltaY) {
        this.strength=strenght;
        this.angle=angle;
        this.deltaX=deltaX;
        this.deltaY=deltaY;
    }

    public void calc() {

        if ((angle >= 90 - deltaX && angle <= 90 + deltaX ) || (angle >= 270 - deltaX && angle <= 270 + deltaX )){      //вперед / назад
            this.motorA= (int) (this.strength*Math.round(Math.sin(Math.toRadians(angle))));
            this.motorB= (int) (this.strength*Math.round(Math.sin(Math.toRadians(angle))));
        }else if((angle <=0+deltaY && angle >=0) || (angle>=360-deltaY && angle<=360) ){                                //разворот по часовой
            this.motorA=-this.strength;
            this.motorB=0;
        } else if(angle<=180+deltaY&& angle>=180-deltaY){                                                               //разворот против часовой
            this.motorA=0;
            this.motorB=this.strength;
        } else if (angle > 0+deltaY&& angle< 90-deltaX){                                                                     //вперед левее
            motorA = this.strength;
            motorB = strength * (angle - deltaY) / (90 -deltaX - deltaY);
        }
        else if (angle > 90 +deltaX && angle< 180-deltaY ){                                                                //вперед правее
            motorB = this.strength;
            motorA = strength * (angle - deltaY - 90) / (90 -deltaX - deltaY);
        }
        else if (angle > 180 +deltaY && angle< 270-deltaX ){                                                                //назад левее
            motorB = -this.strength;
            motorA = - strength * (angle - deltaY - 180) / (90 -deltaX - deltaY);
        }
        else if (angle > 270 +deltaX && angle< 360-deltaY ){                                                                //назад левее
            motorB = -this.strength;
            motorA = -strength * (angle - deltaY - 270) / (90 -deltaX - deltaY);
        }
    }

    int getMotorA() {
        return motorA;
    }

    int getMotorB() {
        return motorB;
    }

    void setAngle(int angle) {
        this.angle = angle;
        calc();
    }

    void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }
}
