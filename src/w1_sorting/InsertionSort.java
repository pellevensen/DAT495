package w1_sorting;

/**
 *
 * @author evensen
 *
 */
public class InsertionSort implements IntSorter {
	@Override
	public void sort(int[] arr) {
		for(int i = 1; i < arr.length; i++) {
			int j = i;
			while(j > 0 && arr[j] < arr[j - 1]) {
				int tmp = arr[j];
				arr[j] = arr[j - 1];
				arr[j - 1] = tmp;
				j--;
			}
		}
	}
}
