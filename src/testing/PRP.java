package testing;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The purpose of this class is to generate pseudo random permutations without
 * storing any of the indices.
 *
 * Useful for testing when one wants to make sure that all values occur at most
 * once.
 *
 * @author evensen
 */
public class PRP implements Iterable<Long> {
	// Some odd nothing-up-my-sleeve-numbers.
	private static final long SQRT3 = 0xBB67AE8584CAA73BL;
	private static final long SQRT5 = 0x3C6EF372FE94F82BL;
	private static final long SQRT7 = 0xA54FF53A5F1D36F1L;

	private final long size;
	private final long seed;
	private final long mask;
	private final int s1, s2;

	/**
	 * @param size The size of the permutation.
	 * @param seed The randomization seed.
	 */
	public PRP(long size, long seed) {
		this.size = size;
		@SuppressWarnings("hiding")
		long mask = 0L;
		int i = 0;
		while (mask < size) {
			mask = mask << 1 | 1;
			i++;
		}
		this.mask = mask;
		this.seed = seed;
		this.s1 = Math.max(i / 3, 1);
		this.s2 = Math.max(2 * i / 3, 1);
	}

	// A decent approximation of a psuedo-random
	// permutation on [-2^63, 2^63).
	// this.seed influences what permutation is chosen.
	// (Of course this is a small subset since there are
	// size! permutations but the seed has a cardinality
	// [typically] much smaller than size!.)
	// Different seeds are not guaranteed to yield different
	// permutations although it's highly likely for larger
	// sizes.
	private long mix(long v0) {
		long v = v0;
		long s = this.seed;
		for (int i = 0; i < 3; i++) {
			s = s + SQRT3;
			s ^= s >>> 49 ^ s >>> 25;
			v ^= v >>> this.s1 ^ v >>> this.s2;
			v = v + s & this.mask;
			s = s * SQRT5 & this.mask;
			v = v * SQRT7 & this.mask;
		}
		return v;
	}

	/**
	 * Give an iterator that will be able to iterate for {@code size} elements and
	 * contain each element [0, size) exactly once.
	 */
	@Override
	public Iterator<Long> iterator() {
		return new Iterator<>() {
			private long ctr = 0;
			private long used = 0;

			@Override
			public boolean hasNext() {
				return this.used < PRP.this.size;
			}

			@SuppressWarnings("boxing")
			@Override
			public Long next() {
				if (this.used == PRP.this.size) {
					throw new NoSuchElementException();
				}
				long v = -1;
				do {
					v = mix(this.ctr++);
				} while (v >= PRP.this.size);
				this.used++;
				return v;
			}

		};
	}

	@SuppressWarnings("boxing")
	@Override
	public int hashCode() {
		return Objects.hash(this.seed, this.size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		PRP other = (PRP) obj;
		return this.seed == other.seed && this.size == other.size;
	}
}