package prover.utility.utilities;

import java.util.Objects;

import prover.utility.Utility;

public class Pair<L, R> extends Utility {

	public L left;
	public R right;
	
	protected Pair(L left, R right){
		this.left = left;
		this.right = right;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Pair<?, ?> o = (Pair<?, ?>) obj;
		return Objects.equals(left, o.left) && Objects.equals(right, o.right);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(left) + 31 * Objects.hashCode(right);
	}
	
	@Override
	public String toString() {
		return "" + '<' + left + ',' + ' ' + right + '>';
	}
}