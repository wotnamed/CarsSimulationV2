public class Physics {
    // TODO
    // CALCULATIONS TO BE DONE IN A SEPARATE Physics CLASS INVOKED FROM UPDATE FUNCTION IN IMPLEMENTATIONS OF PhysicsBasedVehicle
    // calculate velocity vector from current velocity and facing angle as facing angle should be direction of velocity
    // update current engine force
    // calculate or get angle of engine force (aka angle between current position and target position
    //      to fo this we need a function that takes (dy/dx) if dy != 0, and if dy=0 return value based on dx
    // update traction by checking ground parameters at current coordinates
    // calculate engine force vector by multiplying traction*enginePower*trigfunction(targetAngle) for x and y
    // get acceleration by force/mass
    // get added velocity by multiplying with dt
    // sum previous velocity vector and new velocity vector
    // add any other eventual velocity vector arising from collision or other event
    // calculate the actual velocity (speed)
    // calculate the angle of the velocity vector
    // calculate drag from the velocity, turn it into a vector by multiplying with trig(angle)
    // add drag to velocity vector (decrease speed)
    // calculate the actual velocity (again)
    // update currentVelocity
    // set facing angle to previously calculated angle of velocity vector
    // update position by adding velocity vector * dt to current position

    // testing of this system could be done by creating a "checkpoint" object with dynamic (user adjustable) position visualized on screen as target for the vehicle. (position could for example be adjusted with WASD keys in runtime)
    public double[] convertVelocityToVector(double velocity, double angle){
        return new double[]{velocity*Math.cos(angle),velocity*Math.sin(angle)};
    }
    public double calculateTargetAngle(double[] origin, double[] destination){
        return Math.atan2(destination[1]-origin[1], destination[0]-origin[0]);
    }

    public double calculateAngleOfVector(double[] vector){
        return Math.atan2(vector[1],vector[0]);
    }
    public double[] calculateEngineForceVector(double traction, double enginePower, double angle){
        return new double[]{traction*enginePower*Math.cos(angle), traction*enginePower*Math.sin(angle)};
    }

    public double[] calculateAcceleration(double[] forceVector, double mass){
        return new double[]{forceVector[0]/mass, forceVector[1]/mass};
    }

    public double[] calculateVelocityFromAcceleration(double[] accelerationVector, double dt){
        return new double[]{accelerationVector[0]*dt, accelerationVector[1]*dt};
    }

    public double[] sumVectors(double[] a, double[] b){
        return new double[]{a[0]+b[0],a[1]+b[1]};
    }

    public double calculateHypotenuse(double[] vector){
        return Math.sqrt(Math.pow(vector[0], 2)+Math.pow(vector[1], 2));
    }

    public double[] calculateDragVector(double velocity, double dragcoefficient, double angle){
        return new double[]{-dragcoefficient*Math.pow(velocity, 2)*Math.cos(angle), -dragcoefficient*Math.pow(velocity, 2)*Math.sin(angle)};
    }

    public double[] calculateCoordinates(double[] currentCoordinates, double[] velocityVector, double dt){
        return new double[]{currentCoordinates[0]+velocityVector[0]*dt, currentCoordinates[1]+velocityVector[1]*dt};
    }

}
