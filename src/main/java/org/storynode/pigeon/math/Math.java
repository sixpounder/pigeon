package org.storynode.pigeon.math;

import lombok.experimental.UtilityClass;

/** Math utilities */
@UtilityClass
public class Math {
  /**
   * Clamps a value between a <code>min</code> and <code>max</code> value (inclusive)
   *
   * @param val The value to clamp
   * @param min The minimum allowed value
   * @param max The maximum allowed value
   * @return The clamped value
   */
  public static float clamp(float val, float min, float max) {
    return java.lang.Math.max(min, java.lang.Math.min(max, val));
  }

  /**
   * Clamps a value between a <code>min</code> and <code>max</code> value (inclusive)
   *
   * @param val The value to clamp
   * @param min The minimum allowed value
   * @param max The maximum allowed value
   * @return The clamped value
   */
  public static int clamp(int val, int min, int max) {
    return java.lang.Math.max(min, java.lang.Math.min(max, val));
  }

  /**
   * Clamps a value between a <code>min</code> and <code>max</code> value (inclusive)
   *
   * @param val The value to clamp
   * @param min The minimum allowed value
   * @param max The maximum allowed value
   * @return The clamped value
   */
  public static double clamp(double val, double min, double max) {
    return java.lang.Math.max(min, java.lang.Math.min(max, val));
  }
}
