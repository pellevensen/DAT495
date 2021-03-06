package testing;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The purpose of this class is to generate pseudo random permutations without
 * storing any of the indices. The memory usage is constant (and small)
 * regardless of the permutation size.
 *
 * Useful for testing when one wants to make sure that all values occur at most
 * once.
 *
 * @author Pelle Evensen
 */
public class PRP implements Iterable<Long> {
	// Some odd nothing-up-my-sleeve-numbers.
	private static final long SQRT3 = 0xBB67AE8584CAA73BL;
	private static final long SQRT5 = 0x3C6EF372FE94F82BL;
	private static final long SQRT7 = 0xA54FF53A5F1D36F1L;

	private final long size;
	private final long runs;
	private final long seed;
	private final long mask;
	private final int s1, s2;

	/**
	 * @param size The size of the permutation.
	 * @param seed The randomization seed.
	 */
	private PRP(final long size, final long seed, final long runs) {
		this.size = size;
		@SuppressWarnings("hiding")
		var mask = 0L;
		var i = 0;
		while (mask < size * runs) {
			mask = mask << 1 | 1;
			i++;
		}
		this.mask = mask;
		this.seed = seed;
		this.runs = runs;
		this.s1 = Math.max(i / 3, 1);
		this.s2 = Math.max(2 * i / 3, 1);
	}

	public static class Builder {
		private long seed;
		private final long size;
		private long runs;

		public static Builder size(final long size) {
			return new Builder(size);
		}

		private Builder(final long size) {
			if (size < 0) {
				throw new IllegalArgumentException("size must be >= 0 (was " + size + ").");
			}
			this.size = size;
			this.seed = 0;
			this.runs = 1;
		}

		/**
		 * Each number will occur exactly {@code runs} times.
		 */
		public Builder runs(final long runs) {
			if (runs < 0) {
				throw new IllegalArgumentException("Number of runs must be >= 0 (was " + runs + ").");
			}
			this.runs = runs;
			return this;
		}

		public Builder seed(final long seed) {
			this.seed = seed;
			return this;
		}

		public PRP build() {
			if (Long.numberOfLeadingZeros(this.size) + Long.numberOfLeadingZeros(this.runs) < 65) {
				throw new IllegalStateException("size * runs must be < 2^63");
			}
			return new PRP(this.size, this.seed, this.runs);
		}
	}

	// A decent approximation of a psuedo-random permutation on [-2^63, 2^63).
	// this.seed influences what permutation is chosen.
	// (Of course this is a small subset since there are size! permutations but the
	// seed has cardinality
	// [typically] much smaller than size!.)
	//
	// Different seeds are not guaranteed to yield different permutations although
	// it's highly likely for larger
	// values of size.
	private long mix(final long v0) {
		var v = v0;
		var s = this.seed;
		for (var i = 0; i < 3; i++) {
			s = s + PRP.SQRT3;
			s ^= s >>> 49 ^ s >>> 25;
			v ^= v >>> this.s1 ^ v >>> this.s2;
			v = v + s & this.mask;
			s = s * PRP.SQRT5 & this.mask;
			v = v * PRP.SQRT7 & this.mask;
		}
		return v;
	}

	/**
	 * Gives an iterator that will be able to iterate for {@code size} elements and
	 * contain each element [0, size) exactly once.
	 *
	 * Iterator::next runs in amortized constant time.
	 */
	@Override
	public Iterator<Long> iterator() {
		return new Iterator<>() {
			private long ctr = 0;
			private long used = 0;

			@Override
			public boolean hasNext() {
				return this.used < PRP.this.size * PRP.this.runs;
			}

			@SuppressWarnings("boxing")
			@Override
			public Long next() {
				if (this.used == PRP.this.size * PRP.this.runs) {
					throw new NoSuchElementException();
				}
				long v;
				do {
					v = mix(this.ctr++);
				} while (v >= PRP.this.size * PRP.this.runs);
				this.used++;
				return v % PRP.this.size;
			}

		};
	}

	/**
	 * Gives the exact same output as the regular {@code iterator()} but in reverse
	 * order.
	 */
	public Iterable<Long> inReverse() {
		return () -> new Iterator<>() {
			private long ctr = PRP.this.mask;
			private long used = 0;

			@Override
			public boolean hasNext() {
				return this.used < PRP.this.size * PRP.this.runs;
			}

			@SuppressWarnings("boxing")
			@Override
			public Long next() {
				if (this.used == PRP.this.size * PRP.this.runs) {
					throw new NoSuchElementException();
				}
				long v;
				do {
					v = mix(this.ctr--);
				} while (v >= PRP.this.size * PRP.this.runs);
				this.used++;
				return v % PRP.this.size;
			}

		};
	}

	@SuppressWarnings("boxing")
	@Override
	public int hashCode() {
		return Objects.hash(this.seed, this.size, this.runs);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final var other = (PRP) obj;
		return this.seed == other.seed && this.size == other.size && this.runs == other.runs;
	}

}