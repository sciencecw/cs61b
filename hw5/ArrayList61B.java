import java.util.AbstractList;

class ArrayList61B<E> extends AbstractList<E>{
	private E[] ar;
	private int size=0;

	public ArrayList61B(int initialCapacity){
		if (initialCapacity<1){
			throw new IllegalArgumentException();
		}
		ar = (E[]) new Object[initialCapacity];
		return;
	}

	public ArrayList61B(){
		this(1);
	}

	@Override
	public E get(int i){
		if (i<0||i>=size){
			throw new IllegalArgumentException();
		}
		return ar[i];
	}

	@Override
	public boolean add(E item){
		if (size == ar.length){
			this.resize(size*2);
		}
		ar[size]=item;
		size=size+1;
		return true;
	}

	@Override
	public int size(){
		return size;
	}

	private void resize(int arsize){
		if (arsize<1){
			throw new IllegalArgumentException();
		}
		E[] temp = (E[]) new Object[arsize];
		for (int i = 0; i < ar.length; i++){
			temp[i] = ar[i];
		}
		ar=temp;
		return;
	}


}