/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import java.util.List;
import java.util.Random;
import objectPools.Vec3Pool;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Quaternion;

public class Maths {
    public static final float ROOT_2 = (float)Math.sqrt(2.0);
    public static final float DEGREES_IN_CIRCLE = 360.0f;
    public static final float DEGREES_IN_HALF_CIRCLE = 180.0f;
    public static final Random RANDOM = new Random();
    public static final float PI_OVER_2 = 1.5707964f;
    public static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);
    public static final Vector3f VEC3 = new Vector3f();
    public static final Vector2f VEC2 = new Vector2f();
    public static final Vector4f VEC4 = new Vector4f();
    private static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
    private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
    private static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static float radiansToDegrees(float radians) {
        return (float)((double)(radians * 180.0f) / Math.PI);
    }

    public static float randomNumberBetween(float min, float max) {
        float extra = max - min;
        return min + RANDOM.nextFloat() * extra;
    }

    public static float getRandomVariance(float average, float varianceFactor) {
        float variance = average * varianceFactor;
        return average - variance + RANDOM.nextFloat() * variance * 2.0f;
    }

    public static float applyFactor(float original, float factor, float influence) {
        return original * (1.0f - influence) + original * factor * influence;
    }

    public static float getFactor(float factor, float influence) {
        return 1.0f - influence + influence * factor;
    }

    public static boolean chance(float chance) {
        return RANDOM.nextFloat() < chance;
    }

    public static float calculateVectorRotationY(Vector2f direction2D) {
        direction2D.normalise();
        return -((float)Math.toDegrees(Math.atan2(direction2D.y, direction2D.x) - 1.5707963705062866));
    }

    public static Matrix4f createProjectionMatrix(float width, float height, float near, float far, float fov) {
        Matrix4f projectionMatrix = new Matrix4f();
        float aspectRatio = width / height;
        float y_scale = (float)(1.0 / Math.tan(Math.toRadians(fov / 2.0f)));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = far - near;
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((far + near) / frustum_length);
        projectionMatrix.m23 = -1.0f;
        projectionMatrix.m32 = -(2.0f * near * far / frustum_length);
        projectionMatrix.m33 = 0.0f;
        return projectionMatrix;
    }

    public static float calculateVectorAngle(Vector2f direction2D) {
        direction2D.normalise();
        float angle = -((float)Math.toDegrees(Math.atan2(direction2D.y, direction2D.x) - 1.5707963705062866));
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static float parabola(float progression) {
        float x = 2.0f * progression - 1.0f;
        return -(x * x) + 1.0f;
    }

    public static Vector3f getClosestPointLineSegment(Vector3f line0, Vector3f line1, Vector3f point) {
        Vector3f line0ToPoint;
        Vector3f line0To1 = Vector3f.sub(line1, line0, null);
        float projectionDistance = Vector3f.dot(line0To1, line0ToPoint = Vector3f.sub(point, line0, null));
        if (projectionDistance <= 0.0f) {
            return new Vector3f(line0);
        }
        float lineLength = Vector3f.dot(line0To1, line0To1);
        if (lineLength <= projectionDistance) {
            return new Vector3f(line1);
        }
        float fraction = projectionDistance / lineLength;
        line0To1.scale(fraction);
        Vector3f.add(line0, line0To1, line0To1);
        return line0To1;
    }

    public static void createViewMatrix(Matrix4f viewMatrix, Vector3f position, float pitch, float yaw) {
        viewMatrix.setIdentity();
        Vector3f cameraPos = new Vector3f(-position.x, -position.y, -position.z);
        Matrix4f.rotate(Maths.degreesToRadians(pitch), new Vector3f(1.0f, 0.0f, 0.0f), viewMatrix, viewMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(-yaw), new Vector3f(0.0f, 1.0f, 0.0f), viewMatrix, viewMatrix);
        Matrix4f.translate(cameraPos, viewMatrix, viewMatrix);
    }

    public static float slope(float value, float lowerBound, float upperBound) {
        float inside = (value - lowerBound) / (upperBound - lowerBound);
        return Maths.clamp(inside, 0.0f, 1.0f);
    }

    public static String toRomanNumeral(int input) {
        if (input < 1 || input > 3999) {
            return "Z";
        }
        String s = "";
        while (input >= 1000) {
            s = String.valueOf(s) + "M";
            input -= 1000;
        }
        while (input >= 900) {
            s = String.valueOf(s) + "CM";
            input -= 900;
        }
        while (input >= 500) {
            s = String.valueOf(s) + "D";
            input -= 500;
        }
        while (input >= 400) {
            s = String.valueOf(s) + "CD";
            input -= 400;
        }
        while (input >= 100) {
            s = String.valueOf(s) + "C";
            input -= 100;
        }
        while (input >= 90) {
            s = String.valueOf(s) + "XC";
            input -= 90;
        }
        while (input >= 50) {
            s = String.valueOf(s) + "L";
            input -= 50;
        }
        while (input >= 40) {
            s = String.valueOf(s) + "XL";
            input -= 40;
        }
        while (input >= 10) {
            s = String.valueOf(s) + "X";
            input -= 10;
        }
        while (input >= 9) {
            s = String.valueOf(s) + "IX";
            input -= 9;
        }
        while (input >= 5) {
            s = String.valueOf(s) + "V";
            input -= 5;
        }
        while (input >= 4) {
            s = String.valueOf(s) + "IV";
            input -= 4;
        }
        while (input >= 1) {
            s = String.valueOf(s) + "I";
            --input;
        }
        return s;
    }

    public static void storeInArray(byte[] totalData, byte[] smallData, int start, int length) {
        int i = 0;
        while (i < length) {
            totalData[start + i] = smallData[i];
            ++i;
        }
    }

    public static float cosInterpolate(float a, float b, float blend) {
        double ft = (double)blend * Math.PI;
        float f = (float)((1.0 - Math.cos(ft)) * 0.5);
        return a * (1.0f - f) + b * f;
    }

    public static float interpolate(float min, float max, float blend) {
        return min + (max - min) * blend;
    }

    public static Vector3f interpolate(Vector3f min, Vector3f max, float blend) {
        Vector3f.sub(max, min, VEC3);
        VEC3.scale(blend);
        return Vector3f.add(min, VEC3, null);
    }

    public static float smoothInterpolate(float a, float b, float blend) {
        blend = Maths.smoothStep(0.0f, 1.0f, blend);
        return a * (1.0f - blend) + b * blend;
    }

    public static float rock(float min, float max, float time) {
        return Maths.smoothInterpolate(min, max, Math.abs((time - 0.5f) * 2.0f));
    }

    public static float fakeSin(float min, float max, float time) {
        float x = 1.0f - Math.abs((time + 0.25f) * 2.0f % 2.0f - 1.0f);
        return Maths.smoothInterpolate(min, max, x);
    }

    public static float smoothStep(float edge0, float edge1, float x) {
        float t = Maths.clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        return t * t * (3.0f - 2.0f * t);
    }

    public static float quickStep(float edge0, float edge1, float x) {
        return Maths.clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public static int clampInt(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static Vector3f convertToScreenSpace(Vector3f position, Matrix4f viewMatrix, Matrix4f projMatrix) {
        Vector4f coords = new Vector4f(position.x, position.y, position.z, 1.0f);
        Matrix4f.transform(viewMatrix, coords, coords);
        Matrix4f.transform(projMatrix, coords, coords);
        Vector3f screenCoords = Maths.clipSpaceToScreenSpace(coords);
        return screenCoords;
    }

    public static float cosify(float input) {
        float theta = (float)(Math.PI * (double)input);
        float cosine = (float)Math.cos(theta);
        return cosine * -0.5f + 0.5f;
    }

    public static Vector2f rotateAroundPoint(Vector3f origin, Vector3f point, float angle) {
        angle = (float)Math.toRadians(angle);
        double unrotatedCircleX = Math.cos(angle) * (double)(point.x - origin.x) - Math.sin(angle) * (double)(point.z - origin.z) + (double)origin.x;
        double unrotatedCircleY = Math.sin(angle) * (double)(point.x - origin.x) + Math.cos(angle) * (double)(point.z - origin.z) + (double)origin.z;
        return new Vector2f((float)unrotatedCircleX, (float)unrotatedCircleY);
    }

    private static Vector3f clipSpaceToScreenSpace(Vector4f coords) {
        if (coords.w < 0.0f) {
            return null;
        }
        Vector3f screenCoords = new Vector3f((coords.x / coords.w + 1.0f) / 2.0f, 1.0f - (coords.y / coords.w + 1.0f) / 2.0f, coords.z);
        return screenCoords;
    }

    public static Matrix4f getRotationMatrix(Vector3f up, Vector3f forwardish) {
        Vector3f right = Vector3f.cross(forwardish, up, null);
        right.normalise();
        Vector3f.cross(up, right, forwardish);
        forwardish.normalise();
        Matrix4f rotationMat = new Matrix4f();
        rotationMat.m00 = right.x;
        rotationMat.m01 = right.y;
        rotationMat.m02 = right.z;
        rotationMat.m10 = up.x;
        rotationMat.m11 = up.y;
        rotationMat.m12 = up.z;
        rotationMat.m20 = -forwardish.x;
        rotationMat.m21 = -forwardish.y;
        rotationMat.m22 = -forwardish.z;
        return rotationMat;
    }

    public static float[] floatListToArray(List<Float> floatList) {
        float[] array = new float[floatList.size()];
        int i = 0;
        while (i < array.length) {
            array[i] = floatList.get(i).floatValue();
            ++i;
        }
        return array;
    }

    public static Vector2f generateRandom2dVector() {
        double radians = (double)RANDOM.nextFloat() * Math.PI * 2.0;
        float x = (float)Math.cos(radians);
        float y = (float)Math.sin(radians);
        return new Vector2f(x, y);
    }

    public static float distanceFromLine(Vector2f entityCenter, float gradient, Vector2f lineOrigin) {
        float a = gradient;
        float c = lineOrigin.getY() - a * lineOrigin.getX();
        float distance = (float)((double)Math.abs(-a * entityCenter.x + entityCenter.y - c) / Math.sqrt(a * a + 1.0f));
        return distance;
    }

    public static void createTexCoordTransform(Matrix3f transform, float x, float y, float rot, float scale) {
        transform.setIdentity();
        Maths.translateMatrix(transform, -0.5f, -0.5f);
        Matrix3f rotation = new Matrix3f();
        float cosAlpha = (float)Math.cos(Math.toRadians(rot));
        float sinAlpha = (float)Math.sin(Math.toRadians(rot));
        rotation.m00 = cosAlpha;
        rotation.m11 = cosAlpha;
        rotation.m10 = sinAlpha;
        rotation.m01 = -sinAlpha;
        Matrix3f.mul(rotation, transform, transform);
        Maths.translateMatrix(transform, 0.5f, 0.5f);
        Maths.scaleMatrix(transform, scale);
        Maths.translateMatrix(transform, x, y);
    }

    public static void translateMatrix(Matrix3f matrix, float x, float y) {
        Matrix3f translate = new Matrix3f();
        translate.m20 = x;
        translate.m21 = y;
        Matrix3f.mul(translate, matrix, matrix);
    }

    public static float[] concatenateArrays(List<float[]> arrays) {
        int totalLength = 0;
        for (float[] array : arrays) {
            totalLength += array.length;
        }
        return Maths.concatenateArrays(arrays, totalLength);
    }

    public static float[] concatenateArrays(List<float[]> arrays, int floatCount) {
        float[] bigArray = new float[floatCount];
        int pointer = 0;
        for (float[] array : arrays) {
            int i = 0;
            while (i < array.length) {
                bigArray[pointer++] = array[i];
                ++i;
            }
        }
        return bigArray;
    }

    public static float[] concatenateArrays(float[] ... arrays) {
        int totalLength = 0;
        float[][] fArray = arrays;
        int n = arrays.length;
        int n2 = 0;
        while (n2 < n) {
            float[] array = fArray[n2];
            totalLength += array.length;
            ++n2;
        }
        float[] bigArray = new float[totalLength];
        int pointer = 0;
        float[][] fArray2 = arrays;
        int n3 = arrays.length;
        int n4 = 0;
        while (n4 < n3) {
            float[] array = fArray2[n4];
            int i = 0;
            while (i < array.length) {
                bigArray[pointer++] = array[i];
                ++i;
            }
            ++n4;
        }
        return bigArray;
    }

    public static int[] concatenateArrays(int[] ... arrays) {
        int totalLength = 0;
        int[][] nArray = arrays;
        int n = arrays.length;
        int n2 = 0;
        while (n2 < n) {
            int[] array = nArray[n2];
            totalLength += array.length;
            ++n2;
        }
        int[] bigArray = new int[totalLength];
        int pointer = 0;
        int[][] nArray2 = arrays;
        int n3 = arrays.length;
        int n4 = 0;
        while (n4 < n3) {
            int[] array = nArray2[n4];
            int i = 0;
            while (i < array.length) {
                bigArray[pointer++] = array[i];
                ++i;
            }
            ++n4;
        }
        return bigArray;
    }

    public static void scaleMatrix(Matrix3f matrix, float scale) {
        Matrix3f scaleMatrix = new Matrix3f();
        scaleMatrix.m00 = scale;
        scaleMatrix.m11 = scale;
        Matrix3f.mul(scaleMatrix, matrix, matrix);
    }

    public static boolean pointInTriangle(Vector3f p, Vector3f a1, Vector3f b1, Vector3f c1) {
        Vector3f a = new Vector3f(a1.x, 0.0f, a1.z);
        Vector3f b = new Vector3f(b1.x, 0.0f, b1.z);
        Vector3f c = new Vector3f(c1.x, 0.0f, c1.z);
        return Maths.sameSide(p, a, b, c) && Maths.sameSide(p, b, a, c) && Maths.sameSide(p, c, a, b);
    }

    public static boolean sameSide(Vector3f p1, Vector3f p2, Vector3f a, Vector3f b) {
        Vector3f cp2;
        Vector3f cp1 = Vector3f.cross(Vector3f.sub(b, a, null), Vector3f.sub(p1, a, null), null);
        return Vector3f.dot(cp1, cp2 = Vector3f.cross(Vector3f.sub(b, a, null), Vector3f.sub(p2, a, null), null)) >= 0.0f;
    }

    public static boolean testPointInTriangle(Vector3f p, Vector3f a, Vector3f b, Vector3f c) {
        Vector3f v0 = Vector3f.sub(c, a, null);
        Vector3f v1 = Vector3f.sub(b, a, null);
        Vector3f v2 = Vector3f.sub(p, a, null);
        float dot00 = Vector3f.dot(v0, v0);
        float dot01 = Vector3f.dot(v0, v1);
        float dot02 = Vector3f.dot(v0, v2);
        float dot11 = Vector3f.dot(v1, v1);
        float dot12 = Vector3f.dot(v1, v2);
        float invDenom = 1.0f / (dot00 * dot11 - dot01 * dot01);
        float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        float v = (dot00 * dot12 - dot01 * dot02) * invDenom;
        return u >= 0.0f && v >= 0.0f && u + v < 1.0f;
    }

    public static Matrix3f getRotationMatrix(Matrix4f mat4) {
        Matrix3f mat3 = new Matrix3f();
        mat3.m00 = mat4.m00;
        mat3.m01 = mat4.m01;
        mat3.m02 = mat4.m02;
        mat3.m10 = mat4.m10;
        mat3.m11 = mat4.m11;
        mat3.m12 = mat4.m12;
        mat3.m20 = mat4.m20;
        mat3.m21 = mat4.m21;
        mat3.m22 = mat4.m22;
        return mat3;
    }

    public static Vector3f generateRandomUnitVector() {
        Random random = new Random();
        float theta = (float)((double)(random.nextFloat() * 2.0f) * Math.PI);
        float z = random.nextFloat() * 2.0f - 1.0f;
        float rootOneMinusZSquared = (float)Math.sqrt(1.0f - z * z);
        float x = (float)((double)rootOneMinusZSquared * Math.cos(theta));
        float y = (float)((double)rootOneMinusZSquared * Math.sin(theta));
        return new Vector3f(x, y, z);
    }

    public static Vector3f randomPointInSquare(float centerX, float centerZ, float halfSize) {
        float fullSize = halfSize + halfSize;
        Vector3f result = new Vector3f();
        result.x = centerX - halfSize + RANDOM.nextFloat() * fullSize;
        result.z = centerZ - halfSize + RANDOM.nextFloat() * fullSize;
        return result;
    }

    public static Vector3f randomPointOnSquare(float centerX, float centerZ, float halfSize) {
        float fullSize = halfSize + halfSize;
        Vector3f result = new Vector3f();
        boolean side = RANDOM.nextBoolean();
        float sideValue = RANDOM.nextBoolean() ? 1 : 0;
        result.x = centerX - halfSize + (side ? RANDOM.nextFloat() : sideValue) * fullSize;
        result.z = centerZ - halfSize + (!side ? RANDOM.nextFloat() : sideValue) * fullSize;
        return result;
    }

    public static Vector3f randomPointOnCircle(Vector3f normal, float radius) {
        Random random = new Random();
        Vector3f randomPerpendicular = new Vector3f();
        do {
            Vector3f randomVector = Maths.generateRandomUnitVector();
            Vector3f.cross(randomVector, normal, randomPerpendicular);
        } while (randomPerpendicular.length() == 0.0f);
        randomPerpendicular.normalise();
        randomPerpendicular.scale(radius);
        float a = random.nextFloat();
        float b = random.nextFloat();
        if (a > b) {
            float temp = a;
            a = b;
            b = temp;
        }
        float randX = (float)((double)b * Math.cos(Math.PI * 2 * (double)(a / b)));
        float randY = (float)((double)b * Math.sin(Math.PI * 2 * (double)(a / b)));
        float distance = new Vector2f(randX, randY).length();
        randomPerpendicular.scale(distance);
        return randomPerpendicular;
    }

    public static Vector3f generateVectorWithinCone(Vector3f coneDirection, float angle, float scale) {
        Vector3f velocity = Maths.generateRandomUnitVectorWithinCone(coneDirection, angle);
        velocity.scale(scale);
        return velocity;
    }

    public static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
        float cosAngle = (float)Math.cos(angle);
        Random random = new Random();
        float theta = (float)((double)(random.nextFloat() * 2.0f) * Math.PI);
        float z = cosAngle + random.nextFloat() * (1.0f - cosAngle);
        float rootOneMinusZSquared = (float)Math.sqrt(1.0f - z * z);
        float x = (float)((double)rootOneMinusZSquared * Math.cos(theta));
        float y = (float)((double)rootOneMinusZSquared * Math.sin(theta));
        Vector4f direction = new Vector4f(x, y, z, 1.0f);
        if (coneDirection.x != 0.0f || coneDirection.y != 0.0f || coneDirection.z != 1.0f && coneDirection.z != -1.0f) {
            Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0.0f, 0.0f, 1.0f), null);
            rotateAxis.normalise();
            float rotateAngle = (float)Math.acos(Vector3f.dot(coneDirection, new Vector3f(0.0f, 0.0f, 1.0f)));
            Matrix4f rotationMatrix = new Matrix4f();
            rotationMatrix.setIdentity();
            rotationMatrix.rotate(-rotateAngle, rotateAxis);
            Matrix4f.transform(rotationMatrix, direction, direction);
        } else if (coneDirection.z == -1.0f) {
            direction.z *= -1.0f;
        }
        return new Vector3f(direction.x, direction.y, direction.z);
    }

    public static boolean isRayIntersectingWithSphere(Vector3f unitRay, Vector3f start, Vector3f spherePosition, float radius) {
        Vector3f toSphere = Vector3f.sub(spherePosition, start, null);
        if (Vector3f.dot(toSphere, unitRay) < 0.0f) {
            return false;
        }
        float projectedVectorLength = Vector3f.dot(unitRay, toSphere);
        Vector3f projectedVector = new Vector3f(unitRay.x * projectedVectorLength, unitRay.y * projectedVectorLength, unitRay.z * projectedVectorLength);
        Vector3f projectedPoint = Vector3f.add(start, projectedVector, null);
        Vector3f projectedToCenter = Vector3f.sub(spherePosition, projectedPoint, null);
        return projectedToCenter.length() <= radius;
    }

    public static float getAverageOfList(List<Float> numbers) {
        float total = 0.0f;
        for (Float number : numbers) {
            total += number.floatValue();
        }
        return total / (float)numbers.size();
    }

    public static Vector3f rotateVector(Vector3f direction, float rotX, float rotY, float rotZ) {
        Matrix4f matrix = Maths.updateTransformationMatrix(new Matrix4f(), 0.0f, 0.0f, 0.0f, rotX, rotY, rotZ, 1.0f);
        Vector4f direction4 = new Vector4f(direction.x, direction.y, direction.z, 1.0f);
        Matrix4f.transform(matrix, direction4, direction4);
        return new Vector3f(direction4.x, direction4.y, direction4.z);
    }

    public static String toStringWithLimitedDigits(float number, int numberDigits) {
        String fullNumber = Float.toString(number);
        char[] digits = fullNumber.toCharArray();
        if (digits.length < numberDigits) {
            return fullNumber;
        }
        String limitedNumber = "";
        int i = 0;
        while (i < numberDigits) {
            limitedNumber = String.valueOf(limitedNumber) + digits[i];
            ++i;
        }
        return limitedNumber;
    }

    public static float coTangent(float angle) {
        return (float)(1.0 / Math.tan(angle));
    }

    public static float degreesToRadians(float degrees) {
        return degrees * ((float)Math.PI / 180);
    }

    public static int[] calculateGridSquaresCrazy(int terrain, int section, int sectionCount, int increase) {
        int terrainChange = increase / sectionCount;
        int sectionChange = increase % sectionCount;
        int terrainResult = terrain + terrainChange;
        int sectionResult = section + sectionChange;
        if (sectionResult >= sectionCount) {
            sectionResult -= sectionCount;
        } else if (sectionResult < 0) {
            sectionResult += sectionCount;
            --terrainResult;
        }
        return new int[]{++terrainResult, sectionResult};
    }

    public static float getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        float dX = x1 - x2;
        float dY = y1 - y2;
        float distance = (float)Math.sqrt(dX * dX + dY * dY);
        return distance;
    }

    public static float getDistanceBetweenPoints(float x1, float y1, float z1, float x2, float y2, float z2) {
        float dX = x1 - x2;
        float dY = y1 - y2;
        float dZ = z1 - z2;
        float distance = (float)Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        return distance;
    }

    public static float getComparitableDistance(float x1, float y1, float z1, float x2, float y2, float z2) {
        float dX = x1 - x2;
        float dY = y1 - y2;
        float dZ = z1 - z2;
        float distance = dX * dX + dY * dY + dZ * dZ;
        return distance;
    }

    public static float getComparitableDistance(float x1, float y1, float x2, float y2) {
        float dX = x1 - x2;
        float dY = y1 - y2;
        float distance = dX * dX + dY * dY;
        return distance;
    }

    public static float getComparitableDistance(Vector3f p1, Vector3f p2) {
        return Maths.getComparitableDistance(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
    }

    public static float getVectorLength(float x, float y, float z) {
        float length = (float)Math.sqrt(x * x + y * y + z * z);
        return length;
    }

    public static double dotProductOfQuaternions(double[] qA, double[] qB) {
        return qA[0] * qB[0] + qA[1] * qB[1] + qA[2] * qB[2] + qA[3] * qB[3];
    }

    public static double dotProductOfQuaternions(Quaternion qA, Quaternion qB) {
        return qA.getW() * qB.getW() + qA.getX() * qB.getX() + qA.getY() * qB.getY() + qA.getZ() * qB.getZ();
    }

    public static Matrix4f quaternionToMatrix(double w, double x, double y, double z) {
        double xSquared = x * x;
        double twoXY = 2.0 * x * y;
        double twoXZ = 2.0 * x * z;
        double twoXW = 2.0 * x * w;
        double ySquared = y * y;
        double twoYZ = 2.0 * y * z;
        double twoYW = 2.0 * y * w;
        double twoZW = 2.0 * z * w;
        double zSquared = z * z;
        double wSquared = w * w;
        Matrix4f matrix = new Matrix4f();
        matrix.m00 = (float)(wSquared + xSquared - ySquared - zSquared);
        matrix.m01 = (float)(twoXY - twoZW);
        matrix.m02 = (float)(twoXZ + twoYW);
        matrix.m03 = 0.0f;
        matrix.m10 = (float)(twoXY + twoZW);
        matrix.m11 = (float)(wSquared - xSquared + ySquared - zSquared);
        matrix.m12 = (float)(twoYZ - twoXW);
        matrix.m13 = 0.0f;
        matrix.m20 = (float)(twoXZ - twoYW);
        matrix.m21 = (float)(twoYZ + twoXW);
        matrix.m22 = (float)(wSquared - xSquared - ySquared + zSquared);
        matrix.m23 = 0.0f;
        matrix.m30 = 0.0f;
        matrix.m31 = 0.0f;
        matrix.m32 = 0.0f;
        matrix.m33 = 1.0f;
        return matrix;
    }

    public static Matrix4f updateTransformationMatrix(Matrix4f transformationMatrix, float x, float y, float z, float rotX, float rotY, float rotZ, float scale) {
        transformationMatrix.setIdentity();
        Vector3f pos = Vec3Pool.get(x, y, z);
        Matrix4f.translate(pos, transformationMatrix, transformationMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotY), Y_AXIS, transformationMatrix, transformationMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotZ), Z_AXIS, transformationMatrix, transformationMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotX), X_AXIS, transformationMatrix, transformationMatrix);
        Vector3f scaleVec = Vec3Pool.get(scale, scale, scale);
        Matrix4f.scale(scaleVec, transformationMatrix, transformationMatrix);
        Vec3Pool.release(pos, scaleVec);
        return transformationMatrix;
    }

    public static Matrix4f updateTransformationMatrix(Matrix4f transformationMatrix, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        return Maths.updateTransformationMatrix(transformationMatrix, position.x, position.y, position.z, rotX, rotY, rotZ, scale);
    }

    public static Matrix4f updateModelMatrix(Matrix4f matrix, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale) {
        matrix.setIdentity();
        Matrix4f.translate(position, matrix, matrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotX), X_AXIS, matrix, matrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotY), Y_AXIS, matrix, matrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotZ), Z_AXIS, matrix, matrix);
        Matrix4f.scale(scale, matrix, matrix);
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(float x, float y, float z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
        Matrix4f transformationMatrix = new Matrix4f();
        Matrix4f.translate(new Vector3f(x, y, z), transformationMatrix, transformationMatrix);
        Matrix4f.scale(new Vector3f(scaleX, scaleY, scaleZ), transformationMatrix, transformationMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotX), X_AXIS, transformationMatrix, transformationMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotY), Y_AXIS, transformationMatrix, transformationMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotZ), Z_AXIS, transformationMatrix, transformationMatrix);
        return transformationMatrix;
    }

    public static Matrix4f createModelMatrix(Vector3f pos, float rotY, float scale) {
        Matrix4f modelMatrix = new Matrix4f();
        Matrix4f.translate(pos, modelMatrix, modelMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(rotY), new Vector3f(0.0f, 1.0f, 0.0f), modelMatrix, modelMatrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
        return modelMatrix;
    }

    public static float getLowestPositiveRoot(float a, float b, float c) {
        float r1;
        float determinant = b * b - 4.0f * a * c;
        if (determinant < 0.0f) {
            return -1.0f;
        }
        float sqrtD = (float)Math.sqrt(determinant);
        float r2 = (-b + sqrtD) / (2.0f * a);
        if (r2 < (r1 = (-b - sqrtD) / (2.0f * a))) {
            float temp = r1;
            r1 = r2;
            r2 = temp;
        }
        if (r1 > 0.0f) {
            return r1;
        }
        if (r2 > 0.0f) {
            return r2;
        }
        return -1.0f;
    }

    public static Vector3f calculateNormal(Vector3f point0, Vector3f point1, Vector3f point2) {
        Vector3f vectorA = Vector3f.sub(point1, point0, null);
        Vector3f vectorB = Vector3f.sub(point2, point0, null);
        Vector3f normal = Vector3f.cross(vectorA, vectorB, null);
        normal.normalise();
        return normal;
    }

    public static String formatNumber(long amount) {
        String cash = "";
        boolean negative = amount < 0L;
        amount = Math.abs(amount);
        String number = String.valueOf(amount);
        char[] digits = number.toCharArray();
        int count = 0;
        cash = String.valueOf(cash) + digits[count++];
        while (count < digits.length) {
            if ((digits.length - count) % 3 == 0) {
                cash = String.valueOf(cash) + ",";
            }
            cash = String.valueOf(cash) + digits[count++];
        }
        String result = negative ? "-" : "";
        return String.valueOf(result) + cash;
    }

    public static float biggestOf(float a, float b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    public static String formatAbreviatedNumber(long count) {
        String suffix;
        String number;
        if (count >= 10000000000L) {
            number = String.valueOf(count / 1000000000L);
            suffix = "B";
        } else if (count >= 1000000000L) {
            number = String.format("%.1f", Float.valueOf((float)count / 1.0E9f));
            suffix = "B";
        } else if (count >= 10000000L) {
            number = String.valueOf(count / 1000000L);
            suffix = "M";
        } else if (count >= 1000000L) {
            number = String.format("%.1f", Float.valueOf((float)count / 1000000.0f));
            suffix = "M";
        } else if (count >= 10000L) {
            number = String.valueOf(count / 1000L);
            suffix = "K";
        } else if (count >= 1000L) {
            number = String.format("%.1f", Float.valueOf((float)count / 1000.0f));
            suffix = "K";
        } else {
            suffix = "";
            number = String.valueOf(count);
        }
        String countString = String.valueOf(number) + suffix;
        return countString;
    }
}

