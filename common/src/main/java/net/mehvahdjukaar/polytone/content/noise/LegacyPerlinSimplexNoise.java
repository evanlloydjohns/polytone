package net.mehvahdjukaar.polytone.content.noise;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

import java.util.List;
import java.util.TreeSet;

/**
 * Keeps Polytone's existing octave-based noise format after vanilla removed
 * PerlinSimplexNoise in 26.3. The octave layout and weighting follow 26.2.
 */
public final class LegacyPerlinSimplexNoise {
    private final OffsetSimplexNoise[] noiseLevels;
    private final double highestFreqInputFactor;
    private final double highestFreqValueFactor;

    public LegacyPerlinSimplexNoise(RandomSource random, List<Integer> octaves) {
        var sorted = new TreeSet<>(octaves);
        if (sorted.isEmpty()) throw new IllegalArgumentException("Need some octaves!");

        int highest = sorted.last();
        int count = highest - sorted.first() + 1;
        if (count < 1) throw new IllegalArgumentException("Total number of octaves needs to be >= 1");

        OffsetSimplexNoise base = new OffsetSimplexNoise(random);
        noiseLevels = new OffsetSimplexNoise[count];
        if (highest >= 0 && highest < count && sorted.contains(0)) noiseLevels[highest] = base;

        for (int index = highest + 1; index < count; index++) {
            if (index >= 0 && sorted.contains(highest - index)) {
                noiseLevels[index] = new OffsetSimplexNoise(random);
            } else {
                random.consumeCount(262);
            }
        }

        if (highest > 0) {
            long seed = (long) (base.originValue() * Long.MAX_VALUE);
            RandomSource lowerRandom = new WorldgenRandom(new LegacyRandomSource(seed));
            for (int index = highest - 1; index >= 0; index--) {
                if (index < count && sorted.contains(highest - index)) {
                    noiseLevels[index] = new OffsetSimplexNoise(lowerRandom);
                } else {
                    lowerRandom.consumeCount(262);
                }
            }
        }

        highestFreqInputFactor = Math.pow(2.0, highest);
        highestFreqValueFactor = 1.0 / (Math.pow(2.0, count) - 1.0);
    }

    public double getValue(double x, double y, boolean useOrigin) {
        double value = 0.0;
        double inputFactor = highestFreqInputFactor;
        double valueFactor = highestFreqValueFactor;
        for (OffsetSimplexNoise level : noiseLevels) {
            if (level != null) value += level.sample(x * inputFactor, y * inputFactor, useOrigin) * valueFactor;
            inputFactor /= 2.0;
            valueFactor *= 2.0;
        }
        return value;
    }

    private static final class OffsetSimplexNoise extends SimplexNoise {
        private OffsetSimplexNoise(RandomSource random) {
            super(random);
        }

        private double sample(double x, double y, boolean useOrigin) {
            return get(x - (useOrigin ? 0.0 : offsetX), y - (useOrigin ? 0.0 : offsetY));
        }

        private double originValue() {
            return get(offsetX, offsetY, offsetZ);
        }
    }
}
