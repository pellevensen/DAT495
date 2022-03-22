package w1_sorting;

/**
 *
 * @author evensen
 *
 */
public class SelectionSort implements IntSorter {
	@Override
	public void sort(int[] arr) {
		for(int i = 0; i < arr.length - 1; i++) {
			int min = arr[i];
			int minIdx = i;
			for(int j = i + 1; j < arr.length; j++) {
				if(arr[j] < min) {
					min = arr[j];
					minIdx = j;
				}
			}
			arr[minIdx] = arr[i];
			arr[i] = min;
		}
	}
}
