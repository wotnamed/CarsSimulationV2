public class Physics {
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
